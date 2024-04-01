package com.rkfcheung.portfolio.connection;

import com.rkfcheung.portfolio.model.Equity;
import com.rkfcheung.portfolio.model.Option;
import com.rkfcheung.portfolio.model.Security;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CombinedQuoteProvider implements QuoteProvider<Security> {

    private final UnderlyingQuoteProvider underlyingQuoteProvider;
    private final OptionQuoteProvider optionQuoteProvider;

    @Override
    public BigDecimal quote(final Security security) {
        if (security instanceof Option) {
            return optionQuoteProvider.quote((Option) security);
        }

        return underlyingQuoteProvider.quote((Equity) security);
    }
}
