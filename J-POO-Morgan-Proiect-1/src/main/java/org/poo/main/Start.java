package org.poo.main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.utilities.commands.*;
import org.poo.utilities.users.*;

import java.util.Arrays;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.utils.Utils;


public class Start {

    private ArrayList<User> users;
    private ArrayList<CommandInput> commands;
    private ArrayList<ExchangeInput> exchangeData;
    private CurrencyGraph currencyGraph;
    private ArrayList<Transaction> transactions;

    public Start(ObjectInput inputData) {
        users = new ArrayList<>();

        for (UserInput userInput : inputData.getUsers()) {
            User user = new User();
            user.setUser(userInput);
            user.setAccounts(new ArrayList<>());
            users.add(user);
        }

        exchangeData = new ArrayList<>();

        transactions = new ArrayList<>();

        for (ExchangeInput exchangeInput : inputData.getExchangeRates()) {

            ExchangeInput exchange = new ExchangeInput();
            exchange.setFrom(exchangeInput.getFrom());
            exchange.setTo(exchangeInput.getTo());
            exchange.setRate(exchangeInput.getRate());
            exchange.setTimestamp(exchangeInput.getTimestamp());

            exchangeData.add(exchange);
        }
        currencyGraph = new CurrencyGraph();
        currencyGraph.buildCurrencyGraph(exchangeData);

        commands = new ArrayList<>(Arrays.asList(inputData.getCommands()));
    }

    public void run(ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        Utils.resetRandom();

        for (CommandInput command : commands) {
            ObjectNode commandNode = objectMapper.createObjectNode();

            CommandFactory factory = new CommandFactory(
                    command.getCommand(),
                    users,
                    commandNode,
                    output,
                    command,
                    objectMapper,
                    currencyGraph,
                    transactions
            );

            factory.execute();
        }
    }
}
