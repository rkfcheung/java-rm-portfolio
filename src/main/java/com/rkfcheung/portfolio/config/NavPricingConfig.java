package com.rkfcheung.portfolio.config;

import com.rkfcheung.portfolio.connection.CombinedQuoteProvider;
import com.rkfcheung.portfolio.connection.NavPricer;
import com.rkfcheung.portfolio.source.Source;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class NavPricingConfig {

    private final Source source;
    private final CombinedQuoteProvider combinedQuoteProvider;

    @Bean
    public NavPricer navPricer() {
        return new NavPricer(source, combinedQuoteProvider);
    }
}
