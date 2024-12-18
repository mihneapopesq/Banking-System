package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.User;
import org.poo.utilities.users.Card;

import java.util.ArrayList;

public class PrintUsers extends CommandBase {

    private final ArrayNode output;
    private final ArrayList<User> users;
    private final ObjectMapper objectMapper;
    private final ObjectNode commandNode;
    private final CommandInput command;

    public PrintUsers(Builder builder) {
        this.output = builder.getOutput();
        this.users = builder.getUsers();
        this.objectMapper = builder.getObjectMapper();
        this.commandNode = builder.getCommandNode();
        this.command = builder.getCommandInput();
    }

    @Override
    public void execute() {
        commandNode.put("command", command.getCommand());

        ArrayNode usersOutput = objectMapper.createArrayNode();

        for (User user : users) {
            ObjectNode userNode = objectMapper.createObjectNode();
            userNode.put("firstName", user.getUser().getFirstName());
            userNode.put("lastName", user.getUser().getLastName());
            userNode.put("email", user.getUser().getEmail());

            ArrayNode accountsNode = objectMapper.createArrayNode();

            for (Account account : user.getAccounts()) {
                ObjectNode accountNode = objectMapper.createObjectNode();
                accountNode.put("IBAN", account.getIban());
                accountNode.put("balance", account.getBalance());
                accountNode.put("currency", account.getCurrency());
                accountNode.put("type", account.getAccountType());

                ArrayNode cardsNode = objectMapper.createArrayNode();

                if (account.getCards() != null) {
                    for (Card card : account.getCards()) {
                        ObjectNode cardNode = objectMapper.createObjectNode();
                        cardNode.put("cardNumber", card.getCardNumber());
                        cardNode.put("status", card.getStatus());
                        cardsNode.add(cardNode);
                    }
                    accountNode.set("cards", cardsNode);
                }
                accountsNode.add(accountNode);
            }

            userNode.set("accounts", accountsNode);
            usersOutput.add(userNode);
        }

        commandNode.set("output", usersOutput);
        commandNode.put("timestamp", command.getTimestamp());
        output.add(commandNode);
    }

}
