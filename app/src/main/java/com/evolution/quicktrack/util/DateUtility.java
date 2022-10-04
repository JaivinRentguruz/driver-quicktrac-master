package com.evolution.quicktrack.util;

import com.evolution.quicktrack.constants.LogCustom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtility {
    private  static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static SimpleDateFormat dateFormat;

    public static Date getCurrentDateTime(){
        return Calendar.getInstance().getTime();
    }

    public static void initFormat(){
        dateFormat =new SimpleDateFormat(DATE_FORMAT);
    }

    public static Date formatDate(String strDate) throws ParseException {
        return dateFormat.parse(strDate);
    }

    public static boolean isValidDateDifference(String startDateString) throws ParseException {
        long duration  = DateUtility.getCurrentDateTime().getTime() - DateUtility.formatDate(startDateString).getTime();
        long diffInDay = TimeUnit.DAYS.convert(duration, TimeUnit.MILLISECONDS);
        if (diffInDay > 4) {
            LogCustom.logd("xxxxxx","diffrnce > 5"+diffInDay);
            return false;
        } else {
            LogCustom.logd("xxxxxx","diffrnce"+diffInDay);
            return true;
        }
    }
}
