package hrv.band.app.ui.view.util;

import android.content.Context;
import android.os.Build;

import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 09.11.2017
 */


public class DateUtil {

    private DateUtil() {
        //hide implicit public constructor
    }
    /**
     * Formats the date and time for the measurement depending on user local.
     *
     * @param date the date to format.
     * @return the formatted date and time for the measurement depending on user local.
     */
    public static String formatTime(Context context, Date date) {
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        return timeFormat.format(date);
    }
    public static String formatDate( Context context, Date date, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern, getCurrentLocale(context));
        return dateFormat.format(date);
    }
    public static String formatDateMonth( Context context, Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MM.yyyy", getCurrentLocale(context));
        return dateFormat.format(date);
    }
    public static String formatDateTime( Context context, Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM", getCurrentLocale(context));
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        return dateFormat.format(date) + ", " + timeFormat.format(date);
    }
    public static Date getEndOfDay(Date date) {
        return DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DATE), -1);
    }

    public static Date getStartOfDay(Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }

    public static Date getStartOfWeek(Date date) {
        Calendar calendar = getCalenderFromDate(date);
        clearTime(calendar);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }

    private static void clearTime(Calendar calendar) {
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
    }

    public static Date getEndOfWeek(Date date) {
        Calendar calendar = getCalenderFromDate(getStartOfWeek(date));
        setMaxTime(calendar);
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        return calendar.getTime();
    }

    private static void setMaxTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    public static Date getStartOfMonth(Date date) {
        Calendar calendar = getCalenderFromDate(date);
        clearTime(calendar);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date getEndOfMonth(Date date) {
        Calendar calendar = getCalenderFromDate(getStartOfMonth(date));
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        setMaxTime(calendar);
        calendar.add(Calendar.DAY_OF_MONTH, daysInMonth - 1);
        return calendar.getTime();
    }

    private static Calendar getCalenderFromDate(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private static Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }
}
