package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.CurrencyGraph;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;
import java.util.ArrayList;

public class CommandFactory implements CommandExecutable {
    private String commandType;
    private ArrayList<User> users;
    private ObjectNode commandNode;
    private ArrayNode output;
    private CommandInput command;
    private ObjectMapper objectMapper;
    private CurrencyGraph currencyGraph;
    private ArrayList<Transaction> transactions;

    public CommandFactory(String commandType, ArrayList<User> users, ObjectNode commandNode, ArrayNode output,
                          CommandInput command, ObjectMapper objectMapper, CurrencyGraph currencyGraph,
                          ArrayList<Transaction> transactions) {
        this.commandType = commandType;
        this.users = users;
        this.commandNode = commandNode;
        this.output = output;
        this.command = command;
        this.objectMapper = objectMapper;
        this.currencyGraph = currencyGraph;
        this.transactions = transactions;
    }

    @Override
    public void execute() {
        switch (commandType) {
            case "addAccount":
                new AddAccount().addAccount(users, command, transactions);
                break;
            case "createCard":
                new CreateCard().createCard(users, command, transactions);
                break;
            case "printUsers":
                new PrintUsers().printUsers(output, users, objectMapper, commandNode, command);
                break;
            case "addFunds":
                new AddFunds().AddFunds(users, command);
                break;
            case "deleteAccount":
                new DeleteAccount().deleteAccount(users, command, output, objectMapper, commandNode, transactions);
                break;
            case "createOneTimeCard":
                new CreateOneTimeCard().createOneTimeCard(users, command, transactions);
                break;
            case "deleteCard":
                new DeleteCard().deleteCard(users, command, transactions);
                break;
            case "setMinimumBalance":
                new SetMinimumBalance().setMinimumBalance(users, command);
                break;
            case "payOnline":
                new PayOnline().payOnline(users, command, currencyGraph, commandNode, objectMapper, output, transactions);
                break;
            case "sendMoney":
                new SendMoney().sendMoney(users, command, currencyGraph, transactions);
                break;
            case "setAlias":
                new SetAlias().setAlias(users, command);
                break;
            case "printTransactions":
                new PrintTransactions().printTransactions(output, users, objectMapper, commandNode, command, transactions);
                break;
            case "checkCardStatus":
                new CheckCardStatus().checkCardStatus(users, command, output, commandNode, objectMapper, transactions);
                break;
            case "changeInterestRate":
                new ChangeInterestRate().changeInterestRate(output, users, objectMapper, commandNode, command, transactions);
                break;
            case "splitPayment":
                new SplitPayment().splitPayment(users, command, currencyGraph, transactions);
                break;
            case "report":
                new Report().report(output, users, objectMapper, commandNode, command, transactions);
                break;
            case "spendingsReport":
                new SpendingsReport().spendingsReport(output, users, objectMapper, commandNode, command, transactions);
                break;
            case "addInterest":
                new AddInterest().addInterest(output, users, objectMapper, commandNode, command);
                break;
            default:
                throw new IllegalArgumentException("Unknown command type: " + commandType);
        }
    }
}
