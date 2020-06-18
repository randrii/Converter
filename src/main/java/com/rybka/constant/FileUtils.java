package com.rybka.constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtils {
    public static final String FILE_NAME_PATTERN = "yyyy-MM-dd-hhmmss";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(FILE_NAME_PATTERN);

    public static String generateFileName(String fileSuffix) {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(DATE_FORMAT) + '.' + fileSuffix;
    }
}
