package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;
import org.poo.utilities.users.Account;
import org.poo.utils.Utils;

import java.util.ArrayList;

/**
 * Command for adding a new account to a user.
 * Automatically generates an IBAN and initializes the account with zero balance.
 */
public class AddAccount extends CommandBase {
    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final ArrayList<Transaction> transactions;

    /**
     * Constructs the AddAccount command using the provided builder.
     *
     * @param builder the builder containing the dependencies and configuration for this command.
     */
    public AddAccount(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.transactions = builder.getTransactions();
    }

    /**
     * Executes the command to create a new account for a user.
     * Adds the account to the user's account list and records the transaction.
     */
    @Override
    public void execute() {
        for (User user : users) {
            if (user.getUser().getEmail().equals(commandInput.getEmail())) {
                String generatedIBAN = Utils.generateIBAN();

                Account newAccount = new Account(
                        generatedIBAN,
                        commandInput.getCurrency(),
                        commandInput.getAccountType(),
                        commandInput.getInterestRate(),
                        0,
                        0
                );

                Transaction transaction = new Transaction(
                        "New account created",
                        commandInput.getTimestamp(),
                        user.getUser().getEmail(),
                        generatedIBAN
                );
                transactions.add(transaction);

                user.getAccounts().add(newAccount);
                break;
            }
        }
    }
}
