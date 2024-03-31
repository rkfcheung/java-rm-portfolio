package com.rkfcheung.portfolio.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValueUtilTest {

    @Test
    void testAsLocalDate() {
        final LocalDate date = ValueUtil.asLocalDate("2020", "OCT");
        assertNotNull(date);
    }
}