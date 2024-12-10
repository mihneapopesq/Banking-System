package org.poo.utilities.users;

import org.poo.fileio.UserInput;

import java.util.ArrayList;

public class User {
    private UserInput user;
    private ArrayList<Transaction> transactions;
    private ArrayList<Account> accounts;


    public User() {
        this.accounts = new ArrayList<>();
    }

    public User(UserInput user, ArrayList<Account> accounts) {
        this.user = user;
        this.accounts = accounts != null ? accounts : new ArrayList<>();
    }

    public UserInput getUser() {
        return user;
    }

    public void setUser(UserInput user) {
        this.user = user;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }
}
