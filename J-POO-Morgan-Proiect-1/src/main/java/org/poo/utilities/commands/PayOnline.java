package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.Account;
import org.poo.utilities.users.Card;
import org.poo.utilities.users.User;

import java.util.*;

public class PayOnline {
    public void payOnline(ArrayList<User> users, CommandInput commandInput,
                          Map<String, Map<String, Double>> currencyGraph,
                          ObjectNode commandNode, ObjectMapper objectMapper, ArrayNode output) {
        String targetEmail = commandInput.getEmail();
        String targetCardNumber = commandInput.getCardNumber();
        String paymentCurrency = commandInput.getCurrency();
        double paymentAmount = commandInput.getAmount();

        for (User user : users) {
            if (user.getUser().getEmail().equals(targetEmail)) {
                for (Account account : user.getAccounts()) {
                    for (Card card : account.getCards()) {
                        if (card.getCardNumber().equals(targetCardNumber)) {
                            double amountInAccountCurrency = convertCurrency(currencyGraph,
                                    paymentCurrency, account.getCurrency(), paymentAmount);

                            if(account.getMinBalance() > account.getBalance() - amountInAccountCurrency) {
                                return ;
                            }
                            account.setBalance(account.getBalance() - amountInAccountCurrency);


                            return;
                        }
                    }
                }
            }
        }

        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("timestamp", commandInput.getTimestamp());
        errorNode.put("description", "Card not found");
        commandNode.set("output", errorNode);
        commandNode.put("command", "payOnline");
        commandNode.put("timestamp", commandInput.getTimestamp());

        output.add(commandNode);
    }


    public double convertCurrency(Map<String, Map<String, Double>> currencyGraph, String from,
                                  String to, double amount) {
        if (!currencyGraph.containsKey(from)) {
            throw new IllegalArgumentException("Currency not supported: " + from);
        }

        if (!currencyGraph.containsKey(to)) {
            throw new IllegalArgumentException("Currency not supported: " + to);
        }

        Map<String, Double> distances = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<Pair<String, Double>> pq = new PriorityQueue<>(Comparator.comparingDouble(Pair::getValue));

        for (String currency : currencyGraph.keySet()) {
            distances.put(currency, Double.MAX_VALUE);
        }

        distances.put(from, 1.0);

        pq.add(new Pair<>(from, 1.0));

        while (!pq.isEmpty()) {
            Pair<String, Double> current = pq.poll();
            String currentCurrency = current.getKey();
            double currentRate = current.getValue();

            if (visited.contains(currentCurrency)) {
                continue;
            }
            visited.add(currentCurrency);

            for (Map.Entry<String, Double> neighbor : currencyGraph.get(currentCurrency).entrySet()) {
                String neighborCurrency = neighbor.getKey();
                double edgeRate = neighbor.getValue();

                double newRate = currentRate * edgeRate;
                if (newRate < distances.get(neighborCurrency)) {
                    distances.put(neighborCurrency, newRate);
                    pq.add(new Pair<>(neighborCurrency, newRate));
                }
            }
        }

        if (distances.get(to) == Double.MAX_VALUE) {
            throw new IllegalArgumentException("No conversion path available from " + from + " to " + to);
        }

        return amount * distances.get(to);
    }

    private static class Pair<K, V> {
        K key;
        V value;

        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }


}
