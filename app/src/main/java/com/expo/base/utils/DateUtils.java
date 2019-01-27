package com.expo.base.utils;

import android.content.Context;

import com.blankj.utilcode.util.TimeUtils;
import com.expo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    /**
     * 格式化日期
     *
     * @param context
     * @param calendar
     * @param dateStr
     * @return
     */
    public static String formatTime(Context context, Calendar calendar, String dateStr) {
        try {
            Date date = sdf.parse(dateStr);
            Calendar target = Calendar.getInstance();
            target.setTimeInMillis(date.getTime());
            int year = target.get(Calendar.YEAR);
            int month = target.get(Calendar.MONTH);
            int day = target.get(Calendar.DATE);
            int hours = target.get(Calendar.HOUR_OF_DAY);
            int minutes = target.get(Calendar.MINUTE);
            if (year != calendar.get(Calendar.YEAR)) {
                return dateStr;
            }
            int currDay = calendar.get(Calendar.DATE);
            if (month != calendar.get(Calendar.MONTH) || currDay - day > 1) {
                return String.format(Locale.getDefault(), context.getString(R.string.same_month), month + 1, day, hours, minutes);
            }
            if (currDay - day == 1) {
                return String.format(Locale.getDefault(), context.getString(R.string.yesterday), hours, minutes);
            }
            if (calendar.get(Calendar.HOUR_OF_DAY) != hours) {
                return String.format(Locale.getDefault(), context.getString(R.string.today), hours, minutes);
            }
            if (calendar.get(Calendar.MINUTE) != minutes) {
                return String.format(Locale.getDefault(), context.getString(R.string.within_one_hours), calendar.get(Calendar.MINUTE) - minutes);
            }
            return context.getString(R.string.a_moment_age);
        } catch (ParseException e) {
            return dateStr;
        }
    }

    /**
     * 相加时间为
     *
     * @param time  原时间
     * @param year  1：加一年，0：年数不变，-1：减一年
     * @param month 1：加一月，0：年数不变，-1：减一月
     * @param day   1：加一日，0：年数不变，-1：减一日
     * @return 新的时间戳
     */
    public static long getDate(long time, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.YEAR, year);
        calendar.add(Calendar.MONTH, month);
        time = calendar.getTimeInMillis();
        time += day * 24 * 3600 * 1000;
        return time;
    }

    /**
     * @param time 时间
     * @return 返回时间所在月的天数
     */
    public static int getMonthDays(long time) {
        String monthStr = TimeUtils.millis2String(time, new SimpleDateFormat("MM"));
        int month = Integer.valueOf(monthStr);
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            if (isLeapYear(time)) return 29;
            return 28;
        }
    }

    /**
     * @param time 时间
     * @return 返回时间所在年的天数
     */
    public static long getYearDays(long time) {
        if (isLeapYear(time)) return 366;
        return 365;
    }

    public static boolean isLeapYear(long time) {
        String yearStr = TimeUtils.millis2String(time, new SimpleDateFormat("yyyy"));
        int year = Integer.valueOf(yearStr);
        if (year % 400 == 0) return true;
        if (year % 4 == 0 && year % 100 != 0) return true;
        return false;
    }
}
