package com.rybka.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class FileUtils {

    public static final String FILE_NAME_PATTERN = "yyyy-MM-dd_hh-mm-ss";
    public static final String CSV_SUFFIX = ".csv";
    public static final String JSON_SUFFIX = ".json";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(FILE_NAME_PATTERN);

    public static String generateFileName(String fileSuffix) {
        LocalDateTime DATE_TIME = LocalDateTime.now();
        return DATE_FORMAT.format(DATE_TIME) + fileSuffix;
    }
}
