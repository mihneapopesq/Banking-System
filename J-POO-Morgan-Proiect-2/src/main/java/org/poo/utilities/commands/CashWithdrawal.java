package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.*;

import java.util.ArrayList;

public class CashWithdrawal extends CommandBase{
    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final ArrayNode output;
    private final ObjectMapper objectMapper;
    private final ObjectNode commandNode;
    private final ArrayList<Transaction> transactions;
    private final CurrencyGraph currencyGraph;

    public CashWithdrawal(final Builder builder) {
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
              for(Card card : account.getCards()) {
//                  System.out.printf("am intrat in forul cu carduri\n");
                  if(card.getCardNumber().equals(commandInput.getCardNumber())) {
//                      System.out.printf("am intrat dupa in if\n");
                      double amount = commandInput.getAmount();
                      if(account.getBalance() - account.getMinBalance() < amount) {
                          Transaction transaction = new Transaction(
                                  "Insufficient funds",
                                  commandInput.getTimestamp(),
                                  user.getUser().getEmail()
                          );
                          transactions.add(transaction);
                          return;
                      }

                      double cashback = 0;

                      System.out.printf("account type ul %s\n", account.getAccountType());

                      if(user.getUserPlan().equals("standard")) {
                          double comision = amount * 0.002;
                          comision = currencyGraph.convertCurrency("RON", account.getCurrency(), comision);
                          account.setBalance(account.getBalance() - comision);
                      }

                      if(user.getUserPlan().equals("silver") && amount >= 500) {
                            double comision = amount * 0.001;
                            comision = currencyGraph.convertCurrency("RON", account.getCurrency(), comision);
                            account.setBalance(account.getBalance() - comision);
                      }

                      // todo set balance
                      double righAmount = currencyGraph.convertCurrency("RON", account.getCurrency(), amount);
                      account.setBalance(account.getBalance() - righAmount + cashback);

                      Transaction transaction = new Transaction(
                              "Cash withdrawal of " + commandInput.getAmount(),
                              commandInput.getTimestamp(),
                              user.getUser().getEmail(),
                              commandInput.getAmount()
                      );
                      transactions.add(transaction);

                      return;
                  }
              }
          }
      }

        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("timestamp", commandInput.getTimestamp());
        errorNode.put("description", "Card not found");
        commandNode.set("output", errorNode);
        commandNode.put("command", "cashWithdrawal");
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
}
