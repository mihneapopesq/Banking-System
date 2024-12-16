package org.poo.utilities.users;

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

    public Transaction(String description, int timestamp, String iban,
                       String cardNumber, String cardHolder, String email, String reportIban) {
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

    public Transaction(String description, int timestamp, double amount, String iban,
                       double amountSpent, String email) {
        this.description = description;
        this.timestamp = timestamp;
        this.amount = amount;
        this.iban = iban;
        this.amountSpent = amountSpent;
        this.email = email;
    }

    public Transaction(String description, double amountSpent, String currency, double amount, List<String> accounts,
                       int timestamp, String email, String reportIban) {
        this.description = description;
        this.amountSpent = amountSpent;
        this.currency = currency;
        this.amount = amount;
        this.accounts = accounts;
        this.timestamp = timestamp;
        this.email = email;
        this.reportIban = reportIban;
    }


    public Transaction(double amount, String currency, String transferType, int timestamp, String description,
                       String senderIBAN, String receiverIBAN, String email, String reportIban) {
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

    public Transaction(String description, int timestamp, double amount, String currency,
                       String receiverIBAN, String senderIBAN, String transferType, String iban,
                       String cardNumber, String cardHolder, String commerciant, double amountSpent,
                       String email, List<String> accounts, String reportIban, String errorAccount) {
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


    public Transaction(String description, int timestamp, String email, String reportIban) {
        this.description = description;
        this.timestamp = timestamp;
        this.email = email;
        this.reportIban = reportIban;
    }

    public Transaction(String description, int timestamp, String email) {
        this.description = description;
        this.timestamp = timestamp;
        this.email = email;
    }

    public Transaction(String description, int timestamp, double amountSpent, String commerciant, String email, String reportIban) {
        this.description = description;
        this.timestamp = timestamp;
        this.amountSpent = amountSpent;
        this.commerciant = commerciant;
        this.email = email;
        this.reportIban = reportIban;
    }

    public Transaction(String description, double amountSpent, String currency, double amount, List<String> accounts,
                       int timestamp, String email, String reportIban, String errorAccount) {
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
}
