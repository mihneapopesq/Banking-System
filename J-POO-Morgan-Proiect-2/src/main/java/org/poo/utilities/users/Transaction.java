package org.poo.utilities.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Transaction {
    private String description;
    private int timestamp;
    private double amount;
    private String currency;
    private String receiverIBAN;
    private String senderIBAN;
    private String transferType;
    private String iban;
    private String cardNumber;
    private String cardHolder;
    private String commerciant;
    private double amountSpent;
    private String email;
    private List<String> accounts;
    private String reportIban;
    private String errorAccount;
    private String newPlanType;

    public Transaction(final int timestamp, final String description, final String iban,
                       final String newPlanType){
        this.description = description;
        this.timestamp = timestamp;
        this.iban = iban;
        this.newPlanType = newPlanType;
    }


    public Transaction(final String description, final int timestamp, final String iban,
                       final String cardNumber, final String cardHolder, final String email,
                       final String reportIban) {
        this.description = description;
        this.timestamp = timestamp;
        this.iban = iban;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.email = email;
        this.reportIban = reportIban;
        this.amount = 0;
        this.currency = null;
    }

    public Transaction(final String description, final int timestamp, final double amount,
                       final String iban, final double amountSpent, final String email) {
        this.description = description;
        this.timestamp = timestamp;
        this.amount = amount;
        this.iban = iban;
        this.amountSpent = amountSpent;
        this.email = email;
    }

    public Transaction(final String description, final double amountSpent, final String currency,
                       final double amount, final List<String> accounts, final int timestamp,
                       final String email, final String reportIban) {
        this.description = description;
        this.amountSpent = amountSpent;
        this.currency = currency;
        this.amount = amount;
        this.accounts = accounts;
        this.timestamp = timestamp;
        this.email = email;
        this.reportIban = reportIban;
    }

    public Transaction(final double amount, final String currency, final String transferType,
                       final int timestamp, final String description, final String senderIBAN,
                       final String receiverIBAN, final String email, final String reportIban) {
        this.amount = amount;
        this.currency = currency;
        this.transferType = transferType;
        this.timestamp = timestamp;
        this.description = description;
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.email = email;
        this.reportIban = reportIban;
    }

    public Transaction(final String description, final int timestamp, final double amount,
                       final String currency, final String receiverIBAN, final String senderIBAN,
                       final String transferType, final String iban, final String cardNumber,
                       final String cardHolder, final String commerciant, final double amountSpent,
                       final String email, final List<String> accounts, final String reportIban,
                       final String errorAccount) {
        this.description = description;
        this.timestamp = timestamp;
        this.amount = amount;
        this.currency = currency;
        this.receiverIBAN = receiverIBAN;
        this.senderIBAN = senderIBAN;
        this.transferType = transferType;
        this.iban = iban;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.commerciant = commerciant;
        this.amountSpent = amountSpent;
        this.email = email;
        this.accounts = accounts;
        this.reportIban = reportIban;
        this.errorAccount = errorAccount;
    }

    public Transaction(final String description, final int timestamp, final String email,
                       final String reportIban) {
        this.description = description;
        this.timestamp = timestamp;
        this.email = email;
        this.reportIban = reportIban;
    }

    public Transaction(final String description, final int timestamp, final String email) {
        this.description = description;
        this.timestamp = timestamp;
        this.email = email;
    }

    public Transaction(final String description, final int timestamp, final double amountSpent,
                       final String commerciant, final String email, final String reportIban) {
        this.description = description;
        this.timestamp = timestamp;
        this.amountSpent = amountSpent;
        this.commerciant = commerciant;
        this.email = email;
        this.reportIban = reportIban;
    }

    public Transaction(final String description, final double amountSpent, final String currency,
                       final double amount, final List<String> accounts, final int timestamp,
                       final String email, final String reportIban, final String errorAccount) {
        this.description = description;
        this.amountSpent = amountSpent;
        this.currency = currency;
        this.amount = amount;
        this.accounts = accounts;
        this.timestamp = timestamp;
        this.email = email;
        this.reportIban = reportIban;
        this.errorAccount = errorAccount;
    }

    /**
     * Populates the provided `transactionNode` with the details of the transaction.
     *
     * @param transaction The `Transaction` object whose details are to be populated.
     * @param transactionNode The `ObjectNode` that will hold the transaction details
     *                        in JSON format.
     * @param objectMapper The `ObjectMapper` used to create the JSON nodes.
     */
    public void populateTransactionNode(final Transaction transaction,
                                        final ObjectNode transactionNode,
                                        final ObjectMapper objectMapper) {
        transactionNode.put("timestamp", transaction.getTimestamp());
        transactionNode.put("description", transaction.getDescription());

        if (transaction.getSenderIBAN() != null) {
            transactionNode.put("senderIBAN", transaction.getSenderIBAN());
        }

        if (transaction.getReceiverIBAN() != null) {
            transactionNode.put("receiverIBAN", transaction.getReceiverIBAN());
        }

        if (transaction.getAmount() != 0 && transaction.getCurrency() != null) {
            transactionNode.put("amount", transaction.getAmount() + " "
                    + transaction.getCurrency());
        }

        if (transaction.getTransferType() != null) {
            transactionNode.put("transferType", transaction.getTransferType());
        }

        if (transaction.getCardNumber() != null) {
            transactionNode.put("card", transaction.getCardNumber());
        }

        if (transaction.getCardHolder() != null) {
            transactionNode.put("cardHolder", transaction.getCardHolder());
        }

        if (transaction.getIban() != null) {
            transactionNode.put("account", transaction.getIban());
        }

        if (transaction.getCommerciant() != null) {
            transactionNode.put("commerciant", transaction.getCommerciant());
        }

        if (transaction.getAmountSpent() > 0) {
            transactionNode.put("amount", transaction.getAmountSpent());
        }

        if(transaction.getNewPlanType() != null){
            transactionNode.put("newPlanType", transaction.getNewPlanType());
        }

        if (transaction.getAccounts() != null) {
            transactionNode.remove("description");
            transactionNode.remove("amount");
            transactionNode.remove("currency");
            transactionNode.remove("accounts");

            String formattedAmount = String.format("%.2f", transaction.getAmount());

            transactionNode.put("amount", transaction.getAmountSpent());
            transactionNode.put("currency", transaction.getCurrency());
            transactionNode.put("description", "Split payment of " + formattedAmount + " "
                    + transaction.getCurrency());

            if (transaction.getErrorAccount() != null) {
                transactionNode.put("error", transaction.getErrorAccount());
            }

            ArrayNode involvedAccounts = objectMapper.createArrayNode();
            for (String acc : transaction.getAccounts()) {
                involvedAccounts.add(acc);
            }
            transactionNode.set("involvedAccounts", involvedAccounts);
        }
    }
}
