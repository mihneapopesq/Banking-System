package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.User;
import org.poo.utilities.users.Account;
import org.poo.utils.Utils;

import java.util.Arrays;

public class AddAccount {
    public void addAccount(User[] users, CommandInput commandInput) {
        ObjectMapper objectMapper = new ObjectMapper();

        for (User user : users) {
           if(user.getUser().getEmail().equals(commandInput.getEmail())) {

               Account newAccount = new Account();
               Account[] existingAccounts = user.getAccounts();
               Account[] updatedAccounts = Arrays.copyOf(existingAccounts, existingAccounts.length + 1);

               // fa un constructor
               newAccount.setAccountType(commandInput.getAccountType());
               newAccount.setCurrency(commandInput.getCurrency());
               newAccount.setIban(Utils.generateIBAN());



               updatedAccounts[existingAccounts.length] = newAccount;
               user.setAccounts(updatedAccounts);

               break;
           }
        }
    }
}
