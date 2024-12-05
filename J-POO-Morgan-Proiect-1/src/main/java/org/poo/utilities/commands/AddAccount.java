package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;

public class AddAccount {
    public void addAccount(ArrayList<UserInput> users, CommandInput commandInput) {
        ObjectMapper objectMapper = new ObjectMapper();

        for (UserInput user : users) {
            if(user.getEmail().equals(commandInput.getEmail())) {
                if(!commandInput.getCurrency().isEmpty()) {



//                    ObjectNode account = objectMapper.createObjectNode();
//                    account.put("currency", commandInput.getCurrency());
                } else if(!commandInput.getAccountType().isEmpty()) {
//                    ObjectNode account = objectMapper.createObjectNode();
//                    account.put("accountType", commandInput.getAccountType());

                } else if(commandInput.getAmount() > 0) {
//                    ObjectNode account = objectMapper.createObjectNode();
//                    account.put("amount", commandInput.getAmount());
                }
                break;
            }
        }
    }
}
