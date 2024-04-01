package com.rkfcheung.portfolio.model;

import com.rkfcheung.portfolio.util.ValueUtil;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@Entity
public class Option implements Security {

    @Id
    private UUID id;

    private String symbol;

    private String underlying;

    private BigDecimal strike;

    private LocalDate maturity;

    private OptionType type;

    @Nullable
    public static Option of(final String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }

        final String[] parts = value.split("-");
        if (parts.length != 5) {
            return null;
        }

        final String underlying = parts[0];
        if (ObjectUtils.isEmpty(underlying)) {
            return null;
        }

        final String month = parts[1];
        final String year = parts[2];
        final LocalDate maturity = ValueUtil.asLocalDate(year, month);
        if (maturity == null) {
            return null;
        }

        final BigDecimal strike = ValueUtil.asBigDecimal(parts[3]);
        if (strike == null) {
            return null;
        }

        final OptionType type = OptionType.fromValue(parts[4]);
        if (type == null) {
            return null;
        }

        return Option.builder()
                .symbol(value)
                .underlying(underlying)
                .strike(strike)
                .maturity(maturity)
                .type(type)
                .build();
    }

    public boolean isExpired() {
        if (maturity == null) {
            return true;
        }

        return LocalDate.now().isAfter(maturity);
    }
}
