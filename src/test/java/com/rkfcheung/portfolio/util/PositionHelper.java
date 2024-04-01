package com.rkfcheung.portfolio.util;

import com.rkfcheung.portfolio.model.Option;
import com.rkfcheung.portfolio.model.OptionType;
import com.rkfcheung.portfolio.model.Position;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class PositionHelper {

    private PositionHelper() {
    }

    @NonNull
    public static List<Position> load() {
        final String apple = "AAPL";
        final String tesla = "TSLA";

        final LocalDate maturity1 = LocalDate.now().plusMonths(6L);
        final LocalDate maturity2 = LocalDate.now().plusMonths(7L);
        final LocalDate maturity3 = LocalDate.now().plusMonths(8L);

        final Option appleCall = PositionHelper.prepare(
                apple, maturity1.getMonth(), maturity1.getYear(), BigDecimal.valueOf(110.0), OptionType.C
        );
        final Option applePut = PositionHelper.prepare(
                apple, maturity1.getMonth(), maturity1.getYear(), BigDecimal.valueOf(110.0), OptionType.P
        );
        final Option teslaCall = PositionHelper.prepare(
                tesla, maturity2.getMonth(), maturity2.getYear(), BigDecimal.valueOf(400.0), OptionType.C
        );
        final Option teslaPut = PositionHelper.prepare(
                tesla, maturity3.getMonth(), maturity3.getYear(), BigDecimal.valueOf(400.0), OptionType.P
        );

        return Arrays.asList(
                Position.of(apple, 1_000L),
                Position.builder().security(appleCall).qty(-20_000L).build(),
                Position.builder().security(applePut).qty(20_000L).build(),
                Position.of(tesla, -500L),
                Position.builder().security(teslaCall).qty(10_000L).build(),
                Position.builder().security(teslaPut).qty(-10_000L).build()
        );
    }

    public static Option prepare(
            final String underlying,
            final @NonNull Month maturityMonth,
            final int maturityYear,
            final BigDecimal strike,
            final OptionType type
    ) {
        final String year = String.valueOf(maturityYear);
        final String month = maturityMonth.name().substring(0, 3);
        final LocalDate maturity = ValueUtil.asLocalDate(year, month);
        final BigDecimal k = ValueUtil.round(strike);
        final String symbol = underlying + "-" + month + "-" + year + "-" + k + "-" + type;

        return Option.builder()
                .symbol(symbol)
                .underlying(underlying)
                .strike(k)
                .maturity(maturity)
                .type(type)
                .build();
    }
}
