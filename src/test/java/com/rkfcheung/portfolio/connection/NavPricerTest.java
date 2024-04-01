package com.rkfcheung.portfolio.connection;

import com.rkfcheung.portfolio.model.NavEntry;
import com.rkfcheung.portfolio.model.Position;
import com.rkfcheung.portfolio.model.Security;
import com.rkfcheung.portfolio.source.Source;
import com.rkfcheung.portfolio.util.PositionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class NavPricerTest {

    private final Source source = mock(Source.class);
    private final CombinedQuoteProvider combinedQuoteProvider = mock(CombinedQuoteProvider.class);
    private final NavPricer navPricer = new NavPricer(source, combinedQuoteProvider);

    @BeforeEach
    void setUp() {
        reset(source);
        reset(combinedQuoteProvider);

        when(source.available()).thenReturn(true);
        mockData();
    }

    @Test
    void testInitAndUpdate() {
        final List<NavEntry> entries = navPricer.load();
        assertEquals(6, entries.size());

        final Security applePut = entries.get(2).getSecurity();
        final NavEntry applePutEntry = navPricer.get(applePut.getSymbol());
        assertNotNull(applePutEntry);
        assertEquals(0, BigDecimal.valueOf(11_000.0).compareTo(applePutEntry.getValue()));
        assertEquals(0, BigDecimal.valueOf(-6_000.0).compareTo(navPricer.total()));

        when(combinedQuoteProvider.quote(applePut)).thenReturn(BigDecimal.valueOf(0.56));
        navPricer.update(applePut.getSymbol());
        assertEquals(0, BigDecimal.valueOf(11_200.0).compareTo(applePutEntry.getValue()));
        assertEquals(0, BigDecimal.valueOf(-5_800.0).compareTo(navPricer.total()));
    }

    @Test
    void testUnderlyingPriceChange() {
        navPricer.init();
        final List<NavEntry> entries = navPricer.load();
        final Security apple = entries.get(0).getSecurity();
        final Security appleCall = entries.get(1).getSecurity();
        final Security applePut = entries.get(2).getSecurity();

        when(combinedQuoteProvider.quote(apple)).thenReturn(BigDecimal.valueOf(109.0));
        when(combinedQuoteProvider.quote(appleCall)).thenReturn(BigDecimal.valueOf(5.5));
        when(combinedQuoteProvider.quote(applePut)).thenReturn(BigDecimal.valueOf(0.56));

        navPricer.update(apple.getSymbol());

        assertEquals(0, BigDecimal.valueOf(109_000.0).compareTo(entries.get(0).getValue()));
        assertEquals(0, BigDecimal.valueOf(-110_000.0).compareTo(entries.get(1).getValue()));
        assertEquals(0, BigDecimal.valueOf(11_200.0).compareTo(entries.get(2).getValue()));
        assertEquals(0, BigDecimal.valueOf(-5_800.0).compareTo(navPricer.total()));
    }

    void mockData() {
        final List<Position> positions = PositionHelper.load();
        when(source.load()).thenReturn(positions);

        when(combinedQuoteProvider.quote(positions.get(0).getSecurity())).thenReturn(BigDecimal.valueOf(110.0));
        when(combinedQuoteProvider.quote(positions.get(1).getSecurity())).thenReturn(BigDecimal.valueOf(5.55));
        when(combinedQuoteProvider.quote(positions.get(2).getSecurity())).thenReturn(BigDecimal.valueOf(0.55));
        when(combinedQuoteProvider.quote(positions.get(3).getSecurity())).thenReturn(BigDecimal.valueOf(450.0));
        when(combinedQuoteProvider.quote(positions.get(4).getSecurity())).thenReturn(BigDecimal.valueOf(27.25));
        when(combinedQuoteProvider.quote(positions.get(5).getSecurity())).thenReturn(BigDecimal.valueOf(6.35));
    }
}