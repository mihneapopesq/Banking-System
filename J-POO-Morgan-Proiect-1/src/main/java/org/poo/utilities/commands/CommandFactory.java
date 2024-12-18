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
                new AddAccount(
                        new Builder(users, command, transactions)
                ).execute();
                break;
            case "createCard":
                CommandBase createCardCommand = new CreateCard(
                        new Builder(users, command, transactions, 0)
                );
                createCardCommand.execute();break;
            case "printUsers":
                CommandBase printUsersCommand = new PrintUsers(
                        new Builder(output, users, objectMapper, commandNode, command)
                );
                printUsersCommand.execute();
                break;
            case "addFunds":
                CommandBase addFundsCommand = new AddFunds(new Builder(users, command));
                addFundsCommand.execute();
                break;
            case "deleteAccount":
                CommandBase deleteAccountCommand = new DeleteAccount(
                        new Builder(output, users, objectMapper, commandNode, command, transactions)
                );
                deleteAccountCommand.execute();
                break;
            case "createOneTimeCard":
                CommandBase createCard = new CreateCard(
                        new Builder(users, command, transactions, 1)
                );
                createCard.execute();break;
            case "deleteCard":
                CommandBase deleteCardCommand = new DeleteCard(
                        new Builder(users, command, transactions)
                );
                deleteCardCommand.execute();
                break;
            case "setMinimumBalance":
                CommandBase setMinimumBalanceCommand = new SetMinimumBalance(
                        new Builder(users, command)
                );
                setMinimumBalanceCommand.execute();
                break;
            case "payOnline":
                CommandBase payOnlineCommand = new PayOnline(
                        new Builder(users, command, transactions, currencyGraph, objectMapper, commandNode, output)
                );
                payOnlineCommand.execute();
                break;
            case "sendMoney":
                CommandBase sendMoneyCommand = new SendMoney(
                        new Builder(users, command, transactions, currencyGraph)
                );
                sendMoneyCommand.execute();
                break;
            case "setAlias":
                CommandBase setAliasCommand = new SetAlias(
                        new Builder(users, command)
                );
                setAliasCommand.execute();
                break;
            case "printTransactions":
                CommandBase printTransactionsCommand = new PrintTransactions(
                        new Builder(output, objectMapper, commandNode, command, transactions)
                );
                printTransactionsCommand.execute();
                break;
            case "checkCardStatus":
                CommandBase checkCardStatusCommand = new CheckCardStatus(
                        new Builder(output, users, objectMapper, commandNode, command, transactions)
                );
                checkCardStatusCommand.execute();
                break;
            case "changeInterestRate":
                CommandBase changeInterestRateCommand = new ChangeInterestRate(
                        new Builder(output, users, objectMapper, commandNode, command, transactions)
                );
                changeInterestRateCommand.execute();
                break;
            case "splitPayment":
                CommandBase splitPaymentCommand = new SplitPayment(
                        new Builder(users, command, transactions, currencyGraph)
                );
                splitPaymentCommand.execute();
                break;
            case "report":
                CommandBase reportCommand = new Report(
                        new Builder(output, users, objectMapper, commandNode, command, transactions)
                );
                reportCommand.execute();
                break;
            case "spendingsReport":
                CommandBase spendingsReportCommand = new SpendingsReport(
                        new Builder(output, users, objectMapper, commandNode, command, transactions)
                );
                spendingsReportCommand.execute();
                break;
            case "addInterest":
                CommandBase addInterestCommand = new AddInterest(
                        new Builder(output, users, objectMapper, commandNode, command)
                );
                addInterestCommand.execute();
                break;
            default:
                throw new IllegalArgumentException("Unknown command type: " + commandType);
        }
    }
}
