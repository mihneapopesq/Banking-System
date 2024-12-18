package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.*;

import java.util.ArrayList;
import java.util.List;

public class SplitPayment extends CommandBase {
    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final CurrencyGraph graph;
    private final ArrayList<Transaction> transactions;

    public SplitPayment(Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.graph = builder.getCurrencyGraph();
        this.transactions = builder.getTransactions();
    }

    @Override
    public void execute() {
        List<String> accounts = commandInput.getAccounts();
        int numberOfAccounts = accounts.size();
        double amountPerAccount = commandInput.getAmount() / numberOfAccounts;

        boolean canSplit = true;
        Account invalidAccount = null;

        for (String iban : accounts) {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(iban)) {
                        double rightAmount = graph.convertCurrency(commandInput.getCurrency(), account.getCurrency(), amountPerAccount);

                        if (account.getMinBalance() > account.getBalance() - rightAmount) {
                            invalidAccount = account;
                            canSplit = false;
                        }
                    }
                }
            }
        }

        if (canSplit) {
            for (String iban : accounts) {
                for (User user : users) {
                    for (Account account : user.getAccounts()) {
                        if (account.getIban().equals(iban)) {
                            Transaction userTransaction = new Transaction(
                                    "Split payment of",
                                    amountPerAccount,
                                    commandInput.getCurrency(),
                                    commandInput.getAmount(),
                                    accounts,
                                    commandInput.getTimestamp(),
                                    user.getUser().getEmail(),
                                    account.getIban()
                            );

                            transactions.add(userTransaction);

                            double rightAmount = graph.convertCurrency(commandInput.getCurrency(), account.getCurrency(), amountPerAccount);

                            account.setBalance(account.getBalance() - rightAmount);
                        }
                    }
                }
            }
        } else {
            for (String iban : accounts) {
                for (User user : users) {
                    for (Account account : user.getAccounts()) {
                        if (account.getIban().equals(iban)) {
                            Transaction userTransaction = new Transaction(
                                    "Split payment of",
                                    amountPerAccount,
                                    commandInput.getCurrency(),
                                    commandInput.getAmount(),
                                    accounts,
                                    commandInput.getTimestamp(),
                                    user.getUser().getEmail(),
                                    account.getIban(),
                                    "Account " + invalidAccount.getIban() + " has insufficient funds for a split payment."
                            );

                            transactions.add(userTransaction);
                        }
                    }
                }
            }
        }
    }
}
