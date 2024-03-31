package com.rkfcheung.portfolio.source;

import com.rkfcheung.portfolio.model.PortfolioPosition;
import com.rkfcheung.portfolio.util.FileUtil;
import com.rkfcheung.portfolio.util.ValueUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CsvSource implements Source {

    private final File input;

    public CsvSource(final String input) {
        this(FileUtil.read(input));
    }

    @Override
    public boolean available() {
        return Optional.ofNullable(input).map(File::exists).orElse(false);
    }

    @Override
    public List<PortfolioPosition> load() {
        try {
            final List<String> lines = Files.readAllLines(input.toPath());
            final List<PortfolioPosition> result = new ArrayList<>();
            for (int i = 1; i < lines.size(); i++) {
                Optional.ofNullable(parse(lines.get(i)))
                        .ifPresent(result::add);
            }

            return result;
        } catch (IOException e) {
            log.warn("Unexpected IO Exception on {}: {}", input, e.getMessage());
        }

        return Collections.emptyList();
    }

    @Nullable
    public PortfolioPosition parse(final String line) {
        if (ObjectUtils.isEmpty(line)) {
            return null;
        }

        final String[] parts = line.split(",");
        if (parts.length < 2) {
            return null;
        }

        final Long qty = ValueUtil.asLong(parts[1]);
        if (qty == null) {
            return null;
        }

        return PortfolioPosition.of(parts[0], qty);
    }
}
