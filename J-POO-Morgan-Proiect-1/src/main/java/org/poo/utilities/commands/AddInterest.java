package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.User;

import java.util.ArrayList;

/**
 * Command for adding interest to a savings account.
 * Interest is added to the balance based on the account's interest rate.
 */
public class AddInterest extends CommandBase {
    private final ArrayNode output;
    private final ArrayList<User> users;
    private final ObjectMapper objectMapper;
    private final ObjectNode commandNode;
    private final CommandInput commandInput;

    /**
     * Constructs the AddInterest command using the provided builder.
     *
     * @param builder the builder containing the dependencies and configuration for this command.
     */
    public AddInterest(final Builder builder) {
        this.output = builder.getOutput();
        this.users = builder.getUsers();
        this.objectMapper = builder.getObjectMapper();
        this.commandNode = builder.getCommandNode();
        this.commandInput = builder.getCommandInput();
    }

    /**
     * Executes the command to add interest to a savings account.
     * If the account is not a savings account, an error is added to the output.
     */
    @Override
    public void execute() {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(commandInput.getAccount())) {
                    if (!account.getAccountType().equals("savings")) {
                        account.addErrorResponse(objectMapper, commandNode, output,
                                commandInput, "This is not a savings account");
                        return;
                    }
                    account.setBalance(account.getBalance()
                            + account.getBalance() * account.getInterestRate());
                }
            }
        }
    }
}
