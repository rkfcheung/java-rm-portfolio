package com.rkfcheung.portfolio.service;

import com.rkfcheung.portfolio.connection.NavPricer;
import com.rkfcheung.portfolio.model.QuoteUpdate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealTimeDashboard {

    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");
    private final static String NAV_ENTRY = "%-32s %16s %16s %16s";
    private final AtomicLong updated = new AtomicLong(1L);
    private final QuoteService quoteService;
    private final NavPricer navPricer;

    @PostConstruct
    public void init() {
        log.debug("Subscribing quote updates ...");
        quoteService.refresh().subscribe(this::display);
    }

    private void display(final @NonNull List<QuoteUpdate> updates) {
        log.info("## {} Market Data Update", updated.getAndIncrement());
        updates.forEach(update -> {
            final String symbol = update.getEquity().getSymbol();
            log.info("{} change to {}", symbol, DECIMAL_FORMAT.format(update.getPrice()));
            navPricer.update(symbol);
        });
        log.info("");
        log.info("## Portfolio");
        log.info("{}", String.format(NAV_ENTRY, "symbol", "price", "qty", "value"));
        navPricer.load().forEach(it -> log.info("{}", String.format(NAV_ENTRY,
                it.getSecurity().getSymbol(),
                DECIMAL_FORMAT.format(it.getPrice()),
                DECIMAL_FORMAT.format(it.getQty()),
                DECIMAL_FORMAT.format(it.getValue())
        )));
        log.info("");
        log.info("{}", String.format(NAV_ENTRY, "#Total portfolio", "", "", DECIMAL_FORMAT.format(navPricer.total())));
        log.info("");
    }
}
