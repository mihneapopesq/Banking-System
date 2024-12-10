package org.poo.utilities.users;

public class Card {
    private String cardNumber;
    private String status;
    private int isOneTimeCard;
    private int isFrozen;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIsOneTimeCard() {
        return isOneTimeCard;
    }

    public void setIsOneTimeCard(int isOneTimeCard) {
        this.isOneTimeCard = isOneTimeCard;
    }

    public int getIsFrozen() {
        return isFrozen;
    }

    public void setIsFrozen(int isFrozen) {
        this.isFrozen = isFrozen;
    }
}
