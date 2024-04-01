package com.rkfcheung.portfolio.connection;

import com.rkfcheung.portfolio.model.Security;

import java.math.BigDecimal;

public interface QuoteProvider<T extends Security> {

    BigDecimal quote(final T security);
}
