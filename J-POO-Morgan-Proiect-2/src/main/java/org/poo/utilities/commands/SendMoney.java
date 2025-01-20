package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.*;

import java.util.ArrayList;

/**
 * Command for sending money between accounts.
 */
public class SendMoney extends CommandBase {
    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final ArrayList<Transaction> transactions;
    private final CurrencyGraph currencyGraph;
    private final ObjectNode commandNode;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;
    private final ArrayList<BusinessAccount> businessAccounts;

    /**
     * Constructs the SendMoney command using the provided builder.
     *
     * @param builder the builder containing the dependencies and configuration for this command.
     */
    public SendMoney(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.transactions = builder.getTransactions();
        this.currencyGraph = builder.getCurrencyGraph();
        this.commandNode = builder.getCommandNode();
        this.objectMapper = builder.getObjectMapper();
        this.output = builder.getOutput();
        this.businessAccounts = builder.getBusinessAccounts();

    }

    /**
     * Executes the command to send money.
     * Validates account balances and performs currency conversion before transferring funds.
     */
    @Override
    public void execute() {


        /*
        * {
            "command": "sendMoney",
            "account": "RO65POOB8944016386845896",
            "amount": 3321.0,
            "receiver": "RO58POOB7344468893732422",
            "timestamp": 41,
            "email": "Christopher_Jones_MD@hotmail.us",
            "description": "Electricity and internet bills"
        },
        *
        * */


        BusinessAccount senderBusinessAccount = null;
        for (BusinessAccount businessAccount : businessAccounts) {
            if (businessAccount.getIban().equals(commandInput.getAccount())) {
                senderBusinessAccount = businessAccount;
                break;
            }
        }

        Account senderAccount = null;
        User senderUser = null;
        for (User user : users) {
            if (user.getUser().getEmail().equals(commandInput.getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(commandInput.getAccount())) {
                        senderAccount = account;
                        senderUser = user;
                        break;
                    }
                }
            }
        }

        Account receiverAccount = null;
        User receiverUser = null;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(commandInput.getReceiver())) {
                    receiverAccount = account;
                    receiverUser = user;
                    break;
                }
            }
        }

        if (senderBusinessAccount != null && receiverAccount != null) {
            boolean isEmployee = false;
            boolean isManager = false;

            for (User employee : senderBusinessAccount.getEmployees()) {
                if (employee.getUser().getEmail().equals(commandInput.getEmail())) {
                    isEmployee = true;
                    break;
                }
            }

            for (User manager : senderBusinessAccount.getManagers()) {
                if (manager.getUser().getEmail().equals(commandInput.getEmail())) {
                    isManager = true;
                    break;
                }
            }

            double amount = commandInput.getAmount();
            double amountInRON = currencyGraph.convertCurrency(senderBusinessAccount.getCurrency(), "RON", amount);

            if (isEmployee && amountInRON > 500) {
                Transaction transaction = new Transaction(
                        "Spending limit exceeded",
                        commandInput.getTimestamp(),
                        commandInput.getEmail(),
                        senderBusinessAccount.getIban()
                );
                transactions.add(transaction);

                // Adăugăm mesaj de eroare la output
                ObjectNode errorNode = objectMapper.createObjectNode();
                errorNode.put("timestamp", commandInput.getTimestamp());
                errorNode.put("description", "Employees cannot spend more than 500 RON.");
                commandNode.set("output", errorNode);
                commandNode.put("command", "sendMoney");
                commandNode.put("timestamp", commandInput.getTimestamp());
                output.add(commandNode);
                return;
            }

            // Verificăm dacă fondurile sunt suficiente și nu scad sub minimul permis
            if (senderBusinessAccount.getBalance() < amount ||
                    senderBusinessAccount.getBalance() - amount < senderBusinessAccount.getMinBalance()) {
                Transaction transaction = new Transaction(
                        "Insufficient funds",
                        commandInput.getTimestamp(),
                        senderBusinessAccount.getOwnerEmail(),
                        senderBusinessAccount.getIban()
                );
                transactions.add(transaction);

                // Adăugăm mesaj de eroare la output
                ObjectNode errorNode = objectMapper.createObjectNode();
                errorNode.put("timestamp", commandInput.getTimestamp());
                errorNode.put("description", "Insufficient funds or below minimum balance.");
                commandNode.set("output", errorNode);
                commandNode.put("command", "sendMoney");
                commandNode.put("timestamp", commandInput.getTimestamp());
                output.add(commandNode);
                return;
            }

            double convertedAmount = currencyGraph.convertCurrency(
                    senderBusinessAccount.getCurrency(),
                    receiverAccount.getCurrency(),
                    amount
            );

            senderBusinessAccount.setBalance(senderBusinessAccount.getBalance() - amount);
            receiverAccount.setBalance(receiverAccount.getBalance() + convertedAmount);

//            Transaction senderTransaction = new Transaction(
//                    amount,
//                    senderBusinessAccount.getCurrency(),
//                    "sent",
//                    commandInput.getTimestamp(),
//                    commandInput.getDescription(),
//                    senderBusinessAccount.getIban(),
//                    receiverAccount.getIban(),
//                    senderBusinessAccount.getOwnerEmail(),
//                    senderBusinessAccount.getIban()
//            );
//            transactions.add(senderTransaction);
//
//            Transaction receiverTransaction = new Transaction(
//                    convertedAmount,
//                    receiverAccount.getCurrency(),
//                    "received",
//                    commandInput.getTimestamp(),
//                    commandInput.getDescription(),
//                    senderBusinessAccount.getIban(),
//                    receiverAccount.getIban(),
//                    receiverUser != null ? receiverUser.getUser().getEmail() : "Unknown",
//                    receiverAccount.getIban()
//            );
//            transactions.add(receiverTransaction);
            return;
        }




        double amount = commandInput.getAmount();

        if (receiverAccount == null || senderAccount == null) {

            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", commandInput.getTimestamp());
            errorNode.put("description", "User not found");
            commandNode.set("output", errorNode);
            commandNode.put("command", "sendMoney");
            commandNode.put("timestamp", commandInput.getTimestamp());
            output.add(commandNode);


            return;
        }

        if (senderAccount.getBalance() < amount) {
            Transaction transaction = new Transaction(
                    "Insufficient funds",
                    commandInput.getTimestamp(),
                    senderUser.getUser().getEmail(),
                    senderAccount.getIban()
            );
            transactions.add(transaction);
            return;
        }

