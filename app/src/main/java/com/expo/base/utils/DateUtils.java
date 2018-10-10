package com.expo.base.utils;

import android.content.Context;

import com.expo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", Locale.getDefault() );

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
            Date date = sdf.parse( dateStr );
            Calendar target = Calendar.getInstance();
            target.setTimeInMillis( date.getTime() );
            int year = target.get( Calendar.YEAR );
            int month = target.get( Calendar.MONTH );
            int day = target.get( Calendar.DATE );
            int hours = target.get( Calendar.HOUR_OF_DAY );
            int minutes = target.get( Calendar.MINUTE );
            if (year != calendar.get( Calendar.YEAR )) {
                return dateStr;
            }
            int currDay = calendar.get( Calendar.DATE );
            if (month != calendar.get( Calendar.MONTH ) || currDay - day > 1) {
                return String.format( Locale.getDefault(), context.getString( R.string.same_month ), month + 1, day, hours, minutes );
            }
            if (currDay - day == 1) {
                return String.format( Locale.getDefault(), context.getString( R.string.yesterday ), hours, minutes );
            }
            if (calendar.get( Calendar.HOUR_OF_DAY ) != hours) {
                return String.format( Locale.getDefault(), context.getString( R.string.today ), hours, minutes );
            }
            if (calendar.get( Calendar.MINUTE ) != minutes) {
                return String.format( Locale.getDefault(), context.getString( R.string.within_one_hours ), calendar.get( Calendar.MINUTE ) - minutes );
            }
            return context.getString( R.string.a_moment_age );
        } catch (ParseException e) {
            return dateStr;
        }
    }
}
