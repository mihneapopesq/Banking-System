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

public class UpgradePlan extends CommandBase{
    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final ArrayNode output;
    private final ObjectMapper objectMapper;
    private final ObjectNode commandNode;
    private final ArrayList<Transaction> transactions;
    private final CurrencyGraph currencyGraph;

    public UpgradePlan(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.output = builder.getOutput();
        this.objectMapper = builder.getObjectMapper();
        this.commandNode = builder.getCommandNode();
        this.transactions = builder.getTransactions();
        this.currencyGraph = builder.getCurrencyGraph();
    }

    @Override
    public void execute() {
        for(User user : users) {
            for(Account account : user.getAccounts()) {
                if(account.getIban().equals(commandInput.getAccount())) {
                    if(commandInput.getNewPlanType().equals("silver")
                        && (user.getUserPlan().equals("standard")
                            || user.getUserPlan().equals("student"))) {
                        double amountInAccountCurrency = currencyGraph.convertCurrency(
                                "RON",
                                account.getCurrency(),
                                100
                        );

                        if(account.getBalance() - account.getMinBalance() >=  amountInAccountCurrency) {
                            account.setBalance(account.getBalance() - amountInAccountCurrency);
                            Transaction transaction = new Transaction(
                                    "Upgrade plan",
                                    commandInput.getTimestamp(),
                                    user.getUser().getEmail(),
                                    commandInput.getNewPlanType(),
                                    account.getIban()
                            );
                            transactions.add(transaction);
                        }
                        account.setAccountPlan(commandInput.getNewPlanType());
                        user.setUserPlan(commandInput.getNewPlanType());
                    } else if(commandInput.getNewPlanType().equals("gold")
                            && user.getUserPlan().equals("silver")) {

                        if(account.getPaymentsOver300() >= 5) {
                            Transaction transaction = new Transaction(
                                    "Upgrade plan",
                                    commandInput.getTimestamp(),
                                    user.getUser().getEmail(),
                                    commandInput.getNewPlanType(),
                                    account.getIban()
                            );
                            transactions.add(transaction);
                            account.setAccountPlan(commandInput.getNewPlanType());
                            user.setUserPlan(commandInput.getNewPlanType());
                            return ;
                        }

                        double amountInAccountCurrency = currencyGraph.convertCurrency(
                                "RON",
                                account.getCurrency(),
                                250
                        );

                        if(account.getBalance() - account.getMinBalance() >= amountInAccountCurrency) {
                            account.setBalance(account.getBalance() - amountInAccountCurrency);
                            Transaction transaction = new Transaction(
                                    "Upgrade plan",
                                    commandInput.getTimestamp(),
                                    user.getUser().getEmail(),
                                    commandInput.getNewPlanType(),
                                    account.getIban()
                            );
                            transactions.add(transaction);
                        }
                        account.setAccountPlan(commandInput.getNewPlanType());
                        user.setUserPlan(commandInput.getNewPlanType());
                    } else if(commandInput.getNewPlanType().equals("gold")
                            && (user.getUserPlan().equals("standard")
                            || user.getUserPlan().equals("student"))){
                        double amountInAccountCurrency = currencyGraph.convertCurrency(
                                "RON",
                                account.getCurrency(),
                                350
                        );
                        if(account.getBalance() - account.getMinBalance() >= amountInAccountCurrency) {
                            account.setBalance(account.getBalance() - amountInAccountCurrency);
                            account.setPaymentsOver300(account.getPaymentsOver300() + 1);
                            Transaction transaction = new Transaction(
                                    "Upgrade plan",
                                    commandInput.getTimestamp(),
                                    user.getUser().getEmail(),
                                    commandInput.getNewPlanType(),
                                    account.getIban()
                            );
                            transactions.add(transaction);
                        }
                        account.setAccountPlan(commandInput.getNewPlanType());
                        user.setUserPlan(commandInput.getNewPlanType());
                    }
                }
            }
        }
    }
}
