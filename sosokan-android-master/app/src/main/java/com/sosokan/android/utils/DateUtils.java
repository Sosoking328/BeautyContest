package com.sosokan.android.utils;


import android.content.Context;
import android.util.Log;

import com.sosokan.android.R;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static Date parseDate(String date) {
        try {
            SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            dateParse.setTimeZone(TimeZone.getTimeZone("GMT"));
            return dateParse.parse(date);
        } catch (ParseException e) {
            //No print
        }
        try {
            SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            dateParse.setTimeZone(TimeZone.getTimeZone("GMT"));
            return dateParse.parse(date);
        } catch (ParseException e) {
            //No print
        }
        try {
            SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            dateParse.setTimeZone(TimeZone.getTimeZone("GMT"));
            return dateParse.parse(date);
        } catch (ParseException e) {
            //No print
        }
        try {
            SimpleDateFormat dateParse = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
            return dateParse.parse(date);
        } catch (ParseException e) {
            //No print
        }
        return new Date();
    }



    public static final List<Long> times = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1));
    public static final List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");
    public static final List<String> timesStringChinise = Arrays.asList("年", "个月", "日", "小时", "分钟", "倅");
    public static String toDate(long times) {
        Date d = new Date(times);
        DateFormat df = new SimpleDateFormat("HH:mm, MMM d");
        String reportDate = df.format(d);
        return reportDate;
    }

    public static String toHours(long timeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(timeInMilis* 1000);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        Date d = new Date(timeInMilis * 1000);
        DateFormat df = new SimpleDateFormat(timeFormatString);
        String reportDate = df.format(d);

        return  reportDate;

    }
    public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;

    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;

    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;

    public final static long ONE_DAY = ONE_HOUR * 24;
    public static String toDuration(long time, Context ctx) {


        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now) {
           // Log.e("timeAgo ","time > now" + time);
           return null;
        }
        if(time <=0)
            time = Math.abs(time);

        //int dim = getTimeDistanceInMinutes(time);
        int dim = Math.round( curDate.getTime()/1000-time)/60;
        String timeAgo = null;
       // String timeAgo = null;

        if (dim == 0) {
            timeAgo = ctx.getResources().getString(R.string.date_util_term_less) + " " + ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_minute) ;
           // Log.e("timeAgo ",timeAgo);
            return timeAgo;
        } else if (dim == 1) {
            //Log.e("timeAgo ", String.valueOf(dim));
            return "1 " + ctx.getResources().getString(R.string.date_util_unit_minute)+ " " +ctx.getResources().getString(R.string.ago);
        } else if (dim >= 2 && dim <= 44) {
            timeAgo = dim + " " + ctx.getResources().getString(R.string.date_util_unit_minutes);
        } else if (dim >= 45 && dim <= 89) {
            timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + ctx.getResources().getString(R.string.date_util_term_an) + " " + ctx.getResources().getString(R.string.date_util_unit_hour);
        } else if (dim >= 90 && dim <= 1439) {
            timeAgo =  (Math.round(dim / 60)) + " " + ctx.getResources().getString(R.string.date_util_unit_hours);
        } else if (dim >= 1440 && dim <= 2519) {
            timeAgo = "1 " + ctx.getResources().getString(R.string.date_util_unit_day);
        } else if (dim >= 2520 && dim <= 43199) {
            timeAgo = (Math.round(dim / 1440)) + " " + ctx.getResources().getString(R.string.date_util_unit_days);
        } else if (dim >= 43200 && dim <= 86399) {
            timeAgo = ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_month);
        } else if (dim >= 86400 && dim <= 525599) {
            timeAgo = (Math.round(dim / 43200)) + " " + ctx.getResources().getString(R.string.date_util_unit_months);
        } else if (dim >= 525600 && dim <= 655199) {
            timeAgo = ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_year);
        } else if (dim >= 655200 && dim <= 914399) {
            timeAgo = ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_year);
        } else if (dim >= 914400 && dim <= 1051199) {
            timeAgo = " 2 " + ctx.getResources().getString(R.string.date_util_unit_years);
        } else {
            timeAgo =(Math.round(dim / 525600)) + " " + ctx.getResources().getString(R.string.date_util_unit_years);
        }
      //  Log.e("timeAgo ",timeAgo);
        return  timeAgo + " " +ctx.getResources().getString(R.string.ago);
    }

    public static String getTimeForMessage(long smsTimeInMilis)
    {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis* 1000);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";

        Log.e("smsTimeInMilis ", String.valueOf(smsTimeInMilis));

        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) ) {
            Date d = new Date(smsTimeInMilis * 1000);
            DateFormat df = new SimpleDateFormat(timeFormatString);
            String reportDate = df.format(d);
            Log.e("Today ", String.valueOf(reportDate));
            return "Today " + reportDate;//DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1  ){
            Date d = new Date(smsTimeInMilis * 1000);
            DateFormat df = new SimpleDateFormat(timeFormatString);
            String reportDate = df.format(d);
            Log.e("Yesterday ", String.valueOf(reportDate));
            return "Yesterday " + reportDate;//DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            Date d = new Date(smsTimeInMilis * 1000);
            DateFormat df = new SimpleDateFormat(dateTimeFormatString);
            String reportDate = df.format(d);
            Log.e("getTimeForMessage ", String.valueOf(reportDate));
            return  reportDate;
        } else {
            Date d = new Date(smsTimeInMilis * 1000);
            DateFormat df = new SimpleDateFormat(dateTimeFormatString);
            String reportDate = df.format(d);
            Log.e("getTimeForMessage ", String.valueOf(reportDate));
            return  reportDate;
        }
    }

    public static String calculateTimeAgo(String date, Context context) {
        long time = 0;
        try {
            //   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
            date = date.replace("T", " ");
            date = date.replace("Z", "");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
            Date past = format.parse(date);
            Date now = new Date();
            final long diff = now.getTime() - past.getTime();
            //      return toDuration(diff);
            if (diff < MINUTE_MILLIS) {
                return context.getString(R.string.just_now);
            } else if (diff < 2 * MINUTE_MILLIS) {
                return context.getString(R.string.min_ago);
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " " + context.getString(R.string.mins_ago);
            } else if (diff < 90 * MINUTE_MILLIS) {
                return context.getString(R.string.hour_ago);
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " " + context.getString(R.string.hours_ago);
            } else if (diff < 48 * HOUR_MILLIS) {
                return context.getString(R.string.yesterday);
            } else {
                return diff / DAY_MILLIS + " " + context.getString(R.string.days_ago);
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        return "";
    }



    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getTimeAgo(String date, Context ctx) {

        try {
            //   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
            date = date.replace("T", " ");
            date = date.replace("Z", "");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date parsedDate = format.parse(date);
            long time = parsedDate.getTime();

            Date curDate = currentDate();
            long now = curDate.getTime();
            if (time > now || time <= 0) {
                return null;
            }

            int dim = getTimeDistanceInMinutes(time);

            String timeAgo = null;

            if (dim == 0) {
                timeAgo = ctx.getResources().getString(R.string.date_util_term_less) + " " + ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_minute);
                return timeAgo;
            } else if (dim == 1) {
                return "1 " + ctx.getResources().getString(R.string.date_util_unit_minute);
            } else if (dim >= 2 && dim <= 44) {
                timeAgo = dim + " " + ctx.getResources().getString(R.string.date_util_unit_minutes);
            } else if (dim >= 45 && dim <= 89) {
                timeAgo = ctx.getResources().getString(R.string.date_util_prefix_about) + " " + ctx.getResources().getString(R.string.date_util_term_an) + " " + ctx.getResources().getString(R.string.date_util_unit_hour);
            } else if (dim >= 90 && dim <= 1439) {
                timeAgo =  (Math.round(dim / 60)) + " " + ctx.getResources().getString(R.string.date_util_unit_hours);
            } else if (dim >= 1440 && dim <= 2519) {
                timeAgo = "1 " + ctx.getResources().getString(R.string.date_util_unit_day);
            } else if (dim >= 2520 && dim <= 43199) {
                timeAgo = (Math.round(dim / 1440)) + " " + ctx.getResources().getString(R.string.date_util_unit_days);
            } else if (dim >= 43200 && dim <= 86399) {
                timeAgo = ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_month);
            } else if (dim >= 86400 && dim <= 525599) {
                timeAgo = (Math.round(dim / 43200)) + " " + ctx.getResources().getString(R.string.date_util_unit_months);
            } else if (dim >= 525600 && dim <= 655199) {
                timeAgo = ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_year);
            } else if (dim >= 655200 && dim <= 914399) {
                timeAgo = ctx.getResources().getString(R.string.date_util_term_a) + " " + ctx.getResources().getString(R.string.date_util_unit_year);
            } else if (dim >= 914400 && dim <= 1051199) {
                timeAgo = " 2 " + ctx.getResources().getString(R.string.date_util_unit_years);
            } else {
                timeAgo =(Math.round(dim / 525600)) + " " + ctx.getResources().getString(R.string.date_util_unit_years);
            }

            return timeAgo + " " + ctx.getResources().getString(R.string.date_util_suffix);
        } catch (Exception j) {
            j.printStackTrace();
        }

        return "";
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }


    public static long getDateInformation() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        long result = calendar.getTimeInMillis() / 1000;
        return result;
    }

    public static long getTimeFromString(String date) {
        long time = 0;
        try {
            //  System.out.print("getTimeFromString " + date);
            //   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
            date = date.replace("T", " ");
            date = date.replace("Z", "");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date past = format.parse(date);

            time = past.getTime() / 1000;
            return time;
        } catch (Exception j) {
            j.printStackTrace();
        }
        return time;
    }

    public static String getCurrentTime()
    {
        final Date currentTime = new Date();

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        /*TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);*/
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println("UTC time: " + sdf.format(currentTime));
        return  sdf.format(currentTime);
    }
    public static String getDayForAdvertise(String date)
    {

        date = date.replace("T", " ");
        date = date.replace("Z", "");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date past = format.parse(date);
            return  format.format(past);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";

    }

}
