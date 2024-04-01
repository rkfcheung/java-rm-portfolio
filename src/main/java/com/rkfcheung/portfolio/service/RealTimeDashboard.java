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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealTimeDashboard {

    private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");
    private final static String NAV_ENTRY = "%-32s %16s %16s %16s";
    private final AtomicBoolean connected = new AtomicBoolean();
    private final AtomicReference<String> summary = new AtomicReference<>();
    private final AtomicLong updated = new AtomicLong(1L);
    private final QuoteService quoteService;
    private final NavPricer navPricer;

    @PostConstruct
    public void init() {
        log.debug("Subscribing quote updates ...");
        quoteService.refresh().subscribe(this::display);
        connected.set(true);
    }

    public boolean isConnected() {
        return connected.get();
    }

    public String summary() {
        return summary.get();
    }

    public long updatedCount() {
        return updated.get();
    }

    private void display(final @NonNull List<QuoteUpdate> updates) {
        if (updates.isEmpty()) {
            return;
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("## ").append(updated.getAndIncrement()).append(" Market Data Update\n");
        updates.forEach(update -> {
            final String symbol = update.getEquity().getSymbol();
            sb.append(symbol).append(" change to ").append(DECIMAL_FORMAT.format(update.getPrice())).append("\n");
            navPricer.update(symbol);
        });
        sb.append("\n");
        sb.append("## Portfolio\n");
        sb.append(String.format(NAV_ENTRY, "symbol", "price", "qty", "value")).append("\n");
        navPricer.load().forEach(it ->
                sb.append(String.format(NAV_ENTRY,
                        it.getSecurity().getSymbol(),
                        DECIMAL_FORMAT.format(it.getPrice()),
                        DECIMAL_FORMAT.format(it.getQty()),
                        DECIMAL_FORMAT.format(it.getValue())
                )).append("\n")
        );
        sb.append("\n");
        sb.append(String.format(NAV_ENTRY, "#Total portfolio", "", "", DECIMAL_FORMAT.format(navPricer.total()))).append("\n");
        sb.append("\n");

        final String dashboard = sb.toString();
        System.out.println(dashboard);

        summary.set(dashboard);
    }
}
