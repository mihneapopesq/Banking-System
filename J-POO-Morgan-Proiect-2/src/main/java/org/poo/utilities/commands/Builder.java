package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.*;

import java.awt.image.AreaAveragingScaleFilter;
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
    private ArrayList<Commerciant> commerciants;   // optional
    private ArrayList<PendingSplitPayment> pendingSplitPayments; // optional
    private ArrayList<BusinessAccount> businessAccounts; // optional

    public Builder(final ArrayList<User> users, final CommandInput commandInput) {
        this(users, commandInput, null, null, null, null, 0, null);
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions, final ArrayList<BusinessAccount> businessAccounts) {
       this.users = users;
       this.commandInput = commandInput;
       this.transactions = transactions;
       this.businessAccounts = businessAccounts;
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions,
                   final ArrayList<BusinessAccount> businessAccounts,
                   final ObjectMapper objectMapper,
                   final ArrayNode output) {
        this.users = users;
        this.commandInput = commandInput;
        this.transactions = transactions;
        this.businessAccounts = businessAccounts;
        this.objectMapper = objectMapper;
        this.output = output;
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions,
                   final ArrayList<BusinessAccount> businessAccounts,
                   final CurrencyGraph currencyGraph) {
        this.users = users;
        this.commandInput = commandInput;
        this.transactions = transactions;
        this.businessAccounts = businessAccounts;
        this.currencyGraph = currencyGraph;
    }

    public Builder(final CommandInput commandInput,
                    final ArrayList<BusinessAccount> businessAccounts, final ArrayList<User> users) {
        this.users = users;
        this.commandInput = commandInput;
        this.businessAccounts = businessAccounts;
    }

    public Builder(final CommandInput commandInput,
                   final ArrayList<BusinessAccount> businessAccounts,
                   final ArrayList<User> users,
                   final CurrencyGraph currencyGraph) {
        this.users = users;
        this.commandInput = commandInput;
        this.businessAccounts = businessAccounts;
        this.currencyGraph = currencyGraph;
    }

    public Builder(final CommandInput commandInput,
                   final ArrayList<BusinessAccount> businessAccounts,
                   final ArrayList<User> users, final ObjectMapper objectMapper,
                   final ArrayNode output) {
        this.users = users;
        this.commandInput = commandInput;
        this.businessAccounts = businessAccounts;
        this.objectMapper = objectMapper;
        this.output = output;
    }


    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions,final int isOneTimeCard ,final ArrayList<BusinessAccount> businessAccounts) {
        this.users = users;
        this.commandInput = commandInput;
        this.transactions = transactions;
        this.businessAccounts = businessAccounts;
        this.isOneTimeCard = isOneTimeCard;
    }



    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions,
                   final CurrencyGraph currencyGraph, final ObjectMapper objectMapper,
                   final ObjectNode commandNode, final ArrayNode output, final ArrayList<Commerciant> commerciants) {
        this(users, commandInput, transactions, currencyGraph, objectMapper, commandNode, 0, null);
        this.output = output;
        this.commerciants = commerciants;
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions,
                   final CurrencyGraph currencyGraph, final ObjectMapper objectMapper,
                   final ObjectNode commandNode, final ArrayNode output, final ArrayList<Commerciant> commerciants,
                   final ArrayList<BusinessAccount> businessAccounts) {
        this(users, commandInput, transactions, currencyGraph, objectMapper, commandNode, 0, null);
        this.output = output;
        this.commerciants = commerciants;
        this.businessAccounts = businessAccounts;
    }



    public Builder(final ArrayList<User> users, final CommandInput command,
                   final ArrayList<Transaction> transactions,
                   final CurrencyGraph currencyGraph,
                   final ArrayList<PendingSplitPayment> pendingSplitPayments){
        this(users, command, transactions, currencyGraph, null, null, 0, pendingSplitPayments);
    }



    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions) {
        this(users, commandInput, transactions, null, null, null, 0, null);
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions, final CurrencyGraph currencyGraph) {
        this(users, commandInput, transactions, currencyGraph, null, null, 0, null);
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions, final CurrencyGraph currencyGraph,
                   final ObjectMapper objectMapper) {
        this(users, commandInput, transactions, currencyGraph, objectMapper, null, 0, null);
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions, final CurrencyGraph currencyGraph,
                   final ObjectMapper objectMapper, final ObjectNode commandNode) {
        this(users, commandInput, transactions, currencyGraph, objectMapper, commandNode, 0, null);
    }

    public Builder(final ArrayNode output, final ArrayList<User> users,
                   final ObjectMapper objectMapper, final ObjectNode commandNode,
                   final CommandInput commandInput) {
        this(users, commandInput, null, null, objectMapper, commandNode, 0, null);
        this.output = output;
    }

    public Builder(final ArrayNode output, final ArrayList<User> users,
                   final ObjectMapper objectMapper,
                   final ObjectNode commandNode,
                   final CommandInput commandInput,
                   final ArrayList<Transaction> transactions) {
        this(users, commandInput, transactions, null, objectMapper, commandNode, 0, null);
        this.output = output;
    }

    public Builder(final ArrayNode output, final ObjectMapper objectMapper,
                   final ObjectNode commandNode, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions) {
        this(null, commandInput, transactions, null, objectMapper, commandNode, 0, null);
        this.output = output;
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions, final int isOneTimeCard) {
        this(users, commandInput, transactions, null, null, null, isOneTimeCard, null);
    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions,
                   final CurrencyGraph currencyGraph, final ObjectMapper objectMapper,
                   final ObjectNode commandNode, final ArrayNode output) {
        this(users, commandInput, transactions, currencyGraph, objectMapper, commandNode, 0, null);
        this.output = output;
    }

    public Builder(final ArrayList<BusinessAccount> businessAccounts,final ArrayList<User> users,final CommandInput command,
                   final ArrayList<Transaction> transactions,final CurrencyGraph currencyGraph,
                   final ObjectMapper objectMapper,final ObjectNode commandNode,
                   final ArrayNode output){
        this(users, command, transactions, currencyGraph, objectMapper, commandNode, 0, null);
        this.output = output;
        this.businessAccounts = businessAccounts;
    }




//    public Builder(final ArrayList<User> users, final CommandInput commandInput,
//                   final ArrayList<Transaction> transactions,
//                   final CurrencyGraph currencyGraph, final ObjectMapper objectMapper,
//                   final ObjectNode commandNode, final ArrayNode output,
//                   final ArrayList<BusinessAccount> businessAccounts) {
//        this(users, commandInput, transactions, currencyGraph, objectMapper, commandNode, 0, null);
//        this.output = output;
//    }

    public Builder(final ArrayList<User> users, final CommandInput commandInput,
                   final ArrayList<Transaction> transactions, final CurrencyGraph currencyGraph,
                   final ObjectMapper objectMapper, final ObjectNode commandNode,
                   final int isOneTimeCard, final ArrayList<PendingSplitPayment> pendingSplitPayments) {
        this.users = users;
        this.commandInput = commandInput;
        this.transactions = transactions;
        this.currencyGraph = currencyGraph;
        this.objectMapper = objectMapper;
        this.commandNode = commandNode;
        this.isOneTimeCard = isOneTimeCard;
        this.pendingSplitPayments = pendingSplitPayments;
    }
}
