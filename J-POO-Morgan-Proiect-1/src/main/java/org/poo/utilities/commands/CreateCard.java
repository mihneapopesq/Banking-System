package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Card;
import org.poo.utilities.users.User;
import org.poo.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateCard {
    public void createCard(User[] users, CommandInput commandInput) {

        for (User user : users) {
            // caut useru cu email
            if(user.getUser().getEmail().equals(commandInput.getEmail())) {
                Account targetAccount = null;

                // caut contul cu iban
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(commandInput.getAccount())) {
                        targetAccount = account;
                        if (targetAccount.getCards() == null) {
                            targetAccount.setCards(new Card[0]);
                        }


                        Card newCard = new Card();
                        // genereaza aia cu cardnumber
                        newCard.setCardNumber(Utils.generateCardNumber());

                        newCard.setStatus("active");

                        Card[] existingCards = targetAccount.getCards();
                        Card[] updatedCards = Arrays.copyOf(existingCards, existingCards.length + 1);
                        updatedCards[existingCards.length] = newCard;
                        targetAccount.setCards(updatedCards);
                        break;
                    }
                }


// daca n am carduri in contu care trb inseamna ca tre sa bag acu


                break;
            }
        }
    }
}
