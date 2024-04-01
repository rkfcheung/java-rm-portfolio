package com.rkfcheung.portfolio.connection;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SimulationQuoteProviderTest {

    final SimulationQuoteProvider quoteProvider = new SimulationQuoteProvider();

    @Test
    void testQuote() {
        final String symbol = "APPL";
        final BigDecimal currentPrice = quoteProvider.quote(symbol);
        assertTrue(currentPrice.compareTo(BigDecimal.valueOf(0.01)) >= 0);
        assertTrue(currentPrice.compareTo(BigDecimal.valueOf(100_000.0)) <= 0);

        final BigDecimal nextPrice = quoteProvider.quote(symbol);
        final BigDecimal delta = currentPrice.multiply(BigDecimal.valueOf(0.05));
        assertTrue(nextPrice.compareTo(currentPrice.subtract(delta)) >= 0);
        assertTrue(nextPrice.compareTo(currentPrice.add(delta)) <= 0);
    }
}