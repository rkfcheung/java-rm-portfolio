package com.rkfcheung.portfolio.connection;

import java.math.BigDecimal;

public interface QuoteProvider {

    BigDecimal quote(final String symbol);
}
