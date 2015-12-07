package com.estafeta.estafetamovilv1.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class has methods concerning dates.
 */
public class DatesHelper {

    private static  String      TAG  =  "DatesHelper";

    /**
     * This method returns the number of days between an old and dated now.
     * @param lastUpdate Old date.
     * @return
     */
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
        Log.d("TAG",daysBetween+ " days of difference");
        return daysBetween;
    }

    /**
     * Change the date format.
     * @param date
     * @return
     */
    public static String getStringDate (Date date){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
    }

    /**
     * Change the date format.
     * @param date
     * @return
     */
    public static String getStringDateDays (Date date){
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * @deprecated
     * @param date
     * @return
     */
    public int getDateInMiliseconds(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (int)calendar.getTimeInMillis();
    }
}
