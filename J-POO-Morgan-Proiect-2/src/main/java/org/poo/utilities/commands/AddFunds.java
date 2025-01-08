package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.User;

import java.util.ArrayList;

/**
 * Command for adding funds to a specified account.
 */
public class AddFunds extends CommandBase {
    private final ArrayList<User> users;
    private final CommandInput commandInput;

    /**
     * Constructs the AddFunds command using the provided builder.
     *
     * @param builder the builder containing the dependencies and configuration for this command.
     */
    public AddFunds(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
    }

    /**
     * Executes the command to add funds to a specified account.
     * Updates the account's balance by adding the specified amount.
     */
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
