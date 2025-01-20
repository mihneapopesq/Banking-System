package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.*;
import org.poo.utils.Utils;

import java.util.ArrayList;

/**
 * Command for processing online payments using a card.
 * Validates card status and account balance before processing the payment.
 */
public class PayOnline extends CommandBase {

    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final CurrencyGraph currencyGraph;
    private final ObjectNode commandNode;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;
    private final ArrayList<Transaction> transactions;
    private final ArrayList<Commerciant> commerciants;
    private final ArrayList<BusinessAccount> businessAccounts;

    /**
     * Constructs the PayOnline command using the provided builder.
     *
     * @param builder the builder containing the dependencies and configuration for this command.
     */
    public PayOnline(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.currencyGraph = builder.getCurrencyGraph();
        this.commandNode = builder.getCommandNode();
        this.objectMapper = builder.getObjectMapper();
        this.output = builder.getOutput();
        this.transactions = builder.getTransactions();
        this.commerciants = builder.getCommerciants();
        this.businessAccounts = builder.getBusinessAccounts();
    }

    /**
     * Executes the command to process an online payment.
     * Handles frozen cards, insufficient funds, and one-time card handling.
     */
    @Override
    public void execute() {
        String targetEmail = commandInput.getEmail();
        String targetCardNumber = commandInput.getCardNumber();
        String paymentCurrency = commandInput.getCurrency();
        double paymentAmount = commandInput.getAmount();
        if(paymentAmount == 0) {
            return ;
        }

        //search commerciant
        Commerciant commerciant = new Commerciant();
        for(Commerciant commerciant1 : commerciants) {
            if(commerciant1.getCommerciant().getCommerciant().equals(commandInput.getCommerciant())) {
                commerciant = commerciant1;
                break;
            }
        }

    int payed = 0;

    for(BusinessAccount businessAccount : businessAccounts) {
        for(User user : businessAccount.getEmployees()) {
            if(user.getUser().getEmail().equals(commandInput.getEmail())) {
                for(Card card : businessAccount.getCards()) {
                    if(card.getCardNumber().equals(commandInput.getCardNumber())) {

                        double ronAmount = currencyGraph.convertCurrency(paymentCurrency, "RON", paymentAmount);
                        if(ronAmount >= 500) {
                            return ;
                        }


                        businessAccount.setBalance(businessAccount.getBalance() - paymentAmount);
                        user.setSpent(user.getSpent() + paymentAmount);
                        payed = 1;
                    }
                }
            }
        }

        for(User user : businessAccount.getManagers()) {
            if(user.getUser().getEmail().equals(commandInput.getEmail())) {
                for(Card card : businessAccount.getCards()) {
                    if(card.getCardNumber().equals(commandInput.getCardNumber())) {
                        businessAccount.setBalance(businessAccount.getBalance() - paymentAmount);
                        user.setSpent(user.getSpent() + paymentAmount);
                        payed = 1;
                    }
                }
            }
        }

        if(businessAccount.getOwnerEmail().equals(commandInput.getEmail())) {
            for(Card card : businessAccount.getCards()) {
                if(card.getCardNumber().equals(commandInput.getCardNumber())) {
                    businessAccount.setBalance(businessAccount.getBalance() - paymentAmount);
                    for(User user : users) {
                        if (user.getUser().getEmail().equals(commandInput.getEmail())) {
                            user.setSpent(user.getSpent() + paymentAmount);
                        }
                    }
                    payed = 1;
                }
            }
        }
    }
    if(payed == 1)
        return;




        for (User user : users) {
            if (user.getUser().getEmail().equals(targetEmail)) {
                for (Account account : user.getAccounts()) {
                    for (Card card : account.getCards()) {
                        if (card.getCardNumber().equals(targetCardNumber)) {

                            if (card.getIsFrozen() == 1) {
                                addTransaction("The card is frozen", user, account);
                                return;
                            }

                            double amountInAccountCurrency = currencyGraph.convertCurrency(
                                    paymentCurrency, account.getCurrency(), paymentAmount);

                            if (account.getMinBalance() > account.getBalance()
                                    - amountInAccountCurrency) {
                                addTransaction("Insufficient funds", user, account);
                                return;
                            }
                            double cashback = 0;

                            if(commerciant.getCommerciant().getCashbackStrategy().equals("spendingThreshold")) {
                                if(paymentCurrency.equals("RON") && (user.getUserPlan().equals("standard") ||
                                        user.getUserPlan().equals("student"))) {
                                    if(paymentAmount >= 100 && paymentAmount < 300) {
                                        cashback = amountInAccountCurrency * 0.001;
                                    } else if(paymentAmount >= 300 && paymentAmount < 500) {
                                        cashback = amountInAccountCurrency * 0.002;
                                    } else if(paymentAmount >= 500) {
                                        cashback = amountInAccountCurrency * 0.0025;
                                    }
                                }
                            }

                            if(commerciant.getCommerciant().getCashbackStrategy().equals("spendingThreshold")) {
                                if(paymentCurrency.equals("RON") && (user.getUserPlan().equals("silver"))) {
//                                    System.out.printf("al doilea if la tmstp %d\n", commandInput.getTimestamp());
                                    if(paymentAmount >= 100 && paymentAmount < 300) {
                                        cashback = amountInAccountCurrency * 0.003;
                                    } else if(paymentAmount >= 300 && paymentAmount < 500) {
                                        cashback = amountInAccountCurrency * 0.004;
                                    } else if(paymentAmount >= 500) {
                                        cashback = amountInAccountCurrency * 0.005;
                                    }
                                }
                            }

                            if(commerciant.getCommerciant().getCashbackStrategy().equals("spendingThreshold")) {
                                if(paymentCurrency.equals("RON") && (user.getUserPlan().equals("gold"))) {
//                                    System.out.printf("al treilea if la tmstp %d\n", commandInput.getTimestamp());
                                    if(paymentAmount >= 100 && paymentAmount < 300) {
                                        cashback = amountInAccountCurrency * 0.005;
                                    } else if(paymentAmount >= 300 && paymentAmount < 500) {
                                        cashback = amountInAccountCurrency * 0.0055;
                                    } else if(paymentAmount >= 500) {
                                        cashback = amountInAccountCurrency * 0.007;
                                    }
                                }
                            }

                            if(commerciant.getCommerciant().getType().equals("Food")) {
                                account.setFoodPayments(account.getFoodPayments() + 1);
                            } else if(commerciant.getCommerciant().getType().equals("Clothes")) {
                                account.setClothesPayments(account.getClothesPayments() + 1);
                            } else if(commerciant.getCommerciant().getType().equals("Tech")) {
                                account.setTechPayments(account.getTechPayments() + 1);
                            }

                            account.setNumberOfTransactions(account.getNumberOfTransactions() + 1);


                            // todo implementeaza incat sa numere pentru fiecare tip de comerciant

                            if(commerciant.getCommerciant().getCashbackStrategy().equals("nrOfTransactions")) {


                                if(commerciant.getCommerciant().getType().equals("Food")) {
                                    if(account.getFoodPayments() > 2 && account.getGotFoodCashback() == 0) {
                                        cashback = amountInAccountCurrency * 0.02;
                                        account.setGotFoodCashback(1);
                                        account.setGotCashbacks(1);
                                    }
                                } else if(commerciant.getCommerciant().getType().equals("Clothes")) {
                                    if(account.getClothesPayments() > 5 && account.getGotClothesCashback() == 0) {
                                        cashback = amountInAccountCurrency * 0.05;
                                        account.setGotClothesCashback(1);
                                        account.setGotCashbacks(1);
                                    }
                                } else if(commerciant.getCommerciant().getType().equals("Tech")) {
                                    if(account.getTechPayments() > 10 && account.getGotTechCashback() == 0) {
                                        cashback = amountInAccountCurrency * 0.1;
                                        account.setGotTechCashback(1);
                                        account.setGotCashbacks(1);
                                    }
                                }
                            }


                            if(currencyGraph.convertCurrency(account.getCurrency(), "RON", amountInAccountCurrency) >= 300
                               && user.getUserPlan().equals("silver")) {
                                account.setPaymentsOver300(account.getPaymentsOver300() + 1);
                                if(account.getPaymentsOver300() >= 5) {
                                    new UpgradePlan(
                                            new Builder(users, commandInput, transactions, currencyGraph, objectMapper)
                                    ).execute();
                                }
                            }
                            // todo implementeaza restul de cashback si aici
                            if(user.getUserPlan().equals("standard")) {
                                double comision = amountInAccountCurrency * 0.002;
                                account.setBalance(account.getBalance() - comision);
                            }

                            double ronAmount = currencyGraph.convertCurrency(account.getCurrency(), "RON", amountInAccountCurrency);
                            if(user.getUserPlan().equals("silver") && ronAmount >= 500) {
                                double comision = amountInAccountCurrency * 0.001;
                                account.setBalance(account.getBalance() - comision);
                            }

                            account.setBalance(account.getBalance() - (amountInAccountCurrency - cashback));

                            transactions.add(new Transaction(
                                    "Card payment",
                                    commandInput.getTimestamp(),
                                    amountInAccountCurrency,
                                    commandInput.getCommerciant(),
                                    user.getUser().getEmail(),
                                    account.getIban()
                            ));

                            if (card.getIsOneTimeCard() == 1) {
                                handleOneTimeCard(user, account);
                            }
                            return;
                        }
                    }
                }
            }
        }

        addCardNotFoundError();
    }

    private void addTransaction(final String description, final User user, final Account account) {
        transactions.add(new Transaction(
                description,
                commandInput.getTimestamp(),
                user.getUser().getEmail(),
                account.getIban()
        ));
    }

    private void handleOneTimeCard(final User user, final Account account) {
        CommandBase deleteCardCommand = new DeleteCard(
                new Builder(users, commandInput, transactions)
        );
        deleteCardCommand.execute();

        Card newCard = new Card();
        newCard.setCardNumber(Utils.generateCardNumber());
        newCard.setStatus("active");
        newCard.setIsOneTimeCard(1);

        transactions.add(new Transaction(
                "New card created",
                commandInput.getTimestamp(),
                account.getIban(),
                newCard.getCardNumber(),
                user.getUser().getEmail(),
                user.getUser().getEmail(),
                account.getIban()
        ));

        account.getCards().add(newCard);
    }

    private void addCardNotFoundError() {
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("timestamp", commandInput.getTimestamp());
        errorNode.put("description", "Card not found");
        commandNode.set("output", errorNode);
        commandNode.put("command", "payOnline");
        commandNode.put("timestamp", commandInput.getTimestamp());
        output.add(commandNode);
    }
}
