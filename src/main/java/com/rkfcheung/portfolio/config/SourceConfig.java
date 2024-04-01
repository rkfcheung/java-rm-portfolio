package com.rkfcheung.portfolio.config;

import com.rkfcheung.portfolio.source.CsvSource;
import com.rkfcheung.portfolio.source.EmptySource;
import com.rkfcheung.portfolio.source.Source;
import com.rkfcheung.portfolio.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
            final File file;
            if (input.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
                file = resourceLoader.getResource(input).getFile();
            } else {
                file = FileUtil.read(input);
                if (file == null) {
                    throw new FileNotFoundException(input);
                }
            }
            log.debug("Reading input={} ...", file);

            return new CsvSource(file);
        } catch (IOException e) {
            log.error("Failed to read {}: {}", input, e.getMessage());

            return new EmptySource();
        }
    }
}
