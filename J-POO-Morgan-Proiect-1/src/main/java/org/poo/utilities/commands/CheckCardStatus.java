package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Card;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;

/**
 * Command for checking the status of a card and freezing it if funds are insufficient.
 */
public class CheckCardStatus extends CommandBase {
    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final ArrayNode output;
    private final ObjectNode commandNode;
    private final ObjectMapper objectMapper;
    private final ArrayList<Transaction> transactions;

    /**
     * Constructs the CheckCardStatus command using the provided builder.
     *
     * @param builder the builder containing the dependencies and configuration for this command.
     */
    public CheckCardStatus(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.output = builder.getOutput();
        this.commandNode = builder.getCommandNode();
        this.objectMapper = builder.getObjectMapper();
        this.transactions = builder.getTransactions();
    }

    /**
     * Executes the command, checking the status of a card.
     * If the card's associated account balance is insufficient, the card is frozen,
     * and a corresponding transaction is recorded.
     * If the card is not found, an error message is added to the output.
     */
    @Override
    public void execute() {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(commandInput.getCardNumber())) {
                        if (account.getBalance() - account.getMinBalance() <= 30) {
                            Transaction transaction = new Transaction(
                                    "You have reached the minimum amount of funds, "
                                            + "the card will be frozen",
                                    commandInput.getTimestamp(),
                                    user.getUser().getEmail(),
                                    account.getIban()
                            );
                            transactions.add(transaction);

                            card.setIsFrozen(1);
                            card.setStatus("frozen");
                            return;
                        }
                        return;
                    }
                }
            }
        }

        commandNode.put("command", commandInput.getCommand());
        ObjectNode messageNode = objectMapper.createObjectNode();
        messageNode.put("timestamp", commandInput.getTimestamp());
        messageNode.put("description", "Card not found");
        commandNode.set("output", messageNode);
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
}
