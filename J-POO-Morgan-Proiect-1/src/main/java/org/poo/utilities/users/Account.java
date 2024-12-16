package org.poo.utilities.users;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Account {
    private ArrayList<Card> cards;
    private String iban;
    private double balance;
    private double minBalance;
    private String currency;
    private String accountType;
    private String alias;
    private double interestRate;

    public Account(String iban, String currency, String accountType, double interestRate, double balance, double minBalance) {
        this.iban = iban;
        this.currency = currency;
        this.accountType = accountType;
        this.interestRate = interestRate;
        this.balance = balance;
        this.minBalance = minBalance;
        this.cards = new ArrayList<>();
    }
}
