package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.CurrencyGraph;
import org.poo.utilities.users.Transaction;
import org.poo.utilities.users.User;

import java.util.ArrayList;
@Getter
@Setter
public class Builder {
    private ArrayList<User> users;                // mandatory
    private CommandInput commandInput;            // mandatory
    private ArrayList<Transaction> transactions;  // optional
    private CurrencyGraph currencyGraph;          // optional
    private ObjectMapper objectMapper;            // optional
    private ObjectNode commandNode;               // optional
    private int isOneTimeCard;                    // optional
    private ArrayNode output;                     // optional




    public Builder(ArrayList<User> users, CommandInput commandInput) {
        this(users, commandInput, null, null, null, null, 0);
    }

    public Builder(ArrayList<User> users, CommandInput commandInput, ArrayList<Transaction> transactions) {
        this(users, commandInput, transactions, null, null, null, 0);
    }

    public Builder(ArrayList<User> users, CommandInput commandInput, ArrayList<Transaction> transactions, CurrencyGraph currencyGraph) {
        this(users, commandInput, transactions, currencyGraph, null, null, 0);
    }

    public Builder(ArrayList<User> users, CommandInput commandInput, ArrayList<Transaction> transactions, CurrencyGraph currencyGraph, ObjectMapper objectMapper) {
        this(users, commandInput, transactions, currencyGraph, objectMapper, null, 0);
    }

    public Builder(ArrayList<User> users, CommandInput commandInput, ArrayList<Transaction> transactions, CurrencyGraph currencyGraph, ObjectMapper objectMapper, ObjectNode commandNode) {
        this(users, commandInput, transactions, currencyGraph, objectMapper, commandNode, 0);
    }

    public Builder(ArrayNode output, ArrayList<User> users, ObjectMapper objectMapper,
                   ObjectNode commandNode, CommandInput commandInput) {
        this(users, commandInput, null, null, objectMapper, commandNode, 0);
        this.output = output;
    }

    public Builder(ArrayNode output, ArrayList<User> users,
                   ObjectMapper objectMapper,
                   ObjectNode commandNode,
                   CommandInput commandInput, ArrayList<Transaction> transactions) {
        this(users, commandInput, transactions, null, objectMapper, commandNode, 0);
        this.output = output;
    }

    public Builder(ArrayNode output,
                   ObjectMapper objectMapper,
                   ObjectNode commandNode,
                   CommandInput commandInput, ArrayList<Transaction> transactions) {
        this(null, commandInput, transactions, null, objectMapper, commandNode, 0);
        this.output = output;
    }

    public Builder(ArrayList<User> users, CommandInput commandInput,
                   ArrayList<Transaction> transactions, int isOneTimeCard) {
        this(users, commandInput, transactions, null, null, null, isOneTimeCard);
    }

    public Builder(ArrayList<User> users, CommandInput commandInput, ArrayList<Transaction> transactions,
                   CurrencyGraph currencyGraph, ObjectMapper objectMapper, ObjectNode commandNode,
                   ArrayNode output) {
        this(users, commandInput, transactions, currencyGraph, objectMapper, commandNode, 0);
        this.output = output;
    }

    public Builder(ArrayList<User> users, CommandInput commandInput, ArrayList<Transaction> transactions, CurrencyGraph currencyGraph, ObjectMapper objectMapper, ObjectNode commandNode, int isOneTimeCard) {
        this.users = users;
        this.commandInput = commandInput;
        this.transactions = transactions;
        this.currencyGraph = currencyGraph;
        this.objectMapper = objectMapper;
        this.commandNode = commandNode;
        this.isOneTimeCard = isOneTimeCard;
    }
}
