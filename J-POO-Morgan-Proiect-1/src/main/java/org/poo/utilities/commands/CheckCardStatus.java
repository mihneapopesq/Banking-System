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

public class CheckCardStatus {
    public void checkCardStatus(ArrayList<User> users, CommandInput commandInput,
                                ArrayNode output, ObjectNode commandNode, ObjectMapper objectMapper,
                                ArrayList<Transaction> transactions) {
        for(User user : users) {
            for(Account account : user.getAccounts()) {
                for(Card card : account.getCards()) {
                    if(card.getCardNumber().equals(commandInput.getCardNumber())) {


                        if(account.getBalance() - account.getMinBalance() <= 30) {
                            Transaction transaction = new Transaction();
                            transaction.setTimestamp(commandInput.getTimestamp());
                            transaction.setDescription("You have reached the minimum amount of funds, the card will be frozen");
                            transaction.setEmail(user.getUser().getEmail());
                            transactions.add(transaction);

                            card.setIsFrozen(1);
                            card.setStatus("frozen");
                            return ;
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
