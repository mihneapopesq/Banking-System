package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.User;

import java.util.ArrayList;

public class SetMinimumBalance extends CommandBase {
    private final ArrayList<User> users;
    private final CommandInput commandInput;

    public SetMinimumBalance(Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
    }

    @Override
    public void execute() {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(commandInput.getAccount())) {
                    account.setMinBalance(commandInput.getMinBalance());
                    return;
                }
            }
        }
    }

}
