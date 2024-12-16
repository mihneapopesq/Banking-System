package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Card;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;

public class DeleteCard {
    public void deleteCard(final ArrayList<User> users, final CommandInput commandInput,
                           final ArrayList<Transaction> transactions) {
        for (User user : users) {
            if (user.getUser().getEmail().equals(commandInput.getEmail())) {
                for (Account account : user.getAccounts()) {
                    for (int i = account.getCards().size() - 1; i >= 0; i--) {
                        Card card = account.getCards().get(i);
                        if (card.getCardNumber().equals(commandInput.getCardNumber())) {
                            account.getCards().remove(i);

                            Transaction transaction = new Transaction(
                                    "The card has been destroyed",
                                    commandInput.getTimestamp(),
                                    account.getIban(),
                                    card.getCardNumber(),
                                    user.getUser().getEmail(),
                                    user.getUser().getEmail(),
                                    account.getIban()
                            );

                            transactions.add(transaction);

                            return;
                        }
                    }
                }
            }
        }
    }
}
