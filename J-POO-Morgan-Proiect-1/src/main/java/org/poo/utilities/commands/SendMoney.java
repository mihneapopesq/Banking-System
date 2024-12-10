package org.poo.utilities.commands;

import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.CurrencyGraph;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;

public class SendMoney {
    public void sendMoney(final ArrayList<User> users, final CommandInput commandInput,
                          final CurrencyGraph currencyGraph, final ArrayList<Transaction> transactions) {

        Account senderAccount = null;
        User senderUser = null;
        for(User user : users) {
            if(user.getUser().getEmail().equals(commandInput.getEmail())) {
                for(Account account : user.getAccounts()) {
                    if(account.getIban().equals(commandInput.getAccount())) {
                        senderAccount = account;
                        senderUser = user;
                        break;
                    }
                }
            }
        }

        Account receiverAccount = null;
        for(User user : users) {
            for(Account account : user.getAccounts()) {
                if(account.getIban().equals(commandInput.getReceiver())) {
                    receiverAccount = account;
                    break;
                }
            }
        }
        double amount = commandInput.getAmount();

        if(receiverAccount == null || senderAccount == null || senderUser == null) {
            return;
        }


        if(senderAccount.getBalance() < amount) {

            Transaction transaction = new Transaction();
            transaction.setDescription("Insufficient funds");
            transaction.setTimestamp(commandInput.getTimestamp());

            transaction.setEmail(senderUser.getUser().getEmail());
            transaction.setReportIban(senderAccount.getIban());
            transactions.add(transaction);

            return;
        }

        if(senderAccount.getMinBalance() > senderAccount.getBalance() - amount) {

            Transaction transaction = new Transaction();
            transaction.setDescription("Insufficient funds");
            transaction.setTimestamp(commandInput.getTimestamp());
            transaction.setEmail(senderUser.getUser().getEmail());
            transaction.setReportIban(senderAccount.getIban());
            transactions.add(transaction);

            return;
        }

        double rightAmount = currencyGraph.convertCurrency(senderAccount.getCurrency(), receiverAccount.getCurrency(), amount);

        senderAccount.setBalance(senderAccount.getBalance() - amount);
        receiverAccount.setBalance(receiverAccount.getBalance() + rightAmount);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setCurrency(senderAccount.getCurrency());
        transaction.setTransferType("sent");
        transaction.setTimestamp(commandInput.getTimestamp());
        transaction.setDescription(commandInput.getDescription());
        transaction.setSenderIBAN(commandInput.getAccount());
        transaction.setReceiverIBAN(commandInput.getReceiver());
        transaction.setEmail(senderUser.getUser().getEmail());
        transaction.setReportIban(senderAccount.getIban());
        transactions.add(transaction);

    }
}
