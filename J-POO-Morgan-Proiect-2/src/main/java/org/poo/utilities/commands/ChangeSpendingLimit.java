package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.BusinessAccount;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;

public class ChangeSpendingLimit extends CommandBase{

    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final ArrayList<BusinessAccount> businessAccounts;
    private final ArrayList<Transaction> transactions;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    /**
     * Constructs the AddFunds command using the provided builder.
     *
     * @param builder the builder containing the dependencies and configuration for this command.
     */
    public ChangeSpendingLimit(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.transactions = builder.getTransactions();
        this.businessAccounts = builder.getBusinessAccounts();
        this.objectMapper = builder.getObjectMapper();
        this.output = builder.getOutput();
    }


    @Override
    public void execute() {
        for(BusinessAccount account : businessAccounts) {
            if(account.getOwnerEmail().equals(commandInput.getEmail())) {
                account.setSpendingLimit(commandInput.getAmount());
                return;
            }
        }
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("description", "You must be owner in order to change spending limit.");
        errorNode.put("timestamp", commandInput.getTimestamp());

        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", "changeSpendingLimit");
        commandNode.put("timestamp", commandInput.getTimestamp());
        commandNode.set("output", errorNode);

        output.add(commandNode);
    }
}
