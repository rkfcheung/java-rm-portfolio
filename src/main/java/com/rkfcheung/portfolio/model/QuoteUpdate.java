package com.rkfcheung.portfolio.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class QuoteUpdate {

    Equity equity;
    BigDecimal price;
}
