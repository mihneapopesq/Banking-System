package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.*;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class PayOnline extends CommandBase {

    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final CurrencyGraph currencyGraph;
    private final ObjectNode commandNode;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;
    private final ArrayList<Transaction> transactions;

    public PayOnline(Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.currencyGraph = builder.getCurrencyGraph();
        this.commandNode = builder.getCommandNode();
        this.objectMapper = builder.getObjectMapper();
        this.output = builder.getOutput();
        this.transactions = builder.getTransactions();
    }

    @Override
    public void execute() {
        String targetEmail = commandInput.getEmail();
        String targetCardNumber = commandInput.getCardNumber();
        String paymentCurrency = commandInput.getCurrency();
        double paymentAmount = commandInput.getAmount();

        for (User user : users) {
            if (user.getUser().getEmail().equals(targetEmail)) {
                for (Account account : user.getAccounts()) {
                    for (Card card : account.getCards()) {
                        if (card.getCardNumber().equals(targetCardNumber)) {

                            if (card.getIsFrozen() == 1) {
                                addTransaction("The card is frozen", user, account);
                                return;
                            }

                            double amountInAccountCurrency = currencyGraph.convertCurrency(
                                    paymentCurrency, account.getCurrency(), paymentAmount);

                            if (account.getMinBalance() > account.getBalance() - amountInAccountCurrency) {
                                addTransaction("Insufficient funds", user, account);
                                return;
                            }

                            account.setBalance(account.getBalance() - amountInAccountCurrency);

                            transactions.add(new Transaction(
                                    "Card payment",
                                    commandInput.getTimestamp(),
                                    amountInAccountCurrency,
                                    commandInput.getCommerciant(),
                                    user.getUser().getEmail(),
                                    account.getIban()
                            ));

                            if (card.getIsOneTimeCard() == 1) {
                                handleOneTimeCard(user, account, card);
                            }
                            return;
                        }
                    }
                }
            }
        }

        addCardNotFoundError();
    }

    private void addTransaction(String description, User user, Account account) {
        transactions.add(new Transaction(
                description,
                commandInput.getTimestamp(),
                user.getUser().getEmail(),
                account.getIban()
        ));
    }

    private void handleOneTimeCard(User user, Account account, Card card) {
        CommandBase deleteCardCommand = new DeleteCard(
                new Builder(users, commandInput, transactions)
        );
        deleteCardCommand.execute();

        Card newCard = new Card();
        newCard.setCardNumber(Utils.generateCardNumber());
        newCard.setStatus("active");
        newCard.setIsOneTimeCard(1);

        transactions.add(new Transaction(
                "New card created",
                commandInput.getTimestamp(),
                account.getIban(),
                newCard.getCardNumber(),
                user.getUser().getEmail(),
                user.getUser().getEmail(),
                account.getIban()
        ));

        account.getCards().add(newCard);
    }

    private void addCardNotFoundError() {
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("timestamp", commandInput.getTimestamp());
        errorNode.put("description", "Card not found");
        commandNode.set("output", errorNode);
        commandNode.put("command", "payOnline");
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }

}
