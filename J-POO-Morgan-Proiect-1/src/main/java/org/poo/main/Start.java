package org.poo.main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.utilities.commands.*;
import org.poo.utilities.users.Card;
import org.poo.utilities.users.User;
import java.util.Arrays;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.utilities.users.Account;
import org.poo.utils.Utils;


public class Start {

    private ArrayList<User> users;
    private ArrayList<CommandInput> commands;

    public Start(ObjectInput inputData, ArrayNode output) {
        users = new ArrayList<>();

        for (UserInput userInput : inputData.getUsers()) {
            User user = new User();
            user.setUser(userInput);
            user.setAccounts(new ArrayList<>());
            users.add(user);
        }

        // set the commands from the input
        commands = new ArrayList<>(Arrays.asList(inputData.getCommands()));
    }

    public void run(ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();

        Utils.resetRandom();


        for(CommandInput command : commands)  {
            ObjectNode commandNode = objectMapper.createObjectNode();

            if(command.getCommand().equals("addAccount")) {
                AddAccount addAccount = new AddAccount();
                addAccount.addAccount(users, command);
            } else if(command.getCommand().equals("createCard")) {
                CreateCard createCard = new CreateCard();
                createCard.createCard(users, command);
            } else if(command.getCommand().equals("printUsers")) {
                PrintUsers printUsers = new PrintUsers();
                printUsers.printUsers(output, users, objectMapper, commandNode, command);
            } else if(command.getCommand().equals("addFunds")) {
                AddFunds addFunds = new AddFunds();
                addFunds.AddFunds(users, command);
            } else if(command.getCommand().equals("deleteAccount")) {
                DeleteAccount deleteAccount = new DeleteAccount();
                deleteAccount.deleteAccount(users, command, output, objectMapper, commandNode);
            } else if(command.getCommand().equals("createOneTimeCard")) {
                CreateOneTimeCard createOneTimeCard = new CreateOneTimeCard();
                createOneTimeCard.createOneTimeCard(users, command);
            } else if(command.getCommand().equals("deleteCard")) {
                DeleteCard deleteCard = new DeleteCard();
                deleteCard.deleteCard(users, command);
            }
        }
    }
}
