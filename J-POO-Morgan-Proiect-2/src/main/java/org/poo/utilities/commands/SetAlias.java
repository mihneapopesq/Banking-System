package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.User;
import java.util.ArrayList;

/**
 * Command for setting an alias for a specific account.
 */
public class SetAlias extends CommandBase {

    private final ArrayList<User> users;
    private final CommandInput commandInput;

    /**
     * Constructs the SetAlias command using the provided builder.
     *
     * @param builder the builder containing dependencies and configuration for this command.
     */
    public SetAlias(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
    }

    /**
     * Executes the command to set an alias for a specific account.
     * Updates the account's alias if the user and account match the provided input.
     */

    @Override
    public void execute() {
        for (User user : users) {
            if (user.getUser().getEmail().equals(commandInput.getEmail())) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(commandInput.getAccount())) {
                        account.setAlias(commandInput.getAlias());
                        return;
                    }
                }
                return;
            }
        }
    }
}
