package com.rkfcheung.portfolio.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

@Slf4j
public class FileUtil {

    private FileUtil() {
    }

    @Nullable
    public static File read(final @NonNull String filename) {
        final Path path = Paths.get(filename);
        final File file = path.toFile();
        if (file.exists()) {
            return file;
        }

        log.warn("File not found: {}", filename);

        return null;
    }

    @Nullable
    public static File readFromResource(final @Nullable String filename) {
        final String loc = CLASSPATH_URL_PREFIX + filename;
        try {
            return ResourceUtils.getFile(loc);
        } catch (FileNotFoundException e) {
            log.warn("File not found: {}", loc);

            return null;
        }
    }
}
