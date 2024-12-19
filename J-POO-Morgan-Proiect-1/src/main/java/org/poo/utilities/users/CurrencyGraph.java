package org.poo.utilities.users;

import org.poo.fileio.ExchangeInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Comparator;

/**
 * Represents a graph structure for currency exchange rates.
 */
public class CurrencyGraph {
    private Map<String, Map<String, Double>> currencyGraph;

    /**
     * Builds the currency graph based on the provided exchange data.
     * Each exchange rate is represented as a directed edge in the graph.
     *
     * @param exchangeData the list of exchange rates to populate the graph.
     */
    public void buildCurrencyGraph(final ArrayList<ExchangeInput> exchangeData) {
        currencyGraph = new HashMap<>();

        for (ExchangeInput exchange : exchangeData) {
            String from = exchange.getFrom();
            String to = exchange.getTo();
            double rate = exchange.getRate();

            currencyGraph.putIfAbsent(from, new HashMap<>());
            currencyGraph.get(from).put(to, rate);

            currencyGraph.putIfAbsent(to, new HashMap<>());
            currencyGraph.get(to).put(from, 1 / rate);
        }
    }

    /**
     * Converts an amount of money from one currency to another using the exchange rates.
     * Uses Dijkstra's algorithm to find the shortest path in terms of rates.
     *
     * @param from the source currency.
     * @param to the target currency.
     * @param amount the amount of money to convert.
     * @return the converted amount in the target currency.
     * @throws IllegalArgumentException if either currency is not supported
     *                                  or if there is no conversion path available.
     */
    public double convertCurrency(final String from, final String to, final double amount) {
        if (!currencyGraph.containsKey(from)) {
            throw new IllegalArgumentException("Currency not supported: " + from);
        }

        if (!currencyGraph.containsKey(to)) {
            throw new IllegalArgumentException("Currency not supported: " + to);
        }

        Map<String, Double> distances = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<Pair<String, Double>> pq =
                new PriorityQueue<>(Comparator.comparingDouble(Pair::getValue));

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

            for (Map.Entry<String, Double> neighbor : currencyGraph
                    .get(currentCurrency).entrySet()) {
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
            throw new IllegalArgumentException("No conversion path available from "
                    + from + " to " + to);
        }

        return amount * distances.get(to);
    }

    /**
     * A simple generic pair class to hold key-value pairs.
     *
     * @param <K> the type of the key.
     * @param <V> the type of the value.
     */
    private static class Pair<K, V> {
        private final K key;
        private final V value;

        /**
         * Constructs a Pair with the specified key and value.
         *
         * @param key the key of the pair.
         * @param value the value of the pair.
         */
        Pair(final K key, final V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Gets the key of the pair.
         *
         * @return the key.
         */
        public K getKey() {
            return key;
        }

        /**
         * Gets the value of the pair.
         *
         * @return the value.
         */
        public V getValue() {
            return value;
        }
    }
}
