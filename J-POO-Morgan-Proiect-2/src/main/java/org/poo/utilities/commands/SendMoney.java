package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.CurrencyGraph;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;

/**
 * Command for sending money between accounts.
 */
public class SendMoney extends CommandBase {
    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final ArrayList<Transaction> transactions;
    private final CurrencyGraph currencyGraph;

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
    }

    /**
     * Executes the command to send money.
     * Validates account balances and performs currency conversion before transferring funds.
     */
    @Override
    public void execute() {
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

        double amount = commandInput.getAmount();

        if (receiverAccount == null || senderAccount == null) {
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

        if (senderAccount.getMinBalance() > senderAccount.getBalance() - amount) {
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
