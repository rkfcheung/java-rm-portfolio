package com.rkfcheung.portfolio.service;

import com.rkfcheung.portfolio.connection.UnderlyingQuoteProvider;
import com.rkfcheung.portfolio.model.Equity;
import com.rkfcheung.portfolio.model.Position;
import com.rkfcheung.portfolio.model.QuoteUpdate;
import com.rkfcheung.portfolio.source.Source;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuoteService {

    private final Map<Equity, BigDecimal> prices = new ConcurrentHashMap<>();
    private final Source source;
    private final UnderlyingQuoteProvider underlyingQuoteProvider;

    @PostConstruct
    public void init() {
        if (source.available()) {
            source.load().stream().filter(Position::isEquity).forEach(it -> {
                final Equity equity = (Equity) it.getSecurity();
                final BigDecimal price = underlyingQuoteProvider.quote(equity);
                prices.put(equity, price);
            });
        } else {
            log.warn("Source isn't available.");
        }
    }

    public Flux<List<QuoteUpdate>> refresh() {
        return Flux.interval(Duration.ofMillis(500L))
                .flatMap(it -> Mono.fromCallable(this::getUpdates));
    }

    private List<QuoteUpdate> getUpdates() {
        return prices.entrySet().stream()
                .map(entry -> {
                    final Equity equity = entry.getKey();
                    final BigDecimal currentPrice = entry.getValue();

                    final BigDecimal newPrice = underlyingQuoteProvider.quote(equity);
                    if (currentPrice.compareTo(newPrice) != 0) {
                        return QuoteUpdate.builder().equity(equity).price(newPrice).build();
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
