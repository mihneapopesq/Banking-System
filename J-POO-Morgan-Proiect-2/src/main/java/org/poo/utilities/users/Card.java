package org.poo.utilities.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {
    private String cardNumber;
    private String status;
    private int isOneTimeCard;
    private int isFrozen;
    private int numberOfTransactions;
    private int cashbackFood;
    private int cashbackClothes;
    private int cashbackTech;
}
