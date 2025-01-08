package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Transaction;

import java.util.ArrayList;

/**
 * Command for printing transactions associated with a specific email.
 * Filters and displays transactions for the user specified in the command input.
 */
public class PrintTransactions extends CommandBase {

    private final ArrayNode output;
    private final ObjectMapper objectMapper;
    private final ObjectNode commandNode;
    private final CommandInput commandInput;
    private final ArrayList<Transaction> transactions;

    /**
     * Constructs the PrintTransactions command using the provided builder.
     *
     * @param builder the builder containing the dependencies and configuration for this command.
     */
    public PrintTransactions(final Builder builder) {
        this.output = builder.getOutput();
        this.objectMapper = builder.getObjectMapper();
        this.commandNode = builder.getCommandNode();
        this.commandInput = builder.getCommandInput();
        this.transactions = builder.getTransactions();
    }

    /**
     * Executes the command to filter and print transactions.
     * Iterates through the list of transactions and outputs those that match the email
     * specified in the command input.
     */
    @Override
    public void execute() {
        commandNode.put("command", commandInput.getCommand());
        commandNode.put("timestamp", commandInput.getTimestamp());

        ArrayNode outputArray = objectMapper.createArrayNode();

        for (Transaction transaction : transactions) {
            if (transaction.getEmail().equals(commandInput.getEmail())) {
                ObjectNode transactionNode = objectMapper.createObjectNode();
                transaction.populateTransactionNode(transaction, transactionNode, objectMapper);
                outputArray.add(transactionNode);
            }
        }

        commandNode.set("output", outputArray);
        output.add(commandNode);
    }
}
