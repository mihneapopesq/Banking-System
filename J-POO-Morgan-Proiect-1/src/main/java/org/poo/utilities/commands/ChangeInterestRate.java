package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;

public class ChangeInterestRate {
    public void changeInterestRate(final ArrayNode output, final ArrayList<User> users,
                                   final ObjectMapper objectMapper,
                                   final ObjectNode commandNode,
                                   final CommandInput commandInput, final ArrayList<Transaction> transactions) {
        for(User user : users) {
            for(Account account : user.getAccounts()) {
                if(account.getIban().equals(commandInput.getAccount())) {

                    if(account.getAccountType().equals("classic")) {
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
