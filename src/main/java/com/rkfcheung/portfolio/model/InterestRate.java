package com.rkfcheung.portfolio.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InterestRate implements Security {

    String symbol;
}
