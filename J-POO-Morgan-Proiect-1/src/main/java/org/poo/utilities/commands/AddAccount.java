package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;
import org.poo.utilities.users.Account;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class AddAccount {
    public void addAccount(ArrayList<User> users, CommandInput commandInput, ArrayList<Transaction> transactions) {
        for (User user : users) {
            if (user.getUser().getEmail().equals(commandInput.getEmail())) {
                String generatedIBAN = Utils.generateIBAN();

                Account newAccount = new Account(
                        generatedIBAN,
                        commandInput.getCurrency(),
                        commandInput.getAccountType(),
                        commandInput.getInterestRate(),
                        0,
                        0
                );

                Transaction transaction = new Transaction(
                        "New account created",
                        commandInput.getTimestamp(),
                        user.getUser().getEmail(),
                        generatedIBAN
                );
                transactions.add(transaction);

                user.getAccounts().add(newAccount);
                break;
            }
        }
    }
}
