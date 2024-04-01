package com.rkfcheung.portfolio.connection;

import com.rkfcheung.portfolio.model.Option;
import com.rkfcheung.portfolio.model.OptionType;
import com.rkfcheung.portfolio.util.ValueUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OptionPricingQuoteProviderTest {

    private final RiskFreeQuoteProvider riskFreeQuoteProvider = new ConstantRiskFreeQuoteProvider();
    private final UnderlyingQuoteProvider underlyingQuoteProvider = mock(UnderlyingQuoteProvider.class);
    private final OptionPricingQuoteProvider quoteProvider = new OptionPricingQuoteProvider(riskFreeQuoteProvider, underlyingQuoteProvider);

    @BeforeEach
    void setUp() {
        when(underlyingQuoteProvider.quote(anyString())).thenReturn(BigDecimal.valueOf(123.45678));
        when(underlyingQuoteProvider.volatility(anyString())).thenReturn(BigDecimal.valueOf(0.314));
    }

    @Test
    void testQuote() {
        final String underlying = "GOOG";
        final BigDecimal underlyingPrice = underlyingQuoteProvider.quote(underlying);
        final BigDecimal strike = underlyingPrice.multiply(BigDecimal.valueOf(1.1));
        final Option call = prepare(underlying, strike, OptionType.C);
        final Option put = prepare(underlying, strike, OptionType.P);
        final double current = underlyingPrice.doubleValue();

        final BigDecimal callPrice = quoteProvider.quote(call);
        assertTrue(callPrice.compareTo(BigDecimal.valueOf(4.6)) > 0);
        assertTrue(callPrice.compareTo(BigDecimal.valueOf(4.7)) < 0);

        final BigDecimal putPrice = quoteProvider.quote(put);
        assertTrue(putPrice.compareTo(BigDecimal.valueOf(16.1)) > 0);
        assertTrue(putPrice.compareTo(BigDecimal.valueOf(16.2)) < 0);

        assertTrue(verifyPutCallParity(callPrice.doubleValue(), putPrice.doubleValue(), strike.doubleValue(), current));
    }

    @Test
    void testQuoteExpiredOption() {
        final Option option = Option.builder().maturity(LocalDate.now().minusMonths(1L)).build();
        assertEquals(BigDecimal.ZERO, quoteProvider.quote(option));
    }

    @Test
    void testSimulationQuotes() {
        final Random random = new Random();
        final SimulationQuoteProvider simulationQuoteProvider = new SimulationQuoteProvider();
        final OptionPricingQuoteProvider optionPricingQuoteProvider = new OptionPricingQuoteProvider(riskFreeQuoteProvider, simulationQuoteProvider);

        for (int i = 0; i < 1_024; i++) {
            final String underlying = UUID.randomUUID().toString();
            final BigDecimal underlyingPrice = simulationQuoteProvider.quote(underlying);
            final BigDecimal strike = underlyingPrice.multiply(BigDecimal.valueOf(random.nextDouble() * 2.0));
            final OptionType type = random.nextBoolean() ? OptionType.C : OptionType.P;
            final Option option = prepare(underlying, strike, type);
            final BigDecimal price = optionPricingQuoteProvider.quote(option);

            assertTrue(price.compareTo(BigDecimal.ZERO) >= 0);
            assertTrue(price.compareTo(BigDecimal.valueOf(Math.max(underlyingPrice.doubleValue(), strike.doubleValue()))) <= 0);
        }
    }

    private Option prepare(final String underlying, final BigDecimal strike, final OptionType type) {
        final LocalDate test = LocalDate.now().plusMonths(3L);
        final String year = String.valueOf(test.getYear());
        final String month = test.getMonth().name().substring(0, 3);
        final LocalDate maturity = ValueUtil.asLocalDate(year, month);
        final BigDecimal k = ValueUtil.round(strike);
        final String symbol = underlying + "-" + month + "-" + year + "-" + k + "-" + type;

        return Option.builder()
                .symbol(symbol)
                .underlying(underlying)
                .strike(k)
                .maturity(maturity)
                .type(type)
                .build();
    }

    private boolean verifyPutCallParity(
            final double callPrice,
            final double putPrice,
            final double strike,
            final double current
    ) {
        return Math.abs(callPrice + strike - current - putPrice) < 1.0;
    }
}