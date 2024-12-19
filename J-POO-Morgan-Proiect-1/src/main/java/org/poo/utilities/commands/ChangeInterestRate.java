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
 * Command for changing the interest rate of a savings account.
 * Records the change as a transaction and updates the account's interest rate.
 */
public class ChangeInterestRate extends CommandBase {
    private final ArrayNode output;
    private final ArrayList<User> users;
    private final ObjectMapper objectMapper;
    private final ObjectNode commandNode;
    private final CommandInput commandInput;
    private final ArrayList<Transaction> transactions;

    /**
     * Constructs the ChangeInterestRate command using the provided builder.
     *
     * @param builder the builder containing the dependencies and configuration for this command.
     */
    public ChangeInterestRate(final Builder builder) {
        this.output = builder.getOutput();
        this.users = builder.getUsers();
        this.objectMapper = builder.getObjectMapper();
        this.commandNode = builder.getCommandNode();
        this.commandInput = builder.getCommandInput();
        this.transactions = builder.getTransactions();
    }

    /**
     * Executes the command to change the interest rate of a savings account.
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

                    account.setInterestRate(commandInput.getInterestRate());

                    Transaction transaction = new Transaction(
                            "Interest rate of the account changed to "
                                    + commandInput.getInterestRate(), commandInput.getTimestamp(),
                            user.getUser().getEmail()
                    );
                    transactions.add(transaction);
                }
            }
        }
    }
}
