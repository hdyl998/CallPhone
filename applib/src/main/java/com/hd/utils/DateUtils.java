package com.hd.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Note：None
 * Created by Liuguodong on 2018/12/24 17:02
 * E-Mail Address：986850427@qq.com
 */
public class DateUtils {


    public final static SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final static SimpleDateFormat simpleDateYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");


    public final static String DATE_STR_MM_DD_HH_MM = "MM-dd HH:mm";
    public final static String DATE_STR_YYYY_MM_DD_E = "yyyy/MM/dd E";


    public static SimpleDateFormat getSimpleDate() {
        return simpleDate;
    }
    public static SimpleDateFormat getSimpleDateYYYYMMDD() {
        return simpleDateYYYYMMDD;
    }

    /***
     * 得到日期的指定样式
     * "yyyy-MM-dd HH:mm:ss"
     *
     * @param dateString
     * @param format
     * @return
     */
    public static String getDateFormatString(String dateString, String format) {
        if (dateString == null) return "";
        try {
            Date date = simpleDate.parse(dateString);
            SimpleDateFormat sf = new SimpleDateFormat(format);
            return sf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }


}
