package org.poo.utilities.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.*;

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
        List<String> accounts = commandInput.getAccounts();
        double amountPerAccount = 0;
        if (accounts != null) {
            int numberOfAccounts = accounts.size();
            amountPerAccount = commandInput.getAmount() / numberOfAccounts;
        }

        if ("acceptSplitPayment".equals(commandInput.getCommand())) {
            handleAcceptSplitPayment();
            return;
        }

        if ("splitPayment".equals(commandInput.getCommand())) {
            handleNewSplitPayment(accounts, amountPerAccount);
        }
    }

    private void handleAcceptSplitPayment() {
        String email = commandInput.getEmail();
        String splitPaymentType = commandInput.getSplitPaymentType();

        for (PendingSplitPayment pendingPayment : pendingSplitPayments) {
            if (splitPaymentType.equals(pendingPayment.getSplitPaymentType()) && !pendingPayment.isCompleted()) {
                for (String iban : pendingPayment.getAccounts()) {
                    User user = findUserByIBAN(iban);

                    if (user != null && user.getUser().getEmail().equals(email)) {
                        pendingPayment.accept(iban);

                        if (pendingPayment.isFullyAccepted()) {
                            boolean allAccountsValid = true;
                            String errorIban = null;

                            for (int i = 0; i < pendingPayment.getAccounts().size(); i++) {
                                String involvedIban = pendingPayment.getAccounts().get(i);
                                Account account = findAccountByIBAN(involvedIban);

                                if (account == null) {
                                    allAccountsValid = false;
                                    errorIban = involvedIban;
                                    break;
                                }

                                double requiredAmount;
                                if ("equal".equals(splitPaymentType)) {
                                    requiredAmount = graph.convertCurrency(
                                            pendingPayment.getCurrency(),
                                            account.getCurrency(),
                                            pendingPayment.getTotalAmount() / pendingPayment.getAccounts().size()
                                    );
                                } else { // "custom"
                                    requiredAmount = graph.convertCurrency(
                                            pendingPayment.getCurrency(),
                                            account.getCurrency(),
                                            pendingPayment.getAmounts().get(i)
                                    );
                                }

                                if (account.getBalance() - account.getMinBalance() < requiredAmount) {
                                    allAccountsValid = false;
                                    errorIban = involvedIban;
                                    break;
                                }
                            }

                            if (!allAccountsValid) {
                                for (int i = 0; i < pendingPayment.getAccounts().size(); i++) {
                                    String involvedIban = pendingPayment.getAccounts().get(i);
                                    User involvedUser = findUserByIBAN(involvedIban);


                                    if (involvedUser != null) {
                                        double amountPerUser = 0.0;
                                        if(splitPaymentType.equals("equal")) {
                                            amountPerUser = pendingPayment.getTotalAmount() / pendingPayment.getAccounts().size();
                                        }

                                        Transaction transaction;
                                        if (splitPaymentType.equals("equal")) {
                                            transaction = new Transaction(
                                                    amountPerUser, // O singură sumă
                                                    pendingPayment.getCurrency(),
                                                    "Account " + errorIban + " has insufficient funds for a split payment.",
                                                    pendingPayment.getAccounts(),
                                                    splitPaymentType,
                                                    pendingPayment.getTimestamp(),
                                                    involvedUser.getUser().getEmail(),
                                                    amountPerUser * pendingPayment.getAccounts().size()
                                            );
                                        } else {
                                            transaction = new Transaction(
                                                    pendingPayment.getAmounts(),
                                                    pendingPayment.getCurrency(),
                                                    pendingPayment.getTotalAmount(),
                                                    pendingPayment.getAccounts(),
                                                    splitPaymentType,
                                                    pendingPayment.getTimestamp(),
                                                    involvedUser.getUser().getEmail(),
                                                    "Account " + errorIban + " has insufficient funds for a split payment."
                                            );
                                        }
                                        transactions.add(transaction);
                                        transactions.sort((t1, t2) -> Integer.compare(t1.getTimestamp(), t2.getTimestamp()));
                                    }
                                }
                                pendingPayment.markAsCompleted();
                            } else {
                                if ("custom".equals(splitPaymentType)) {
                                    processSplitPaymentCustom(pendingPayment);
                                } else if ("equal".equals(splitPaymentType)) {
                                    double amountPerUser = pendingPayment.getTotalAmount() / pendingPayment.getAccounts().size();
                                    processSplitPaymentEqual(pendingPayment, amountPerUser);
                                }
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

    private void handleNewSplitPayment(List<String> accounts, double amountPerAccount) {
        double totalAmount = commandInput.getAmount();
        String currency = commandInput.getCurrency();
        String splitPaymentType = commandInput.getSplitPaymentType();
        int timestamp = commandInput.getTimestamp();

        HashMap<String, Boolean> approvalStatus = new HashMap<>();
        for (String iban : accounts) {
            approvalStatus.put(iban, false);
        }

        PendingSplitPayment pendingPayment;
        if ("equal".equals(splitPaymentType)) {
            double amountPerUser = totalAmount / accounts.size();
            List<Double> amountsForUsers = new ArrayList<>();
            for (int i = 0; i < accounts.size(); i++) {
                amountsForUsers.add(amountPerUser);
            }

            pendingPayment = new PendingSplitPayment(
                    accounts, amountsForUsers, totalAmount, currency, approvalStatus, timestamp, splitPaymentType
            );
        } else { // "custom"
            List<Double> amountsForUsers = commandInput.getAmountForUsers();
            if (amountsForUsers.size() != accounts.size()) {
                System.out.println("Number of amounts does not match number of accounts. Aborting.");
                return;
            }

            pendingPayment = new PendingSplitPayment(
                    accounts, amountsForUsers, totalAmount, currency, approvalStatus, timestamp, splitPaymentType
            );
        }

        pendingSplitPayments.add(pendingPayment);
    }

    private void processSplitPaymentCustom(PendingSplitPayment pendingPayment) {
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
                            amounts,
                            currency,
                            pendingPayment.getTotalAmount(),
                            accounts,
                            "custom",
                            pendingPayment.getTimestamp(),
                            user.getUser().getEmail()
                    );
                    transactions.add(transaction);
                    transactions.sort((t1, t2) -> Integer.compare(t1.getTimestamp(), t2.getTimestamp()));
                }
            }
        }
        pendingPayment.markAsCompleted();
    }

    private void processSplitPaymentEqual(PendingSplitPayment pendingPayment, double amountPerUser) {
        List<String> accounts = pendingPayment.getAccounts();
        String currency = pendingPayment.getCurrency();

        for (String iban : accounts) {
            Account account = findAccountByIBAN(iban);
            if (account != null) {
                double requiredAmount = graph.convertCurrency(currency, account.getCurrency(), amountPerUser);
                account.setBalance(account.getBalance() - requiredAmount);
                User user = findUserByIBAN(iban);
                if (user != null) {
                    Transaction transaction = new Transaction(
                            List.of(amountPerUser),
                            currency,
                            pendingPayment.getTotalAmount(),
                            accounts,
                            "equal",
                            pendingPayment.getTimestamp(),
                            user.getUser().getEmail()
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
