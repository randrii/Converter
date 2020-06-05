package com.rybka.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FileUtils {

    public static final String FILE_NAME_PATTERN = "yyyy-MM-dd_hh-mm-ss";
    public static final String CSV_SUFFIX = ".csv";
    public static final String JSON_SUFFIX = ".json";

    public static String generateFileName(String fileSuffix) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat(FILE_NAME_PATTERN);
        return dateFormat.format(date) + fileSuffix;
    }
}
