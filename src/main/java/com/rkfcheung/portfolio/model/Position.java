package com.rkfcheung.portfolio.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Position {

    Security security;
    Long qty;

    public static Position of(final String symbol, final long qty) {
        final Security security;
        final Option option = Option.of(symbol);
        if (option == null) {
            security = Equity.builder().symbol(symbol).build();
        } else {
            security = option;
        }

        return Position.builder()
                .security(security)
                .qty(qty)
                .build();
    }

    public boolean isEquity() {
        return security instanceof Equity;
    }

    public boolean isOption() {
        return security instanceof Option;
    }
}
