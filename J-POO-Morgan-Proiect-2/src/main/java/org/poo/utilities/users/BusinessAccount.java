package org.poo.utilities.users;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class BusinessAccount {
    private String iban;
    private double balance;
    private double minBalance;
    private String currency;
    private String accountType;
    private String alias;
    private double interestRate;

    private int numberOfTransactions;
    private int gotFoodCashback;
    private int gotClothesCashback;
    private int gotTechCashback;
    private int gotCashbacks;
    private String accountPlan;

    private int PaymentsOver300;
    private int FoodPayments;
    private int ClothesPayments;
    private int TechPayments;

    private double deposited;
    private double spent;

    private String OwnerEmail;
    private ArrayList<User> employees;
    private ArrayList<User> managers;
    private ArrayList<Card> cards;

    private double spendingLimit;
    private double depositLimit;


    public BusinessAccount(final String iban, final String currency, final String accountType,
                   final double interestRate, final double balance, final double minBalance,
                           final String ownerEmail) {
        this.iban = iban;
        this.currency = currency;
        this.accountType = accountType;
        this.interestRate = interestRate;
        this.balance = balance;
        this.minBalance = minBalance;
        this.OwnerEmail = ownerEmail;
        this.employees = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.cards = new ArrayList<>();
    }



}
