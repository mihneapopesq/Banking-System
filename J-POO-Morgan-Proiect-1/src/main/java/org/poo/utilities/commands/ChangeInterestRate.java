package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.User;

import java.util.ArrayList;

public class ChangeInterestRate {
    public void changeInterestRate(ArrayList<User> users, CommandInput commandInput) {
        for(User user : users) {
            for(Account account : user.getAccounts()) {
                if(account.getIban().equals(commandInput.getAccount())) {
                    account.setInterestRate(commandInput.getInterestRate());
                }
            }
        }

    }
}
