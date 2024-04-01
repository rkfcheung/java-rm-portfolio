package com.rkfcheung.portfolio.service;

import com.rkfcheung.portfolio.connection.NavPricer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealTimeDashboard {

    private final QuoteService quoteService;
    private final NavPricer navPricer;

    @PostConstruct
    public void init() {
        log.info("Subscribing quote updates ...");
        quoteService.refresh().subscribe(update -> {
            log.info("{}", update);
            navPricer.update(update.getEquity().getSymbol());
            log.info("Total NAV: {}", navPricer.total());
        });
    }
}
