package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Commerciant;
import org.poo.utilities.users.CurrencyGraph;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;

/**
 * Factory class for creating and executing different commands based on the command type.
 * The factory determines which command to execute.
 */
public class CommandFactory extends CommandBase {
    private final String commandType;
    private final ArrayList<User> users;
    private final ObjectNode commandNode;
    private final ArrayNode output;
    private final CommandInput command;
    private final ObjectMapper objectMapper;
    private final CurrencyGraph currencyGraph;
    private final ArrayList<Transaction> transactions;
    private final ArrayList<Commerciant> commerciants;

    /**
     * Constructs the CommandFactory with the necessary dependencies.
     *
     * @param commandType    the type of the command to execute.
     * @param users          the list of users in the system.
     * @param commandNode    the JSON node to store the command details.
     * @param output         the output array where the command results are added.
     * @param command        the input details of the command.
     * @param objectMapper   the object mapper.
     * @param currencyGraph  the graph for currency conversions.
     * @param transactions   the list of transactions in the system.
     */
    public CommandFactory(final String commandType, final ArrayList<User> users,
                          final ObjectNode commandNode, final ArrayNode output,
                          final CommandInput command, final ObjectMapper objectMapper,
                          final CurrencyGraph currencyGraph,
                          final ArrayList<Transaction> transactions,
                          final ArrayList<Commerciant> commerciants) {
        this.commandType = commandType;
        this.users = users;
        this.commandNode = commandNode;
        this.output = output;
        this.command = command;
        this.objectMapper = objectMapper;
        this.currencyGraph = currencyGraph;
        this.transactions = transactions;
        this.commerciants = commerciants;
    }

    /**
     * Executes the appropriate command based on the command type.
     * Each case in the switch statement executes the corresponding command.
     *
     * @throws IllegalArgumentException if the command type is unknown or unsupported.
     */
    @Override
    public void execute() {
        switch (commandType) {
            case "addAccount":
                new AddAccount(
                        new Builder(users, command, transactions)
                ).execute();
                break;
            case "createCard":
                new CreateCard(
                        new Builder(users, command, transactions, 0)
                ).execute();
                break;
            case "printUsers":
                new PrintUsers(
                        new Builder(output, users, objectMapper, commandNode, command)
                ).execute();
                break;
            case "addFunds":
                new AddFunds(new Builder(users, command)).execute();
                break;
            case "deleteAccount":
                new DeleteAccount(
                        new Builder(output, users, objectMapper, commandNode,
                                command, transactions)
                ).execute();
                break;
            case "createOneTimeCard":
                new CreateCard(
                        new Builder(users, command, transactions, 1)
                ).execute();
                break;
            case "deleteCard":
                new DeleteCard(
                        new Builder(users, command, transactions)
                ).execute();
                break;
            case "setMinimumBalance":
                new SetMinimumBalance(
                        new Builder(users, command)
                ).execute();
                break;
            case "payOnline":
                new PayOnline(
                        new Builder(users, command, transactions, currencyGraph,
                                objectMapper, commandNode, output, commerciants)
                ).execute();
                break;
            case "sendMoney":
                new SendMoney(
                        new Builder(users, command, transactions, currencyGraph, objectMapper, commandNode, output)
                ).execute();
                break;
            case "setAlias":
                new SetAlias(
                        new Builder(users, command)
                ).execute();
                break;
            case "printTransactions":
                new PrintTransactions(
                        new Builder(output, objectMapper, commandNode, command, transactions)
                ).execute();
                break;
            case "checkCardStatus":
                new CheckCardStatus(
                        new Builder(output, users, objectMapper, commandNode,
                                command, transactions)
                ).execute();
                break;
            case "changeInterestRate":
                new ChangeInterestRate(
                        new Builder(output, users, objectMapper, commandNode,
                                command, transactions)
                ).execute();
                break;
            case "splitPayment":
                new SplitPayment(
                        new Builder(users, command, transactions, currencyGraph)
                ).execute();
                break;
            case "report":
                new Report(
                        new Builder(output, users, objectMapper, commandNode,
                                command, transactions)
                ).execute();
                break;
            case "spendingsReport":
                new SpendingsReport(
                        new Builder(output, users, objectMapper, commandNode,
                                command, transactions)
                ).execute();
                break;
            case "addInterest":
                new AddInterest(
                        new Builder(output, users, objectMapper, commandNode, command, transactions)
                ).execute();
                break;
            case "withdrawSavings":
                new WithdrawSavings(
                        new Builder(users, command, transactions, currencyGraph)
                ).execute();
                break;
            case "upgradePlan":
                new UpgradePlan(
                        new Builder(users, command, transactions, currencyGraph, objectMapper)
                ).execute();
                break;
            case "cashWithdrawal":
                new CashWithdrawal(
                        new Builder(users, command, transactions, currencyGraph,
                                objectMapper, commandNode, output)
                ).execute();
                break;
            default:
//                throw new IllegalArgumentException("Unknown command type: " + commandType);
        }
    }
}
