package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.BusinessAccount;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class AddNewBusinessAssociate extends CommandBase{
    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final ArrayList<Transaction> transactions;
    private final ArrayList<BusinessAccount> businessAccounts;

    public AddNewBusinessAssociate(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.transactions = builder.getTransactions();
        this.businessAccounts = builder.getBusinessAccounts();
    }

    @Override
    public void execute() {
        for(BusinessAccount account : businessAccounts) {
            if(account.getIban().equals(commandInput.getAccount())) {
                if(commandInput.getRole().equals("employee")) {
                    for (User user : users) {
                        if (user.getUser().getEmail().equals(commandInput.getEmail())) {
                            if (!account.getEmployees().contains(user)) {
                                account.getEmployees().add(user);
                            }
                            return;
                        }
                    }
                } else if(commandInput.getRole().equals("manager")) {
                    for (User user : users) {
                        if (user.getUser().getEmail().equals(commandInput.getEmail())) {
                            if (!account.getManagers().contains(user)) {
                                account.getManagers().add(user);
                            }
                            return;
                        }
                    }
                }
                return;
            }
        }
    }
}
