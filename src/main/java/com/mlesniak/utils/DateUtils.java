package com.mlesniak.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static transient ThreadLocal<SimpleDateFormat> simpleDateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static transient ThreadLocal<SimpleDateFormat> simpleTimeFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    };


    public static String toDate(long timestamp) {
        return simpleDateFormat.get().format(new Date(timestamp));
    }

    public static String toTime(long timestamp) {
        return simpleTimeFormat.get().format(new Date(timestamp));
    }
}
