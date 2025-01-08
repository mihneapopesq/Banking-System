package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;

/**
 * Command for deleting a user account.
 * An account can only be deleted if its balance is zero.
 */
public class DeleteAccount extends CommandBase {

    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final ArrayNode output;
    private final ObjectMapper objectMapper;
    private final ObjectNode commandNode;
    private final ArrayList<Transaction> transactions;

    /**
     * Constructs the DeleteAccount command using the provided builder.
     *
     * @param builder the builder containing the dependencies and configuration for this command.
     */
    public DeleteAccount(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.output = builder.getOutput();
        this.objectMapper = builder.getObjectMapper();
        this.commandNode = builder.getCommandNode();
        this.transactions = builder.getTransactions();
    }

    /**
     * Executes the command to delete a user account.
     * If the account balance is zero, the account is deleted.
     * Otherwise, an error is added to the output and a transaction is recorded.
     */
    @Override
    public void execute() {
        boolean accountDeleted = false;

        for (User user : users) {
            if (user.getUser().getEmail().equals(commandInput.getEmail())) {
                for (int i = user.getAccounts().size() - 1; i >= 0; i--) {
                    Account acc = user.getAccounts().get(i);
                    if (acc.getIban().equals(commandInput.getAccount()) && acc.getBalance() == 0) {
                        user.getAccounts().remove(i);
                        accountDeleted = true;
                    }
                }
            }
        }

        commandNode.put("command", commandInput.getCommand());
        ObjectNode outputNode = objectMapper.createObjectNode();

        if (accountDeleted) {
            outputNode.put("success", "Account deleted");
        } else {
            outputNode.put("error",
                    "Account couldn't be deleted - see org.poo.transactions for details");

            Transaction transaction = new Transaction(
                    "Account couldn't be deleted - there are funds remaining",
                    commandInput.getTimestamp(),
                    commandInput.getEmail()
            );
            transactions.add(transaction);
        }

        outputNode.put("timestamp", commandInput.getTimestamp());
        commandNode.set("output", outputNode);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
}
