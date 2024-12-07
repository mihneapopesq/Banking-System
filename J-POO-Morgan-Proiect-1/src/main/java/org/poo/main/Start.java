package org.poo.main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.utilities.commands.AddAccount;
import org.poo.utilities.commands.AddFunds;
import org.poo.utilities.commands.CreateCard;
import org.poo.utilities.commands.PrintUsers;
import org.poo.utilities.users.Card;
import org.poo.utilities.users.User;
import java.util.Arrays;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.utilities.users.Account;



public class Start {

    private User[] users;
    private ArrayList<CommandInput> commands;

    public Start(ObjectInput inputData, ArrayNode output) {

        // set the users in Users (first name, last name, email)
        users = new User[inputData.getUsers().length];
        for (int i = 0; i < inputData.getUsers().length; i++) {
            UserInput userInput = inputData.getUsers()[i];
            User user = new User();
            user.setUser(userInput);
            user.setAccounts(new Account[0]);
            users[i] = user;

        }
        // set the commands from the input
        commands = new ArrayList<>(Arrays.asList(inputData.getCommands()));


    }

    public void run(ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
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
            }
        }
    }
}
