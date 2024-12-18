package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.User;

import java.util.ArrayList;

public class AddFunds extends CommandBase {
    private ArrayList<User> users;
    private CommandInput commandInput;

    public AddFunds(Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
    }

    @Override
    public void execute() {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(commandInput.getAccount())) {
                    account.setBalance(account.getBalance() + commandInput.getAmount());
                    return;
                }
            }
        }
    }
}
