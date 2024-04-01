package com.rkfcheung.portfolio.config;

import com.rkfcheung.portfolio.source.CsvSource;
import com.rkfcheung.portfolio.source.Source;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

@Slf4j
@Configuration
public class SourceConfig {

    private final String input;
    private final ResourceLoader resourceLoader;

    public SourceConfig(
            final @Value("${portfolio.input}") String input,
            final ResourceLoader resourceLoader) {
        this.input = input;
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public Source source() {
        try {
            final File file = resourceLoader.getResource(input).getFile();
            log.debug("Reading input={} ...", file);

            return new CsvSource(file);
        } catch (IOException e) {
            log.error("Failed to read {}: {}", input, e.getMessage());

            throw new UncheckedIOException(e);
        }
    }
}
