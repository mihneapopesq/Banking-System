package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Card;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;
import org.poo.utils.Utils;
import java.util.ArrayList;

public class CreateCard {
    public void createCard(ArrayList<User> users, CommandInput commandInput,
                           ArrayList<Transaction> transactions) {

        for (User user : users) {
            if(user.getUser().getEmail().equals(commandInput.getEmail())) {

                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(commandInput.getAccount())) {

                        Card newCard = new Card();
                        newCard.setCardNumber(Utils.generateCardNumber());

                        newCard.setStatus("active");
                        newCard.setIsOneTimeCard(0);
                        newCard.setIsFrozen(0);
                        Transaction transaction = new Transaction(
                                "New card created",
                                commandInput.getTimestamp(),
                                account.getIban(),
                                newCard.getCardNumber(),
                                user.getUser().getEmail(),
                                user.getUser().getEmail(),
                                account.getIban()
                        );

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
