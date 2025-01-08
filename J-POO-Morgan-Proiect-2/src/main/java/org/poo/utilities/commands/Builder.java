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

    public Builder(final ArrayList<User> users, final CommandInput commandInput) {
        this(users, commandInput, null, null, null, null, 0);
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions) {
        this(users, commandInput, transactions, null, null, null, 0);
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions, final CurrencyGraph currencyGraph) {
        this(users, commandInput, transactions, currencyGraph, null, null, 0);
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions, final CurrencyGraph currencyGraph,
                   final ObjectMapper objectMapper) {
        this(users, commandInput, transactions, currencyGraph, objectMapper, null, 0);
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions, final CurrencyGraph currencyGraph,
                   final ObjectMapper objectMapper, final ObjectNode commandNode) {
        this(users, commandInput, transactions, currencyGraph, objectMapper, commandNode, 0);
    }

    public Builder(final ArrayNode output, final ArrayList<User> users,
                   final ObjectMapper objectMapper, final ObjectNode commandNode,
                   final CommandInput commandInput) {
        this(users, commandInput, null, null, objectMapper, commandNode, 0);
        this.output = output;
    }

    public Builder(final ArrayNode output, final ArrayList<User> users,
                   final ObjectMapper objectMapper,
                   final ObjectNode commandNode,
                   final CommandInput commandInput,
                   final ArrayList<Transaction> transactions) {
        this(users, commandInput, transactions, null, objectMapper, commandNode, 0);
        this.output = output;
    }

    public Builder(final ArrayNode output, final ObjectMapper objectMapper,
                   final ObjectNode commandNode, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions) {
        this(null, commandInput, transactions, null, objectMapper, commandNode, 0);
        this.output = output;
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions, final int isOneTimeCard) {
        this(users, commandInput, transactions, null, null, null, isOneTimeCard);
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions,
                   final CurrencyGraph currencyGraph, final ObjectMapper objectMapper,
                   final ObjectNode commandNode, final ArrayNode output) {
        this(users, commandInput, transactions, currencyGraph, objectMapper, commandNode, 0);
        this.output = output;
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions, final CurrencyGraph currencyGraph,
                   final ObjectMapper objectMapper, final ObjectNode commandNode,
                   final int isOneTimeCard) {
        this.users = users;
        this.commandInput = commandInput;
        this.transactions = transactions;
        this.currencyGraph = currencyGraph;
        this.objectMapper = objectMapper;
        this.commandNode = commandNode;
        this.isOneTimeCard = isOneTimeCard;
    }
}