//        System.out.printf("la tmstp %d, minbalance %f, balance %f, amount %f\n",
//                commandInput.getTimestamp(), senderAccount.getMinBalance(),
//                senderAccount.getBalance(), amount);



        if (senderAccount.getMinBalance() >= senderAccount.getBalance() - amount) {
            Transaction transaction = new Transaction(
                    "Insufficient funds",
                    commandInput.getTimestamp(),
                    senderUser.getUser().getEmail(),
                    senderAccount.getIban()
            );
            transactions.add(transaction);
            return;
        }

        double rightAmount = currencyGraph.convertCurrency(senderAccount.getCurrency(),
                receiverAccount.getCurrency(), amount);

        senderAccount.setBalance(senderAccount.getBalance() - amount);
        receiverAccount.setBalance(receiverAccount.getBalance() + rightAmount);


        if(senderUser.getUserPlan().equals("standard")) {
            double comision = amount * 0.002;
            senderAccount.setBalance(senderAccount.getBalance() - comision);
        }

        double amountInRON = currencyGraph.convertCurrency(senderAccount.getCurrency(), "RON", amount);
        if(amountInRON >= 500 && senderUser.getUserPlan().equals("silver")) {
            double comision = amount * 0.001;
            senderAccount.setBalance(senderAccount.getBalance() - comision);
        }

        Transaction senderTransaction = new Transaction(
                amount,
                senderAccount.getCurrency(),
                "sent",
                commandInput.getTimestamp(),
                commandInput.getDescription(),
                commandInput.getAccount(),
                commandInput.getReceiver(),
                senderUser.getUser().getEmail(),
                senderAccount.getIban()
        );
        transactions.add(senderTransaction);

        Transaction receiverTransaction = new Transaction(
                rightAmount,
                receiverAccount.getCurrency(),
                "received",
                commandInput.getTimestamp(),
                commandInput.getDescription(),
                commandInput.getAccount(),
                commandInput.getReceiver(),
                receiverUser.getUser().getEmail(),
                receiverAccount.getIban()
        );
        transactions.add(receiverTransaction);
    }
}
