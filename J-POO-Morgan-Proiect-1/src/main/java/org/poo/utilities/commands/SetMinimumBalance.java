package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.User;

import java.util.ArrayList;

/**
 * Command for setting a minimum balance for a specific account.
 */
public class SetMinimumBalance extends CommandBase {

    private final ArrayList<User> users;
    private final CommandInput commandInput;

    /**
     * Constructs the SetMinimumBalance command using the provided builder.
     *
     * @param builder the builder containing dependencies and configuration for this command.
     */
    public SetMinimumBalance(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
    }

    /**
     * Executes the command to set the minimum balance for a specific account.
     */
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
