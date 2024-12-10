package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.*;

import java.util.ArrayList;
import java.util.List;

public class SplitPayment {
    public void splitPayment(ArrayList<User> users, CommandInput commandInput, CurrencyGraph graph,
                             ArrayList<Transaction> transactions) {
        List<String> accounts = commandInput.getAccounts();
        int numberOfAccounts = accounts.size();
        double amountPerAccount = commandInput.getAmount() / numberOfAccounts;

        for (String iban : accounts) {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(iban)) {
                        Transaction userTransaction = new Transaction();
                        userTransaction.setDescription("Split payment of");
                        userTransaction.setAmountSpent(amountPerAccount);
                        userTransaction.setCurrency(commandInput.getCurrency());
                        userTransaction.setAmount(commandInput.getAmount());
                        userTransaction.setAccounts(accounts);
                        userTransaction.setTimestamp(commandInput.getTimestamp());
                        userTransaction.setEmail(user.getUser().getEmail());

                        transactions.add(userTransaction);

                        double rightAmount = graph.convertCurrency(account.getCurrency(), commandInput.getCurrency(), amountPerAccount);
                        account.setBalance(account.getBalance() - rightAmount);
                    }
                }
            }
        }
    }
}
