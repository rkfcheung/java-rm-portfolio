package com.rkfcheung.portfolio.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValueUtil {

    private ValueUtil() {
    }

    @Nullable
    public static BigDecimal asBigDecimal(final String value) {
        final Double doubleValue = asDouble(value);
        if (doubleValue == null) {
            return null;
        }

        return BigDecimal.valueOf(doubleValue);
    }


    @Nullable
    public static Double asDouble(final String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Nullable
    public static Integer asInt(final String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    @Nullable
    public static LocalDate asLocalDate(final String year, final String month) {
        if (ObjectUtils.isEmpty(year) || ObjectUtils.isEmpty(month)) {
            return null;
        }

        try {
            final String mmm = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
            final YearMonth yearMonth = YearMonth.parse(year + "-" + mmm, DateTimeFormatter.ofPattern("yyyy-MMM"));
            LocalDate localDate = yearMonth.atEndOfMonth();
            while (isWeekend(localDate)) {
                localDate = localDate.minusDays(1L);
            }

            return localDate;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    @Nullable
    public static Long asLong(final String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static boolean isWeekend(final @NonNull LocalDate value) {
        final DayOfWeek dayOfWeek = value.getDayOfWeek();

        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
