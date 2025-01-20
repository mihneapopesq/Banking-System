package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.BusinessAccount;
import org.poo.utilities.users.CurrencyGraph;
import org.poo.utilities.users.User;

import java.util.ArrayList;

/**
 * Command for adding funds to a specified account.
 */
public class AddFunds extends CommandBase {
    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final ArrayList<BusinessAccount> businessAccounts;
    private final CurrencyGraph currencyGraph;

    /**
     * Constructs the AddFunds command using the provided builder.
     *
     * @param builder the builder containing the dependencies and configuration for this command.
     */
    public AddFunds(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.businessAccounts = builder.getBusinessAccounts();
        this.currencyGraph = builder.getCurrencyGraph();
    }

    /**
     * Executes the command to add funds to a specified account.
     * Updates the account's balance by adding the specified amount.
     */
    @Override
    public void execute() {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(commandInput.getAccount())) {
                    account.setBalance(account.getBalance() + commandInput.getAmount());
                    return;
                }
            }
        }

        for (BusinessAccount account : businessAccounts) {
            if (account.getIban().equals(commandInput.getAccount())) {
                for (User user : account.getEmployees()) {
                    if (user.getUser().getEmail().equals(commandInput.getEmail())) {

                        double rightAmount =
                            currencyGraph.convertCurrency(account.getCurrency(),"RON", commandInput.getAmount());
                        if (rightAmount >= 500) {
                            System.out.println("Employees cannot deposit more than 500 RON.");
                            return;
                        }
                    }
                }

//                if(account.getDepositLimit() < commandInput.getAmount()) {
//                    System.out.println("The deposit limit has been exceeded.");
//                    return;
//                }

                account.setBalance(account.getBalance() + commandInput.getAmount());
                for (User user : users) {
                    if (user.getUser().getEmail().equals(commandInput.getEmail())) {
                        user.setDeposited(user.getDeposited() + commandInput.getAmount());
                    }
                }
                return;
            }
        }

    }
}
