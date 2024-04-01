package com.rkfcheung.portfolio.connection;

import com.rkfcheung.portfolio.model.InterestRate;

import java.math.BigDecimal;

public class ConstantRiskFreeQuoteProvider extends RiskFreeQuoteProvider {

    private final static BigDecimal RISK_FREE = BigDecimal.valueOf(0.02);

    @Override
    public BigDecimal quote(final InterestRate interestRate) {
        return RISK_FREE;
    }
}
