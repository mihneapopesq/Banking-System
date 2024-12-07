package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.User;

public class AddFunds {
    public void AddFunds(User[] users, CommandInput commandInput){
        for(User user : users) {
            for(Account account : user.getAccounts()) {
                if(account.getIban() != null && account.getIban().equals(commandInput.getAccount())) {
                    account.setBalance(account.getBalance() + commandInput.getAmount());
                    return ;
                }
            }
        }
    }
}
