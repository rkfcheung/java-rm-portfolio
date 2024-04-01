package com.rkfcheung.portfolio.connection;

import com.rkfcheung.portfolio.model.Equity;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SimulationQuoteProviderTest {

    final SimulationQuoteProvider quoteProvider = new SimulationQuoteProvider();

    @Test
    void testQuote() throws InterruptedException {
        final String symbol = "APPL";
        final Equity equity = Equity.builder().symbol(symbol).build();
        final BigDecimal currentPrice = quoteProvider.quote(equity);
        assertTrue(currentPrice.compareTo(BigDecimal.valueOf(0.01)) >= 0);
        assertTrue(currentPrice.compareTo(BigDecimal.valueOf(128.0)) <= 0);

        final Double mu = ReflectionTestUtils.invokeMethod(quoteProvider, "mu", symbol);
        assertNotNull(mu);

        Thread.sleep(2_000);
        final BigDecimal nextPrice = quoteProvider.quote(symbol);
        final BigDecimal delta = currentPrice.multiply(BigDecimal.valueOf(mu));
        assertTrue(nextPrice.compareTo(currentPrice.subtract(delta)) >= 0);
        assertTrue(nextPrice.compareTo(currentPrice.add(delta)) <= 0);
    }

    @Test
    void testVolatility() {
        final String symbol = "TELSA";
        final BigDecimal vol = quoteProvider.volatility(symbol);
        assertTrue(vol.compareTo(BigDecimal.ZERO) >= 0);

        assertEquals(vol, quoteProvider.volatility(symbol));
    }

    @Test
    void testRandomQuoteAndVolatility() {
        final Random random = new Random();
        final List<String> symbols = new ArrayList<>();
        for (int i = 0; i < 1_024; i++) {
            final String symbol = UUID.randomUUID().toString();
            if (random.nextBoolean()) {
                assertTrue(quoteProvider.volatility(symbol).compareTo(BigDecimal.ZERO) >= 0);
                assertTrue(quoteProvider.quote(symbol).compareTo(BigDecimal.ZERO) >= 0);
            } else {
                assertTrue(quoteProvider.quote(symbol).compareTo(BigDecimal.ZERO) >= 0);
                assertTrue(quoteProvider.volatility(symbol).compareTo(BigDecimal.ZERO) >= 0);
            }

            symbols.add(symbol);
        }

        symbols.forEach(symbol -> {
            if (random.nextBoolean()) {
                assertTrue(quoteProvider.volatility(symbol).compareTo(BigDecimal.ZERO) >= 0);
                assertTrue(quoteProvider.quote(symbol).compareTo(BigDecimal.ZERO) >= 0);
            } else {
                assertTrue(quoteProvider.quote(symbol).compareTo(BigDecimal.ZERO) >= 0);
                assertTrue(quoteProvider.volatility(symbol).compareTo(BigDecimal.ZERO) >= 0);
            }
        });
    }
}