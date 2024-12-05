package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.UserInput;
import org.poo.utilities.commands.AddAccount;
import org.poo.utilities.commands.PrintUsers;

import java.util.ArrayList;
import java.util.Arrays;

public class Start {

    private ArrayList<CommandInput> commands;
    private ArrayList<UserInput> users;

    public Start(ObjectInput inputData, ArrayNode output) {
        commands = new ArrayList<>(Arrays.asList(inputData.getCommands()));
        users = new ArrayList<>(Arrays.asList(inputData.getUsers()));
    }

    public void run(ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        for (CommandInput command : commands) {

            ObjectNode commandNode = objectMapper.createObjectNode();

            if(command.getCommand().equals("printUsers")) {
                PrintUsers printUsers = new PrintUsers();
                printUsers.printUsers(output, users, command, objectMapper, commandNode);
            } else if(command.getCommand().equals("addAccount")) {
                AddAccount addAccount = new AddAccount();
                addAccount.addAccount(users, command);
            } else if(command.getCommand().equals("createCard")) {

            } else if(command.getCommand().equals("addFunds")) {

            }

        }
    }
}
