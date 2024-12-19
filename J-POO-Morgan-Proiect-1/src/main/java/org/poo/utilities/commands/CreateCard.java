package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Card;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;
import org.poo.utils.Utils;

import java.util.ArrayList;

/**
 * Command for creating a new card for a user.
 * Some cards may be one-time-use cards.
 */
public class CreateCard extends CommandBase {

    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final ArrayList<Transaction> transactions;
    private final int isOneTimeCard;

    /**
     * Constructs the CreateCard command using the provided builder.
     *
     * @param builder the builder containing the dependencies and configuration for this command.
     */
    public CreateCard(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.transactions = builder.getTransactions();
        this.isOneTimeCard = builder.getIsOneTimeCard();
    }

    /**
     * Executes the command to create a new card for the user.
     * Adds the card to the specified account and records the transaction.
     */
    @Override
    public void execute() {
        for (User user : users) {
            if (user.getUser().getEmail().equals(commandInput.getEmail())) {

                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(commandInput.getAccount())) {

                        Card newCard = new Card();
                        newCard.setCardNumber(Utils.generateCardNumber());
                        newCard.setStatus("active");
                        newCard.setIsOneTimeCard(isOneTimeCard);
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
                        account.getCards().add(account.getCards().size(), newCard);
                        break;
                    }
                }
                break;
            }
        }
    }
}
