package org.poo.main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.utilities.commands.CommandFactory;
import org.poo.utilities.users.CurrencyGraph;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.Arrays;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.utils.Utils;

/**
 * The Start class is the entry point of the banking system.
 */
public class Start {

    private final ArrayList<User> users;
    private final ArrayList<CommandInput> commands;
    private final CurrencyGraph currencyGraph;
    private final ArrayList<Transaction> transactions;

    /**
     * Constructor to initialize the Start class with input data.
     * This will create users, initialize the currency graph, and store the commands.
     *
     * @param inputData The input data for initializing the system, including users,
     *                  exchange rates, and commands.
     */
    public Start(final ObjectInput inputData) {
        users = new ArrayList<>();

        for (UserInput userInput : inputData.getUsers()) {
            User user = new User();
            user.setUser(userInput);
            user.setAccounts(new ArrayList<>());
            users.add(user);
        }

        ArrayList<ExchangeInput> exchangeData = new ArrayList<>();
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

    public void run(final ArrayNode output) {
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
