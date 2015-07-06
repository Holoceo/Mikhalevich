package com.redmadintern.mikhalevich.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alexander on 04.07.2015.
 */
public class DateUtil {
    private static final long MILLIS_IN_A_DAY = 86400000;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");

    /**
     * @param days the number of days since Jan. 1, 1970 GMT.
     * @return Calendar representation of the day
     */
    public static Calendar decodeDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(days*MILLIS_IN_A_DAY);
        return calendar;
    }

    /**
     * @param calendar Calendar representation of the day
     * @return the number of days since Jan. 1, 1970 GMT.
     */
    public static int encodeDate(Calendar calendar) {
        return (int)(calendar.getTimeInMillis() / MILLIS_IN_A_DAY);
    }

    /**
     * @param days the number of days since Jan. 1, 1970 GMT.
     * @return String representation of the day
     */
    public static String formatDate(int days) {
        Date date = new Date(days*MILLIS_IN_A_DAY);
        return dateFormat.format(date);
    }

}
