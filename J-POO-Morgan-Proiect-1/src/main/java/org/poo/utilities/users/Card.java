package org.poo.utilities.users;

public class Card {
    private String cardNumber;
    private String status;
    private int isOneTimeCard;

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
}
