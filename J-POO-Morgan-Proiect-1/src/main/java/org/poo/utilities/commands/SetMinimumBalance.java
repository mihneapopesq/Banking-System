package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.User;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class SetMinimumBalance {
    public void setMinimumBalance(ArrayList<User> users, CommandInput commandInput) {
        for(User user: users) {
            for(Account account : user.getAccounts()) {
                if(account.getIban().equals(commandInput.getAccount())) {
                    account.setMinBalance(commandInput.getMinBalance());
                }
            }
        }
    }
}
