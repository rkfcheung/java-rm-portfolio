package com.rkfcheung.portfolio.model;

import liquibase.repackaged.org.apache.commons.lang3.EnumUtils;
import org.springframework.lang.Nullable;

import java.util.Arrays;

public enum OptionType {

    C, P;

    @Nullable
    public static OptionType fromValue(final String value) {
        return Arrays.stream(OptionType.values())
                .filter(it -> it.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }
}
