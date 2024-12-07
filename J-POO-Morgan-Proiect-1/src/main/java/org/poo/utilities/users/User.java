package org.poo.utilities.users;

import org.poo.fileio.UserInput;

public class User {
    private UserInput user;
    private Account[] accounts;

    public User() {
        this.accounts = new Account[0];
    }

    public User(UserInput user, Account[] accounts) {
        this.user = user;
        this.accounts = accounts != null ? accounts : new Account[0];
    }

    public UserInput getUser() {
        return user;
    }

    public void setUser(UserInput user) {
        this.user = user;
    }

    public Account[] getAccounts() {
        return accounts;
    }

    public void setAccounts(Account[] accounts) {
        this.accounts = accounts;
    }
}
