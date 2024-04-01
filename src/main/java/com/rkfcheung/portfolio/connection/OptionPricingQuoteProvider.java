package com.rkfcheung.portfolio.connection;

import com.rkfcheung.portfolio.model.InterestRate;
import com.rkfcheung.portfolio.model.Option;
import com.rkfcheung.portfolio.model.OptionType;
import com.rkfcheung.portfolio.util.ValueUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Slf4j
@RequiredArgsConstructor
public class OptionPricingQuoteProvider extends OptionQuoteProvider {

    private final static InterestRate RISK_FREE_TICKER = InterestRate.builder()
            .symbol("RISK_FREE_RATE")
            .build();
    private final RiskFreeQuoteProvider riskFreeQuoteProvider;
    private final UnderlyingQuoteProvider underlyingQuoteProvider;

    @Override
    public BigDecimal quote(final @NonNull Option option) {
        if (option.isExpired()) {
            return BigDecimal.ZERO;
        }

        final String underlying = option.getUnderlying();
        final double s = currentUnderlyingPrice(underlying).doubleValue();
        final double k = option.getStrike().doubleValue();
        final double r = riskFreeRate().doubleValue();
        final double t = expiredInYear(option.getMaturity());
        final double sigma = underlyingQuoteProvider.volatility(underlying).doubleValue();
        log.info("Pricing {} with [s={}, r={}, t={}, sigma={}] ...", option, s, r, t, sigma);

        final double modelPrice;
        if (option.getType() == OptionType.C) {
            modelPrice = calcCallPrice(s, k, r, t, sigma);
        } else {
            modelPrice = calcPutPrice(s, k, r, t, sigma);
        }

        return ValueUtil.round(BigDecimal.valueOf(Math.max(modelPrice, 0.0)));
    }

    private double calcCallPrice(
            final double s,
            final double k,
            final double r,
            final double t,
            final double sigma
    ) {
        final Intermediate d1d2 = calcD1D2(s, k, r, t, sigma);
        final double d1 = d1d2.getD1();
        final double d2 = d1d2.getD2();

        return s * cumulativeDistributionFunction(d1) - k * Math.exp(-r * t) * cumulativeDistributionFunction(d2);
    }

    @NonNull
    private Intermediate calcD1D2(
            final double s,
            final double k,
            final double r,
            final double t,
            final double sigma
    ) {
        final double d1 = (Math.log(s / k) + (r + 0.5 * sigma * sigma) * t) / (sigma * Math.sqrt(t));
        final double d2 = d1 - sigma * Math.sqrt(t);

        return Intermediate.builder().d1(d1).d2(d2).build();
    }

    private double calcPutPrice(
            final double s,
            final double k,
            final double r,
            final double t,
            final double sigma
    ) {
        final Intermediate d1d2 = calcD1D2(s, k, r, t, sigma);
        final double d1 = d1d2.getD1();
        final double d2 = d1d2.getD2();

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
        final Period period = Period.between(LocalDate.now(), maturity);

        return period.getYears() + period.getMonths() / 12.0 + period.getDays() / 365.0;
    }

    private BigDecimal riskFreeRate() {
        return riskFreeQuoteProvider.quote(RISK_FREE_TICKER);
    }

    @Getter
    @Builder
    private static class Intermediate {
        private final Double d1;
        private final Double d2;
    }
}
