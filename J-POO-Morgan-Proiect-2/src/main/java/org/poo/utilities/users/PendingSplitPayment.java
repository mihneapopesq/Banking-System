package org.poo.utilities.users;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class PendingSplitPayment {
    private final List<String> accounts;
    private final List<Double> amounts;
    private final double totalAmount;
    private final String currency;
    private final HashMap<String, Boolean> approvalStatus;
    private final int timestamp;
    private boolean completed;

    public PendingSplitPayment(List<String> accounts, List<Double> amounts, double totalAmount,
                               String currency, HashMap<String, Boolean> approvalStatus, int timestamp) {
        this.accounts = accounts;
        this.amounts = amounts;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.approvalStatus = approvalStatus;
        this.timestamp = timestamp;
        this.completed = false;
    }

    public void accept(String iban) {
        approvalStatus.put(iban, true);
    }

    public boolean isFullyAccepted() {
        return approvalStatus.values().stream().allMatch(Boolean::booleanValue);
    }

    public void markAsCompleted() {
        this.completed = true;
    }
}
