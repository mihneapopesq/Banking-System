package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Command for generating a spendings report for a specific account.
 */
public class SpendingsReport extends CommandBase {

    private final ArrayNode output;
    private final ArrayList<User> users;
    private final ObjectMapper objectMapper;
    private final ObjectNode commandNode;
    private final CommandInput commandInput;
    private final ArrayList<Transaction> transactions;

    /**
     * Constructs the SpendingsReport command using the provided builder.
     *
     * @param builder the builder containing dependencies and configuration for this command.
     */
    public SpendingsReport(final Builder builder) {
        this.output = builder.getOutput();
        this.users = builder.getUsers();
        this.objectMapper = builder.getObjectMapper();
        this.commandNode = builder.getCommandNode();
        this.commandInput = builder.getCommandInput();
        this.transactions = builder.getTransactions();
    }

    /**
     * Executes the command to generate a spendings report.
     */
    @Override
    public void execute() {
        commandNode.put("command", commandInput.getCommand());
        commandNode.put("timestamp", commandInput.getTimestamp());

        Account foundAccount = null;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(commandInput.getAccount())) {
                    foundAccount = account;

                    if (foundAccount.getAccountType().equals("savings")) {
                        ObjectNode outputNode = objectMapper.createObjectNode();
                        outputNode.put("error",
                                "This kind of report is not supported for a saving account");
                        commandNode.set("output", outputNode);
                        output.add(commandNode);
                        return;
                    }

                    break;
                }
            }
            if (foundAccount != null) {
                break;
            }
        }

        if (foundAccount == null) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("description", "Account not found");
            errorNode.put("timestamp", commandInput.getTimestamp());
            commandNode.set("output", errorNode);
            output.add(commandNode);
            return;
        }

        ObjectNode reportNode = objectMapper.createObjectNode();
        reportNode.put("balance", foundAccount.getBalance());
        reportNode.put("currency", foundAccount.getCurrency());
        reportNode.put("IBAN", foundAccount.getIban());

        ArrayList<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getTimestamp() >= commandInput.getStartTimestamp()
                    && transaction.getTimestamp() <= commandInput.getEndTimestamp()) {
                if (transaction.getReportIban() != null
                        && transaction.getReportIban().equals(foundAccount.getIban())) {
                    accountTransactions.add(transaction);
                }
            }
        }

        Map<String, Double> commerciantTotals = new HashMap<>();
        for (Transaction t : accountTransactions) {
            if (t.getCommerciant() != null && t.getAmountSpent() > 0) {
                commerciantTotals.put(t.getCommerciant(),
                        commerciantTotals.getOrDefault(t.getCommerciant(), 0.0)
                                + t.getAmountSpent());
            }
        }

        ArrayList<String> sortedCommerciants = new ArrayList<>(commerciantTotals.keySet());
        sortedCommerciants.sort(String::compareTo);

        ArrayNode commerciantsArray = objectMapper.createArrayNode();
        for (String commerciantName : sortedCommerciants) {
            ObjectNode cNode = objectMapper.createObjectNode();
            cNode.put("commerciant", commerciantName);
            cNode.put("total", commerciantTotals.get(commerciantName));
            commerciantsArray.add(cNode);
        }

        reportNode.set("commerciants", commerciantsArray);

        ArrayNode transactionsArray = objectMapper.createArrayNode();
        for (Transaction transaction : accountTransactions) {
            ObjectNode transactionNode = objectMapper.createObjectNode();

            if (transaction.getCommerciant() == null) {
                continue;
            }

            if (transaction.getAmountSpent() > 0) {
                transactionNode.put("amount", transaction.getAmountSpent());
            }
            if (transaction.getCommerciant() != null) {
                transactionNode.put("commerciant", transaction.getCommerciant());
            }
            if (transaction.getDescription() != null) {
                transactionNode.put("description", transaction.getDescription());
            }
            transactionNode.put("timestamp", transaction.getTimestamp());

            transactionsArray.add(transactionNode);
        }

        reportNode.set("transactions", transactionsArray);

        commandNode.set("output", reportNode);
        output.add(commandNode);
    }
}
