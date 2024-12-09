package org.poo.main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.utilities.commands.*;
import org.poo.utilities.users.Card;
import org.poo.utilities.users.User;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.utilities.users.Account;
import org.poo.utils.Utils;


public class Start {

    private ArrayList<User> users;
    private ArrayList<CommandInput> commands;
    private ArrayList<ExchangeInput> exchangeData;
    private Map<String, Map<String, Double>> currencyGraph;

    public Start(ObjectInput inputData, ArrayNode output) {
        users = new ArrayList<>();

        for (UserInput userInput : inputData.getUsers()) {
            User user = new User();
            user.setUser(userInput);
            user.setAccounts(new ArrayList<>());
            users.add(user);
        }

         exchangeData = new ArrayList<>();

        for (ExchangeInput exchangeInput : inputData.getExchangeRates()) {

            ExchangeInput exchange = new ExchangeInput();
            exchange.setFrom(exchangeInput.getFrom());
            exchange.setTo(exchangeInput.getTo());
            exchange.setRate(exchangeInput.getRate());
            exchange.setTimestamp(exchangeInput.getTimestamp());

            exchangeData.add(exchange);
        }

        buildCurrencyGraph();

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
            }  else if(command.getCommand().equals("setMinimumBalance")) {
                SetMinimumBalance setMinimumBalance = new SetMinimumBalance();
                setMinimumBalance.setMinimumBalance(users, command);
            } else if(command.getCommand().equals("payOnline")) {
                PayOnline payOnline = new PayOnline();
                payOnline.payOnline(users, command, currencyGraph, commandNode, objectMapper, output);
            }
        }
    }

    public void buildCurrencyGraph() {
        currencyGraph = new HashMap<>();

        for (ExchangeInput exchange : exchangeData) {
            String from = exchange.getFrom();
            String to = exchange.getTo();
            double rate = exchange.getRate();

            currencyGraph.putIfAbsent(from, new HashMap<>());
            currencyGraph.get(from).put(to, rate);

            currencyGraph.putIfAbsent(to, new HashMap<>());
            currencyGraph.get(to).put(from, 1 / rate);
        }
    }


}
