package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.UserInput;
import org.poo.fileio.CommandInput;
import java.util.ArrayList;

public class PrintUsers {
    public void printUsers(final ArrayNode output, final ArrayList<UserInput> users,
                           final CommandInput command, final ObjectMapper objectMapper,
                           final ObjectNode commandNode) {
        commandNode.put("command", command.getCommand());
//        commandNode.put("timestamp", command.getTimestamp());

        ArrayNode usersOutput = objectMapper.createArrayNode();

        for (UserInput user : users) {
            ObjectNode userNode = objectMapper.createObjectNode();
            userNode.put("firstName", user.getFirstName());
            userNode.put("lastName", user.getLastName());
            userNode.put("email", user.getEmail());

            usersOutput.add(userNode);
        }

        commandNode.set("output", usersOutput);

        output.add(commandNode);
    }

}
