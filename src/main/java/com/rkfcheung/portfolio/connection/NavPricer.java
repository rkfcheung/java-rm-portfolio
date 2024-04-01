package com.rkfcheung.portfolio.connection;

import com.rkfcheung.portfolio.model.NavEntry;
import com.rkfcheung.portfolio.model.Option;
import com.rkfcheung.portfolio.model.Position;
import com.rkfcheung.portfolio.model.Security;
import com.rkfcheung.portfolio.source.Source;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequiredArgsConstructor
public class NavPricer {

    private final Map<String, NavEntry> positions = new LinkedHashMap<>();
    private final Map<String, List<Option>> options = new ConcurrentHashMap<>();
    private final AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.ZERO);
    private final Source source;
    private final CombinedQuoteProvider combinedQuoteProvider;

    @PostConstruct
    public void init() {
        BigDecimal nav = BigDecimal.ZERO;
        for (final Position position : source.load()) {
            final Security security = position.getSecurity();
            final BigDecimal price = combinedQuoteProvider.quote(security);
            final NavEntry entry = new NavEntry(position, price);
            positions.put(security.getSymbol(), new NavEntry(position, price));
            if (position.isOption()) {
                final Option option = (Option) security;
                final String underlying = option.getUnderlying();
                if (!options.containsKey(underlying)) {
                    options.put(underlying, new ArrayList<>());
                }
                options.get(underlying).add(option);
            }

            nav = nav.add(entry.getValue());
        }
        total.set(nav);

        log.debug("{} positions loaded with total portfolio value: {}", positions.size(), total.get());
    }

    @Nullable
    public NavEntry get(final String symbol) {
        return positions.get(symbol);
    }

    public List<NavEntry> load() {
        if (positions.isEmpty()) {
            init();
        }

        return new ArrayList<>(positions.values());
    }

    public BigDecimal total() {
        return total.get();
    }

    public void update(final String symbol) {
        doUpdate(symbol);

        final List<Option> optionList = options.get(symbol);
        if (!CollectionUtils.isEmpty(optionList)) {
            optionList.forEach(it -> doUpdate(it.getSymbol()));
        }
    }

    private void doUpdate(final String symbol) {
        final NavEntry entry = positions.get(symbol);
        if (entry == null) {
            return;
        }

        final BigDecimal oldValue = entry.getValue();
        final BigDecimal price = combinedQuoteProvider.quote(entry.getSecurity());
        entry.update(price);
        final BigDecimal newValue = entry.getValue();
        final BigDecimal newNav = total.get().subtract(oldValue).add(newValue);
        total.set(newNav);
    }
}
