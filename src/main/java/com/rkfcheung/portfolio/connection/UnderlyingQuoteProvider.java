package com.rkfcheung.portfolio.connection;

import com.rkfcheung.portfolio.model.Equity;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public abstract class UnderlyingQuoteProvider implements QuoteProvider<Equity> {

    @Override
    public BigDecimal quote(final @NonNull Equity equity) {
        return quote(equity.getSymbol());
    }

    public abstract BigDecimal quote(final String symbol);

    public abstract BigDecimal volatility(final String symbol);
}
