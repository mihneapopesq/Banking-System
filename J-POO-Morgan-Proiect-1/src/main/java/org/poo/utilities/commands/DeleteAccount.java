package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;

public class DeleteAccount {
    public void deleteAccount(final ArrayList<User> users, final CommandInput commandInput,
                              final ArrayNode output, final ObjectMapper objectMapper,
                              final ObjectNode commandNode, final ArrayList<Transaction> transactions) {

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
            outputNode.put("error", "Account couldn't be deleted - see org.poo.transactions for details");

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
