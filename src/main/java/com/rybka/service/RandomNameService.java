package com.rybka.service;

import com.rybka.config.PropertyInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RandomNameService {

    public String generateFileName() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat(PropertyInfo.FILE_NAME_PATTERN);
        return dateFormat.format(date);
    }
}
