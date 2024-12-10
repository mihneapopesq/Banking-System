package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Card;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;
import org.poo.utils.Utils;
import java.util.ArrayList;

public class CreateOneTimeCard {
    public void createOneTimeCard(ArrayList<User> users, CommandInput commandInput,
                                  ArrayList<Transaction> transactions) {

        for (User user : users) {

            if(user.getUser().getEmail().equals(commandInput.getEmail())) {

                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(commandInput.getAccount())) {

                        Card newCard = new Card();
                        newCard.setCardNumber(Utils.generateCardNumber());

                        newCard.setStatus("active");
                        newCard.setIsOneTimeCard(1);

                        Transaction transaction = new Transaction();
                        transaction.setDescription("New card created");
                        transaction.setTimestamp(commandInput.getTimestamp());
                        transaction.setIban(account.getIban());
                        transaction.setCardNumber(newCard.getCardNumber());
                        transaction.setCardHolder(user.getUser().getEmail());
                        transaction.setEmail(user.getUser().getEmail());
                        transaction.setReportIban(account.getIban());
                        transactions.add(transaction);

                        account.getCards().add(account.getCards().size() ,newCard);

                        break;
                    }
                }
                break;
            }
        }
    }
}
