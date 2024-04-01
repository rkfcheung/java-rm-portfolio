package com.rkfcheung.portfolio.source;

import com.rkfcheung.portfolio.model.Position;
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
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@RequiredArgsConstructor
public class CsvSource implements Source {

    private final List<Position> positions = new CopyOnWriteArrayList<>();

    private final File input;

    public CsvSource(final String input) {
        this(FileUtil.read(input));
    }

    @Override
    public boolean available() {
        return Optional.ofNullable(input).map(File::exists).orElse(false);
    }

    @Override
    public List<Position> load() {
        if (!positions.isEmpty()) {
            return positions;
        }

        try {
            final List<String> lines = Files.readAllLines(input.toPath());
            if (lines.isEmpty()) {
                log.warn("Empty input found: {}", input);

                return Collections.emptyList();
            }

            final List<Position> result = new ArrayList<>();
            final String header = lines.get(0).trim();
            log.debug("Loading {} with header {} ...", input, ValueUtil.truncate(header, 128));
            for (int i = 1; i < lines.size(); i++) {
                final String line = lines.get(i);
                final Position position = parse(line);
                if (position == null) {
                    log.warn("Failed to parse: {}", ValueUtil.truncate(line, 256));
                } else {
                    result.add(position);
                }
            }
            positions.addAll(result);

            return result;
        } catch (IOException e) {
            log.warn("Unexpected IO Exception on {}: {}", input, e.getMessage());
        }

        return Collections.emptyList();
    }

    @Nullable
    public Position parse(final String line) {
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

        return Position.of(parts[0].trim().toUpperCase(), qty);
    }
}
