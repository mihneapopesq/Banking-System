package org.poo.utilities.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

/**
 * Represents a bank account
 */
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
    private int numberOfTransactions;
    private int gotFoodCashback;
    private int gotClothesCashback;
    private int gotTechCashback;
    private String accountPlan;
    private int PaymentsOver300;
//    private int numberOfTransactions;
//    private int cashbackFood;
//    private int cashbackClothes;
//    private int cashbackTech;

    /**
     * Constructs an Account with the specified attributes.
     *
     * @param iban         the unique identifier for the account.
     * @param currency     the currency of the account
     * @param accountType  the type of account
     * @param interestRate the interest rate associated with the account.
     * @param balance      the current balance of the account.
     * @param minBalance   the minimum required balance for the account.
     */
    public Account(final String iban, final String currency, final String accountType,
                   final double interestRate, final double balance, final double minBalance) {
        this.iban = iban;
        this.currency = currency;
        this.accountType = accountType;
        this.interestRate = interestRate;
        this.balance = balance;
        this.minBalance = minBalance;
        this.cards = new ArrayList<>();
        this.PaymentsOver300 = 0;
    }

    /**
     * Adds an error response to the output in case of a failed operation.
     *
     * @param objectMapper the object mapper
     * @param commandNode  the command node to which the error response is attached.
     * @param output       the output array where the error response is appended.
     * @param commandInput the command input details related to the operation.
     * @param description  the error description to be included in the response.
     */
    public void addErrorResponse(final ObjectMapper objectMapper, final ObjectNode commandNode,
                                 final ArrayNode output, final CommandInput commandInput,
                                 final String description) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        commandNode.put("command", commandInput.getCommand());
        outputNode.put("description", description);
        outputNode.put("timestamp", commandInput.getTimestamp());
        commandNode.put("timestamp", commandInput.getTimestamp());
        commandNode.set("output", outputNode);
        output.add(commandNode);
    }
}
