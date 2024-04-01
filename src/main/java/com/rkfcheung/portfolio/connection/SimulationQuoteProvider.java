package com.rkfcheung.portfolio.connection;

import com.rkfcheung.portfolio.model.ExpireAfter;
import com.rkfcheung.portfolio.util.ValueUtil;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class SimulationQuoteProvider extends UnderlyingQuoteProvider {

    private final static double MIN_MU_VOL = 0.0;
    private final static double MAX_MU_VOL = 1.0;
    private final static double MIN_PRICE = 0.01;
    private final static double MAX_PRICE = 128.0;
    private final static double SCALING_FACTOR = 7_257_600.0;
    private final Random simulation = new Random();
    private final Map<String, Double> muStore = new ConcurrentHashMap<>();
    private final Map<String, ExpireAfter<BigDecimal>> priceStore = new ConcurrentHashMap<>();
    private final Map<String, BigDecimal> volStore = new ConcurrentHashMap<>();

    @Override
    public BigDecimal quote(final String symbol) {
        final ExpireAfter<BigDecimal> found = getCurrentPrice(symbol);
        final BigDecimal currentPrice = found.getData();
        if (!found.isExpired()) {
            return currentPrice;
        }

        final double s = currentPrice.doubleValue();
        final double deltaT = deltaT() / SCALING_FACTOR;
        final double mu = mu(symbol);
        final double sigma = volatility(symbol).doubleValue();
        final double delta = simulatePriceChange(s, mu, deltaT, sigma);
        final BigDecimal newPrice = currentPrice.add(BigDecimal.valueOf(delta));

        return save(symbol, newPrice).getData();
    }

    @Override
    public BigDecimal volatility(final String symbol) {
        final BigDecimal value = volStore.get(symbol);
        if (value != null) {
            return value;
        }

        final BigDecimal vol = BigDecimal.valueOf(random(MIN_MU_VOL, MAX_MU_VOL));
        volStore.put(symbol, vol);

        return vol;
    }

    private double deltaT() {
        return random(0.5, 2);
    }

    @NonNull
    private ExpireAfter<BigDecimal> getCurrentPrice(final String symbol) {
        final ExpireAfter<BigDecimal> value = priceStore.get(symbol);
        if (value != null) {
            return value;
        }

        return save(symbol, BigDecimal.valueOf(random(MIN_PRICE, MAX_PRICE)));
    }

    private double mu(final String symbol) {
        final Double found = muStore.get(symbol);
        if (found != null) {
            return found;
        }

        final double value = random(MIN_MU_VOL, MAX_MU_VOL);
        muStore.put(symbol, value);

        return value;
    }

    private double random(final double minVal, final double maxVal) {
        return minVal + (maxVal - minVal) * simulation.nextDouble();
    }

    @NonNull
    private ExpireAfter<BigDecimal> save(final String symbol, final BigDecimal price) {
        final double duration = deltaT() * 1_000;
        final ExpireAfter<BigDecimal> value = new ExpireAfter<>(ValueUtil.round(price), (long) duration);
        priceStore.put(symbol, value);

        return value;
    }

    private double simulatePriceChange(
            final double s,
            final double mu,
            final double deltaT,
            final double sigma
    ) {
        final double epsilon = simulation.nextGaussian(); // Standardized normal distribution

        return s * (mu * deltaT + sigma * epsilon * Math.sqrt(deltaT));
    }
}
