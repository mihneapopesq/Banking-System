package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Card;
import org.poo.utilities.users.User;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class CreateOneTimeCard {
    public void createOneTimeCard(ArrayList<User> users, CommandInput commandInput) {

        for (User user : users) {

            if(user.getUser().getEmail().equals(commandInput.getEmail())) {

                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(commandInput.getAccount())) {

                        Card newCard = new Card();
                        newCard.setCardNumber(Utils.generateCardNumber());

                        newCard.setStatus("active");
                        newCard.setIsOneTimeCard(1);

                        account.getCards().add(account.getCards().size() ,newCard);


                        break;
                    }
                }

                break;
            }
        }
    }
}
