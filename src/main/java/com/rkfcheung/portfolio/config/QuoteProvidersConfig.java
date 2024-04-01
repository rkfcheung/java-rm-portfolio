package com.rkfcheung.portfolio.config;

import com.rkfcheung.portfolio.connection.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QuoteProvidersConfig {

    @Bean
    public CombinedQuoteProvider combinedQuoteProvider(
            final UnderlyingQuoteProvider underlyingQuoteProvider,
            final OptionQuoteProvider optionQuoteProvider
    ) {
        return new CombinedQuoteProvider(underlyingQuoteProvider, optionQuoteProvider);
    }

    @Bean
    public OptionQuoteProvider optionQuoteProvider(
            final RiskFreeQuoteProvider riskFreeQuoteProvider,
            final UnderlyingQuoteProvider underlyingQuoteProvider
    ) {
        return new OptionPricingQuoteProvider(riskFreeQuoteProvider, underlyingQuoteProvider);
    }

    @Bean
    public RiskFreeQuoteProvider riskFreeQuoteProvider() {
        return new ConstantRiskFreeQuoteProvider();
    }

    @Bean
    public UnderlyingQuoteProvider underlyingQuoteProvider() {
        return new SimulationQuoteProvider();
    }
}
