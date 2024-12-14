package org.poo.utilities.users;

import org.poo.fileio.ExchangeInput;

import java.util.*;

public class CurrencyGraph {
    private Map<String, Map<String, Double>> currencyGraph;

    public void buildCurrencyGraph(ArrayList<ExchangeInput> exchangeData) {
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

    public double convertCurrency(String from, String to, double amount) {
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