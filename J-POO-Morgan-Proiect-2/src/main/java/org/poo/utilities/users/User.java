package org.poo.utilities.users;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    public int checkOldEnough() {
        if (user == null || user.getBirthDate() == null) {
            return 0;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(user.getBirthDate(), formatter);
        } catch (Exception e) {
            return 0;
        }

        long age = ChronoUnit.YEARS.between(birthDate, LocalDate.now());

        return age >= 21 ? 1 : 0;
    }
}
