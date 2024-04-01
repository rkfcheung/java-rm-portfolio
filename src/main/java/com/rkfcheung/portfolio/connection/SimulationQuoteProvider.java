package com.rkfcheung.portfolio.connection;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class SimulationQuoteProvider implements QuoteProvider {

    private final static double MIN_PRICE = 0.01;
    private final static double MAX_PRICE = 100_000.0;
    private final static double PRICE_MOVE = 0.05;
    private final Random simulation = new Random();
    private final Map<String, BigDecimal> store = new ConcurrentHashMap<>();

    @Override
    public BigDecimal quote(final String symbol) {
        final BigDecimal found = store.get(symbol);
        final BigDecimal currentPrice = getCurrentPrice(symbol);
        if (found == null) {
            return currentPrice;
        }

        final double delta = currentPrice.doubleValue() * PRICE_MOVE * (simulation.nextDouble() * 2 - 1);
        final BigDecimal newPrice = currentPrice.add(BigDecimal.valueOf(delta));
        store.put(symbol, newPrice);

        return newPrice;
    }

    @NonNull
    private BigDecimal getCurrentPrice(final String symbol) {
        final BigDecimal value = store.get(symbol);
        if (value != null) {
            return value;
        }

        final BigDecimal price = BigDecimal.valueOf(MIN_PRICE + (MAX_PRICE - MIN_PRICE) * simulation.nextDouble());
        store.put(symbol, price);

        return price;
    }
}
