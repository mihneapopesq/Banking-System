package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.*;
import org.poo.utils.Utils;

import java.util.*;

public class PayOnline {
    public void payOnline(ArrayList<User> users, CommandInput commandInput,
                          CurrencyGraph currencyGraph,
                          ObjectNode commandNode, ObjectMapper objectMapper, ArrayNode output,
                          ArrayList<Transaction> transactions) {
        String targetEmail = commandInput.getEmail();
        String targetCardNumber = commandInput.getCardNumber();
        String paymentCurrency = commandInput.getCurrency();
        double paymentAmount = commandInput.getAmount();

        for (User user : users) {
            if (user.getUser().getEmail().equals(targetEmail)) {
                for (Account account : user.getAccounts()) {
                    for (Card card : account.getCards()) {
                        if (card.getCardNumber().equals(targetCardNumber)) {


                            if(card.getIsFrozen() == 1) {
                                Transaction transaction = new Transaction();
                                transaction.setTimestamp(commandInput.getTimestamp());
                                transaction.setDescription("The card is frozen");
                                transaction.setEmail(user.getUser().getEmail());
                                transaction.setReportIban(account.getIban());
                                transactions.add(transaction);

                                return ;
                            }


                            double amountInAccountCurrency = currencyGraph.convertCurrency(
                                    paymentCurrency, account.getCurrency(), paymentAmount);


                            if(account.getMinBalance() > account.getBalance() - amountInAccountCurrency) {

                                Transaction transaction = new Transaction();
                                transaction.setTimestamp(commandInput.getTimestamp());
                                transaction.setDescription("Insufficient funds");
                                transaction.setEmail(user.getUser().getEmail());
                                transaction.setReportIban(account.getIban());
                                transactions.add(transaction);

                                return ;
                            }


                            account.setBalance(account.getBalance() - amountInAccountCurrency);

                            Transaction transaction = new Transaction();
                            transaction.setAmountSpent(amountInAccountCurrency);
                            transaction.setTimestamp(commandInput.getTimestamp());
                            transaction.setDescription("Card payment");
                            transaction.setCommerciant(commandInput.getCommerciant());
                            transaction.setEmail(user.getUser().getEmail());
                            transaction.setReportIban(account.getIban());

                            transactions.add(transaction);

                            if(card.getIsOneTimeCard() == 1) {

                                DeleteCard deleteCard = new DeleteCard();
                                deleteCard.deleteCard(users, commandInput, transactions);

                                Card newCard = new Card();
                                newCard.setCardNumber(Utils.generateCardNumber());

                                newCard.setStatus("active");
                                newCard.setIsOneTimeCard(1);

                                Transaction transaction1 = new Transaction();
                                transaction1.setDescription("New card created");
                                transaction1.setTimestamp(commandInput.getTimestamp());
                                transaction1.setIban(account.getIban());
                                transaction1.setCardNumber(newCard.getCardNumber());
                                transaction1.setCardHolder(user.getUser().getEmail());
                                transaction1.setEmail(user.getUser().getEmail());
                                transaction1.setReportIban(account.getIban());
                                transactions.add(transaction1);

                                account.getCards().add(account.getCards().size(), newCard);
                            }
                            return;
                        }
                    }
                }
            }
        }

        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("timestamp", commandInput.getTimestamp());
        errorNode.put("description", "Card not found");
        commandNode.set("output", errorNode);
        commandNode.put("command", "payOnline");
        commandNode.put("timestamp", commandInput.getTimestamp());

        output.add(commandNode);
    }

}
