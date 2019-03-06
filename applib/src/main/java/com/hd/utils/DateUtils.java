package com.hd.utils;

import com.hd.R;
import com.hd.base.HdApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Note：None
 * Created by lgd on 2018/12/24 17:02
 * E-Mail Address：986850427@qq.com
 */
public class DateUtils {
    public final static int HOUR = 3600;
    public final static int DAY = 24 * HOUR;
    public final static int WEEK = DAY * 7;

    public final static String patten = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_STR_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String DATE_STR_YYYY_MM_DD_E = "yyyy/MM/dd E";
    public final static String DATE_STR_YYYY_MM_DD = "yyyy-MM-dd";

    public final static SimpleDateFormat simpleDate = new SimpleDateFormat(patten, Locale.CHINESE);


    public static SimpleDateFormat getSimpleDate() {
        return simpleDate;
    }

    private final static HashMap<String, SimpleDateFormat> hashMap = new HashMap<>(5);

    static {
        hashMap.put(patten, simpleDate);
    }


    public static SimpleDateFormat getSimpleDateFormat(String patten) {
        SimpleDateFormat format = hashMap.get(patten);
        if (format == null) {
            format = new SimpleDateFormat(patten, Locale.CHINESE);
            hashMap.put(patten, format);
        }
        return format;
    }

    /***
     * 得到日期的指定样式
     * "yyyy-MM-dd HH:mm:ss"
     *
     * @param dateString
     * @param patten
     * @return
     */
    public static String getDateFormatString(String dateString, String patten) {
        if (dateString == null) return "";
        try {
            Date date = simpleDate.parse(dateString);
            return getSimpleDateFormat(patten).format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }

//    static String[]week= HdApp.getContext().getResources().getStringArray(R.a);

//    public static String getWeekString(Date date){
//        Calendar calendar=Calendar.getInstance(Locale.CHINESE);
//        return "";
//    }


    /***
     * 计算两者时间之差
     * @param timeStr1 时间1
     * @param timeStr2 时间2
     * @return 时间1减时间2
     */
    public static long getTimeDistance(String timeStr1, String timeStr2) {
        try {
            Date date1 = DateUtils.getSimpleDate().parse(timeStr1);// 2015-03-28
            Date date2 = DateUtils.getSimpleDate().parse(timeStr2);
            return date1.getTime() - date2.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
