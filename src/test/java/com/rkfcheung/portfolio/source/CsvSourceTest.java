package com.rkfcheung.portfolio.source;

import com.rkfcheung.portfolio.model.Option;
import com.rkfcheung.portfolio.model.Position;
import com.rkfcheung.portfolio.model.Security;
import com.rkfcheung.portfolio.util.FileUtil;
import com.rkfcheung.portfolio.util.SampleUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvSourceTest {

    final File sample = SampleUtil.readSampleCSV();
    final CsvSource source = new CsvSource(sample.getAbsolutePath());

    @Test
    void testAvailable() {
        assertTrue(source.available());

        assertFalse(new CsvSource(FileUtil.read("invalid-file.csv")).available());
    }

    @Test
    void testLoad() {
        final List<Position> positions = source.load();

        assertEquals(6, positions.size());

        final Position position = positions.get(1);
        assertTrue(position.isOption());

        final Option option = (Option) position.getSecurity();
        assertNotNull(option);
        assertEquals(0, option.getStrike().compareTo(BigDecimal.valueOf(110.0)));
    }

    @ParameterizedTest
    @org.junit.jupiter.params.provider.CsvSource({
            "AAPL, 1000",
            "GOOGL, 500",
            "MSFT, 750"
    })
    void testCsvParsing(final String symbol, final long qty) {
        final Position position = source.parse(symbol + "," + qty);
        assertNotNull(position);
        assertEquals(qty, position.getQty());

        final Security security = position.getSecurity();
        assertNotNull(security);
        assertEquals(symbol, security.getSymbol());
    }
}