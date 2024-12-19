package org.poo.utilities.users;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;

import java.util.ArrayList;

@Getter
@Setter
public class User {
    private UserInput user;
    private ArrayList<Transaction> transactions;
    private ArrayList<Account> accounts;

    public User() {
        this.accounts = new ArrayList<>();
    }

    public User(final UserInput user, final ArrayList<Account> accounts) {
        this.user = user;
        this.accounts = accounts != null ? accounts : new ArrayList<>();
    }
}
