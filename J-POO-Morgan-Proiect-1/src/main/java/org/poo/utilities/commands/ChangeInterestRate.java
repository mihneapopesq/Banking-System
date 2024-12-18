package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;

public class ChangeInterestRate extends CommandBase {
    private final ArrayNode output;
    private final ArrayList<User> users;
    private final ObjectMapper objectMapper;
    private final ObjectNode commandNode;
    private final CommandInput commandInput;
    private final ArrayList<Transaction> transactions;

    public ChangeInterestRate(Builder builder) {
        this.output = builder.getOutput();
        this.users = builder.getUsers();
        this.objectMapper = builder.getObjectMapper();
        this.commandNode = builder.getCommandNode();
        this.commandInput = builder.getCommandInput();
        this.transactions = builder.getTransactions();
    }

    @Override
    public void execute() {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(commandInput.getAccount())) {

                    if (!account.getAccountType().equals("savings")) {
                        ObjectNode outputNode = objectMapper.createObjectNode();
                        commandNode.put("command", commandInput.getCommand());
                        outputNode.put("description", "This is not a savings account");
                        outputNode.put("timestamp", commandInput.getTimestamp());
                        commandNode.put("timestamp", commandInput.getTimestamp());
                        commandNode.set("output", outputNode);
                        output.add(commandNode);
                        return;
                    }

                    account.setInterestRate(commandInput.getInterestRate());

                    Transaction transaction = new Transaction(
                            "Interest rate of the account changed to " + commandInput.getInterestRate(),
                            commandInput.getTimestamp(),
                            user.getUser().getEmail()
                    );
                    transactions.add(transaction);
                }
            }
        }
    }
}
