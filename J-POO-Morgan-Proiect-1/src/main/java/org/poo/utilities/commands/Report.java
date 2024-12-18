package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;

public class Report extends CommandBase {
    private final ArrayNode output;
    private final ArrayList<User> users;
    private final ObjectMapper objectMapper;
    private final ObjectNode commandNode;
    private final CommandInput commandInput;
    private final ArrayList<Transaction> transactions;

    public Report(Builder builder) {
        this.output = builder.getOutput();
        this.users = builder.getUsers();
        this.objectMapper = builder.getObjectMapper();
        this.commandNode = builder.getCommandNode();
        this.commandInput = builder.getCommandInput();
        this.transactions = builder.getTransactions();
    }

    @Override
    public void execute() {
        commandNode.put("command", commandInput.getCommand());
        commandNode.put("timestamp", commandInput.getTimestamp());

        Account foundAccount = null;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(commandInput.getAccount())) {
                    foundAccount = account;
                    break;
                }
            }
            if (foundAccount != null) {
                break;
            }
        }

        if (foundAccount == null) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("description", "Account not found");
            errorNode.put("timestamp", commandInput.getTimestamp());
            commandNode.set("output", errorNode);
            output.add(commandNode);
            return;
        }

        ObjectNode reportNode = objectMapper.createObjectNode();
        reportNode.put("balance", foundAccount.getBalance());
        reportNode.put("currency", foundAccount.getCurrency());
        reportNode.put("IBAN", foundAccount.getIban());

        ArrayNode transactionsArray = objectMapper.createArrayNode();

        ArrayList<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getTimestamp() >= commandInput.getStartTimestamp()
                    && transaction.getTimestamp() <= commandInput.getEndTimestamp()) {
                accountTransactions.add(transaction);
            }
        }

        for (Transaction transaction : accountTransactions) {
            if (transaction.getReportIban().equals(foundAccount.getIban())) {

                ObjectNode transactionNode = objectMapper.createObjectNode();
                transactionNode.put("timestamp", transaction.getTimestamp());
                transactionNode.put("description", transaction.getDescription());

                if (transaction.getSenderIBAN() != null) {
                    transactionNode.put("senderIBAN", transaction.getSenderIBAN());
                }

                if (transaction.getReceiverIBAN() != null) {
                    transactionNode.put("receiverIBAN", transaction.getReceiverIBAN());
                }

                if (transaction.getAmount() != 0 && transaction.getCurrency() != null) {
                    transactionNode.put("amount", transaction.getAmount() + " " + transaction.getCurrency());
                }

                if (transaction.getTransferType() != null) {
                    transactionNode.put("transferType", transaction.getTransferType());
                }

                if (transaction.getCardNumber() != null) {
                    transactionNode.put("card", transaction.getCardNumber());
                }

                if (transaction.getCardHolder() != null) {
                    transactionNode.put("cardHolder", transaction.getCardHolder());
                }

                if (transaction.getIban() != null) {
                    transactionNode.put("account", transaction.getIban());
                }

                if (transaction.getCommerciant() != null) {
                    transactionNode.put("commerciant", transaction.getCommerciant());
                }

                if (transaction.getAmountSpent() > 0) {
                    transactionNode.put("amount", transaction.getAmountSpent());
                }

                if (transaction.getAccounts() != null) {
                    transactionNode.remove("description");
                    transactionNode.remove("amount");
                    transactionNode.remove("currency");
                    transactionNode.remove("accounts");

                    String formattedAmount = String.format("%.2f", transaction.getAmount());

                    transactionNode.put("amount", transaction.getAmountSpent());
                    transactionNode.put("currency", transaction.getCurrency());
                    transactionNode.put("description", "Split payment of " + formattedAmount + " " + transaction.getCurrency());

                    if (transaction.getErrorAccount() != null) {
                        transactionNode.put("error", transaction.getErrorAccount());
                    }

                    ArrayNode involvedAccounts = objectMapper.createArrayNode();
                    for (String acc : transaction.getAccounts()) {
                        involvedAccounts.add(acc);
                    }
                    transactionNode.set("involvedAccounts", involvedAccounts);
                }

                transactionsArray.add(transactionNode);
            }
        }

        reportNode.set("transactions", transactionsArray);
        commandNode.set("output", reportNode);
        output.add(commandNode);
    }
}
