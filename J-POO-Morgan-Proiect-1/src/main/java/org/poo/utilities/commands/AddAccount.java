package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.User;
import org.poo.utilities.users.Account;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class AddAccount {
    public void addAccount(ArrayList<User> users, CommandInput commandInput) {
        for (User user : users) {
           if(user.getUser().getEmail().equals(commandInput.getEmail())) {


               String generatedIBAN = Utils.generateIBAN();

               // fa un constructor fast
               Account newAccount = new Account();
               newAccount.setAccountType(commandInput.getAccountType());
               newAccount.setCurrency(commandInput.getCurrency());
               newAccount.setIban(generatedIBAN);
               newAccount.setBalance(0);
               newAccount.setCards(new ArrayList<>());

               ArrayList<Account> accounts = user.getAccounts();

               accounts.add(newAccount);

               user.setAccounts(accounts);

               break;
           }
        }
    }
}
