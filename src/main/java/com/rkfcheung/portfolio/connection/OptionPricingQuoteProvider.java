package com.rkfcheung.portfolio.connection;

import com.rkfcheung.portfolio.model.InterestRate;
import com.rkfcheung.portfolio.model.Option;
import com.rkfcheung.portfolio.model.OptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
public class OptionPricingQuoteProvider extends OptionQuoteProvider {

    private final static InterestRate RISK_FREE_TICKER = InterestRate.builder()
            .symbol("RISK_FREE_RATE")
            .build();
    private final RiskFreeQuoteProvider riskFreeQuoteProvider;
    private final UnderlyingQuoteProvider underlyingQuoteProvider;

    @Override
    public BigDecimal quote(final @NonNull Option option) {
        final double t = expiredInYear(option.getMaturity());
        if (t == 0.0) {
            return BigDecimal.ZERO;
        }

        final double strike = option.getStrike().doubleValue();
        final String underlying = option.getUnderlying();
        final double k = currentUnderlyingPrice(underlying).doubleValue();
        final double r = riskFreeRate().doubleValue();
        final double sigma = underlyingQuoteProvider.volatility(underlying).doubleValue();

        final double modelPrice;
        if (option.getType() == OptionType.C) {
            modelPrice = calcCallPrice(strike, k, r, t, sigma);
        } else {
            modelPrice = calcPutPrice(strike, k, r, t, sigma);
        }

        return BigDecimal.valueOf(modelPrice);
    }

    public double calcCallPrice(
            final double s,
            final double k,
            final double r,
            final double t,
            final double sigma
    ) {
        final Pair<Double, Double> d1d2 = calcD1D2(s, k, r, t, sigma);
        final double d1 = d1d2.getFirst();
        final double d2 = d1d2.getSecond();

        return s * cumulativeDistributionFunction(d1) - k * Math.exp(-r * t) * cumulativeDistributionFunction(d2);
    }

    public Pair<Double, Double> calcD1D2(
            final double s,
            final double k,
            final double r,
            final double t,
            final double sigma
    ) {
        final double d1 = (Math.log(s / k) + (r + 0.5 * sigma * sigma) * t) / (sigma * Math.sqrt(t));
        final double d2 = d1 - sigma * Math.sqrt(t);

        return Pair.of(d1, d2);
    }

    public double calcPutPrice(
            final double s,
            final double k,
            final double r,
            final double t,
            final double sigma
    ) {
        final Pair<Double, Double> d1d2 = calcD1D2(s, k, r, t, sigma);
        final double d1 = d1d2.getFirst();
        final double d2 = d1d2.getSecond();

        return k * Math.exp(-r * t) * cumulativeDistributionFunction(-d2) - s * cumulativeDistributionFunction(-d1);
    }

    private double cumulativeDistributionFunction(final double x) {
        return 0.5 * (1 + erf(x / Math.sqrt(2)));
    }

    private BigDecimal currentUnderlyingPrice(final String underlying) {
        return underlyingQuoteProvider.quote(underlying);
    }

    // Error function approximation (using Taylor series)
    private double erf(final double x) {
        final double t = 1.0 / (1.0 + 0.5 * Math.abs(x));
        final double tau = t * Math.exp(-x * x - 1.26551223 + t * (1.00002368 + t * (0.37409196 + t * (0.09678418 +
                t * (-0.18628806 + t * (0.27886807 + t * (-1.13520398 + t * (1.48851587 + t * (-0.82215223 +
                        t * 0.17087277)))))))));

        return x >= 0 ? 1 - tau : tau - 1;
    }

    private double expiredInYear(final LocalDate maturity) {
        final LocalDate currentDate = LocalDate.now();
        if (currentDate.isAfter(maturity)) {
            return 0.0;
        }

        final Period period = Period.between(currentDate, maturity);

        return period.getYears() + period.getMonths() / 12.0 + period.getDays() / 365.0;
    }

    private BigDecimal riskFreeRate() {
        return riskFreeQuoteProvider.quote(RISK_FREE_TICKER);
    }
}
