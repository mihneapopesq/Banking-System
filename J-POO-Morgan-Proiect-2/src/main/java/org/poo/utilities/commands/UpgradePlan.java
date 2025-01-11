package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
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

    public UpgradePlan(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.output = builder.getOutput();
        this.objectMapper = builder.getObjectMapper();
        this.commandNode = builder.getCommandNode();
        this.transactions = builder.getTransactions();
    }

    @Override
    public void execute() {
        for(User user : users) {
            for(Account account : user.getAccounts()) {
                if(account.getIban().equals(commandInput.getAccount())) {
//                    System.out.printf("asdasdskaldaslkd\n");
                    if(commandInput.getNewPlanType().equals("silver")
                        && (account.getAccountPlan().equals("standard")
                            || account.getAccountPlan().equals("student"))) {
                        if(account.getMinBalance() > account.getBalance() - 100) {
                            account.setBalance(account.getBalance() - 100);
                            Transaction transaction = new Transaction(
                                    "Upgrade plan",
                                    commandInput.getTimestamp(),
                                    account.getIban(),
                                    commandInput.getNewPlanType()
                            );
                            transactions.add(transaction);
                        }
                    }
                }
            }
        }
    }
}
