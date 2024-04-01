package com.rkfcheung.portfolio.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Equity implements Security {

    private String symbol;
}
