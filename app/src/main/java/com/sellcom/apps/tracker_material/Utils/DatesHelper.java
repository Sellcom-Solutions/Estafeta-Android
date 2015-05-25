package com.sellcom.apps.tracker_material.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatesHelper {

    private static  String      TAG  =  "DatesHelper";

    public static long daysFromLastUpdate(Date lastUpdate) {

        Date now                    = new Date();
        Calendar cal_now            = Calendar.getInstance();
        cal_now.setTime(now);
        Calendar call_last          = Calendar.getInstance();
        call_last.setTime(lastUpdate);

        Calendar date = (Calendar) call_last.clone();
        long daysBetween = 0;
        while (date.before(cal_now)) {
            Log.d("TAG","1 day of difference");
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public static String getStringDate (Date date){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    public static String getStringDateDays (Date date){
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public int getDateInMiliseconds(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (int)calendar.getTimeInMillis();
    }
}
