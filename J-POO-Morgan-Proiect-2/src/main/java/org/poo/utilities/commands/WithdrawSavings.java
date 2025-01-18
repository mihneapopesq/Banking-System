package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.CurrencyGraph;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;

public class WithdrawSavings extends CommandBase{
    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final CurrencyGraph currencyGraph;
    private final ObjectNode commandNode;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;
    private final ArrayList<Transaction> transactions;

    public WithdrawSavings(final Builder builder) {
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
        for(User user : users) {
            for(Account account : user.getAccounts()) {
                // daca contul e de tip savings si ibanu e ok


                // todo verifica daca exista un cont classic in care sa adaugi banii

                if(account.getIban().equals(commandInput.getAccount())
                        && account.getAccountType().equals("savings")
                        && getClassicAccount(user.getAccounts()) == 1) {
                    // daca are >= 21 de ani
                    if(user.checkOldEnough() == 1) {
                        double amount = commandInput.getAmount();
                        double righAmount = currencyGraph.convertCurrency(commandInput.getCurrency(), account.getCurrency(), amount);
                        account.setBalance(account.getBalance() - righAmount);
                        // mai sus scad din saving account
                        // mai jos caut classic account si adaug in el
                        for(Account account1 : user.getAccounts()) {
                            if(account1.getAccountType().equals("classic")) {
                                account1.setBalance(account1.getBalance() + righAmount);
                                break;
                            }
                        }
                    } else { // “You don't have the minimum age required.”
                        Transaction transaction = new Transaction(
                                "You don't have the minimum age required.",
                                commandInput.getTimestamp(),
                                user.getUser().getEmail()
                        );
                        transactions.add(transaction);
                    }
                    return ;
                } else if (account.getIban().equals(commandInput.getAccount())
                        && account.getAccountType().equals("savings")
                        && getClassicAccount(user.getAccounts()) == 0) {
                    Transaction transaction = new Transaction(
                            "You do not have a classic account.",
                            commandInput.getTimestamp(),
                            user.getUser().getEmail()
                    );
                    transactions.add(transaction);
                }
            }
        }
    }

    public int getClassicAccount(final ArrayList<Account> accounts) {
        for (Account account : accounts) {
            if (account.getAccountType().equals("classic")) {
                return 1;
            }
        }
        return 0;
    }

}
