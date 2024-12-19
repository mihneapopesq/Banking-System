package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;

/**
 * Command for generating a report for a specific account.
 * Includes account details and transactions within a specified time range.
 */
public class Report extends CommandBase {

    private final ArrayNode output;
    private final ArrayList<User> users;
    private final ObjectMapper objectMapper;
    private final ObjectNode commandNode;
    private final CommandInput commandInput;
    private final ArrayList<Transaction> transactions;

    /**
     * Constructs the Report command using the provided builder.
     *
     * @param builder the builder containing dependencies and configuration for this command.
     */
    public Report(final Builder builder) {
        this.output = builder.getOutput();
        this.users = builder.getUsers();
        this.objectMapper = builder.getObjectMapper();
        this.commandNode = builder.getCommandNode();
        this.commandInput = builder.getCommandInput();
        this.transactions = builder.getTransactions();
    }

    /**
     * Executes the command to generate a report.
     */
    @Override
    public void execute() {
        commandNode.put("command", commandInput.getCommand());
        commandNode.put("timestamp", commandInput.getTimestamp());

        Account foundAccount = findAccount();
        if (foundAccount == null) {
            addErrorToOutput("Account not found");
            return;
        }

        ObjectNode reportNode = createReportNode(foundAccount);

        ArrayList<Transaction> accountTransactions = filterTransactionsByTimestamp();
        ArrayNode transactionsArray = objectMapper.createArrayNode();

        for (Transaction transaction : accountTransactions) {
            if (transaction.getReportIban().equals(foundAccount.getIban())) {
                ObjectNode transactionNode = objectMapper.createObjectNode();
                transaction.populateTransactionNode(transaction, transactionNode, objectMapper);
                transactionsArray.add(transactionNode);
            }
        }


        reportNode.set("transactions", transactionsArray);
        commandNode.set("output", reportNode);
        output.add(commandNode);
    }

    private Account findAccount() {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(commandInput.getAccount())) {
                    return account;
                }
            }
        }
        return null;
    }

    private void addErrorToOutput(final String errorMessage) {
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("description", errorMessage);
        errorNode.put("timestamp", commandInput.getTimestamp());
        commandNode.set("output", errorNode);
        output.add(commandNode);
    }

    private ObjectNode createReportNode(final Account foundAccount) {
        ObjectNode reportNode = objectMapper.createObjectNode();
        reportNode.put("balance", foundAccount.getBalance());
        reportNode.put("currency", foundAccount.getCurrency());
        reportNode.put("IBAN", foundAccount.getIban());
        return reportNode;
    }

    private ArrayList<Transaction> filterTransactionsByTimestamp() {
        ArrayList<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getTimestamp() >= commandInput.getStartTimestamp()
                    && transaction.getTimestamp() <= commandInput.getEndTimestamp()) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }
}
