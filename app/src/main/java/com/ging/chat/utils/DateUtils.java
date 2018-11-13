package com.ging.chat.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final String FORMAT_1 = "yyyy/MM/dd HH:mm:ss.SSS";

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static Date currentDate() {
        return new Date();
    }

    public static String currentDateString(String format) {
        return dateToString(format, currentDate());
    }

    public static String dateToString(String format, Date date) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static Date stringToDate(String format, String string) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date;
        try {
            date = dateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            date = null;
        }
        return date;
    }

}
