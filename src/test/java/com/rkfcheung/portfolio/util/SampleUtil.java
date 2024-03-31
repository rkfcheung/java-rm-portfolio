package com.rkfcheung.portfolio.util;

import java.io.File;

public class SampleUtil {

    private final static String SAMPLE_CSV = "source/csv/sample.csv";

    private SampleUtil() {
    }

    public static File readSampleCSV() {
        return FileUtil.readFromResource(SAMPLE_CSV);
    }
}
