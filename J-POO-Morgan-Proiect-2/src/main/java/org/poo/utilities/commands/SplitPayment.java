package org.poo.utilities.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.CurrencyGraph;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;
import org.poo.utilities.users.PendingSplitPayment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Command for performing a split payment across multiple accounts.
 * Ensures that all accounts involved have sufficient balance before completing the transaction.
 */
public class SplitPayment extends CommandBase {
    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final CurrencyGraph graph;
    private final ArrayList<Transaction> transactions;
    private final ArrayList<PendingSplitPayment> pendingSplitPayments;
    /**
     * Constructs the SplitPayment command using the provided builder.
     *
     * @param builder the builder containing the dependencies and configuration for this command.
     */
    public SplitPayment(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.graph = builder.getCurrencyGraph();
        this.transactions = builder.getTransactions();
        this.pendingSplitPayments = builder.getPendingSplitPayments();
    }

    /**
     * Executes the split payment operation.
     * Validates account balances, performs currency conversion, and handles transactions
     * for all accounts.
     */
    @Override
    public void execute() {


        if ("acceptSplitPayment".equals(commandInput.getCommand())) {

            String email = commandInput.getEmail();
            String splitPaymentType = commandInput.getSplitPaymentType();

            for (PendingSplitPayment pendingPayment : pendingSplitPayments) {


                if (splitPaymentType.equals("custom") && !pendingPayment.isCompleted()) {


                    for (String iban : pendingPayment.getAccounts()) {


                        User user = findUserByIBAN(iban);
                        if (user != null && user.getUser().getEmail().equals(email)) {


                            pendingPayment.accept(iban);

                            // If all accounts have accepted, execute the payment
                            if (pendingPayment.isFullyAccepted()) {
                                processSplitPayment(pendingPayment);
                            }
                            return; // Exit after processing this acceptance
                        }
                    }
                }
            }
            return; // Exit if no matching pending split payment is found
        }





        List<String> accounts = commandInput.getAccounts();
        int numberOfAccounts = accounts.size();
        double amountPerAccount = commandInput.getAmount() / numberOfAccounts;


        if(commandInput.getSplitPaymentType().equals("custom")) {


            List<Double> amountsForUsers = commandInput.getAmountForUsers();

            HashMap<String, Boolean> approvalStatus = new HashMap<>();
            for (String iban : accounts) {


                approvalStatus.put(iban, false); // Mark all accounts as pending approval
            }

            PendingSplitPayment pendingPayment = new PendingSplitPayment(
                    accounts, amountsForUsers, commandInput.getAmount(), commandInput.getCurrency(), approvalStatus, commandInput.getTimestamp()
            );
            pendingSplitPayments.add(pendingPayment);


            return;
        }

        boolean canSplit = true;
        Account invalidAccount = null;

        for (String iban : accounts) {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIban().equals(iban)) {
                        double rightAmount = graph.convertCurrency(commandInput.getCurrency(),
                                account.getCurrency(), amountPerAccount);

                        if (account.getMinBalance() > account.getBalance() - rightAmount) {
                            invalidAccount = account;
                            canSplit = false;
                        }
                    }
                }
            }
        }

        if (canSplit) {

            for (String iban : accounts) {
                for (User user : users) {
                    for (Account account : user.getAccounts()) {
                        if (account.getIban().equals(iban)) {
                            Transaction userTransaction = new Transaction(
                                    "Split payment of",
                                    amountPerAccount,
                                    commandInput.getCurrency(),
                                    commandInput.getAmount(),
                                    accounts,
                                    commandInput.getTimestamp(),
                                    user.getUser().getEmail(),
                                    account.getIban()
                            );

                            transactions.add(userTransaction);

                            double rightAmount = graph.convertCurrency(commandInput.getCurrency(),
                                    account.getCurrency(), amountPerAccount);

                            account.setBalance(account.getBalance() - rightAmount);
                        }
                    }
                }
            }
        } else {
            for (String iban : accounts) {
                for (User user : users) {
                    for (Account account : user.getAccounts()) {
                        if (account.getIban().equals(iban)) {
                            Transaction userTransaction = new Transaction(
                                    "Split payment of",
                                    amountPerAccount,
                                    commandInput.getCurrency(),
                                    commandInput.getAmount(),
                                    accounts,
                                    commandInput.getTimestamp(),
                                    user.getUser().getEmail(),
                                    account.getIban(),
                                    "Account " + invalidAccount.getIban()
                                            + " has insufficient funds for a split payment."
                            );

                            transactions.add(userTransaction);
                        }
                    }
                }
            }
        }
    }


    private void processSplitPayment(PendingSplitPayment pendingPayment) {
        List<String> accounts = pendingPayment.getAccounts();
        List<Double> amounts = pendingPayment.getAmounts();
        String currency = pendingPayment.getCurrency();

        for (int i = 0; i < accounts.size(); i++) {
            String iban = accounts.get(i);
            Account account = findAccountByIBAN(iban);
            if (account != null) {
                double requiredAmount = graph.convertCurrency(currency, account.getCurrency(), amounts.get(i));
                account.setBalance(account.getBalance() - requiredAmount);
                User user = findUserByIBAN(iban);
                if (user != null) {
                    Transaction transaction = new Transaction(
                            amounts, // Single user's amount
                            currency,
                            pendingPayment.getTotalAmount(),
                            accounts, // Single user's account
                            "custom", // Split payment type
                            pendingPayment.getTimestamp(),
                            user.getUser().getEmail() // Include the user's email
                    );
                    transactions.add(transaction);
                    transactions.sort((t1, t2) -> Integer.compare(t1.getTimestamp(), t2.getTimestamp()));
                }
            }
        }

        pendingPayment.markAsCompleted();
    }

    private User findUserByIBAN(String iban) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    return user;
                }
            }
        }
        return null;
    }

    private Account findAccountByIBAN(String iban) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    return account;
                }
            }
        }
        return null;
    }

}
