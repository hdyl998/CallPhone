package com.callphone.client.utils;

import com.hd.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Note：None
 * Created by Liuguodong on 2019/2/28 17:49
 * E-Mail Address：986850427@qq.com
 */
public class StringUtil {

    /**
     * @param str
     * @return String
     * @throws PatternSyntaxException
     * @throws
     * @方法名: stringpassword
     * @功能描述:密码格式
     */
    public static String stringPassword(String str) throws PatternSyntaxException {
        // 只允许字母、数字 和 !#$\%^&*.~
        String regEx = "[^a-zA-Z0-9!#$%^@&*.~]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    /***
     * 显示规则  1当天的消息以5分钟为一个跨度的显示时间;
     * 2消息超过1天/小于1周,显示星期+收发消息的时间
     * 3消息大于1周,显示手机收发时间的日期
     * @param timeStr
     * @return
     */
    public static String getPostTime(String timeStr) {
        Date date;
        try {
            date = DateUtils.getSimpleDate().parse(timeStr);// 2015-03-28
            // 15:37:58
        } catch (Exception e) {
            return timeStr;
        }
        long timeStrLong = date.getTime();
        //得到服务器现在的时间
        Date now = new Date();
        long currentTime = now.getTime();


        long distance = (currentTime - timeStrLong) / 1000;// 得到秒数


        //当天的返回时间
        if (date.getDate() == now.getDate() && distance < DateUtils.DAY) {
            return DateUtils.getDateFormatString(timeStr, "今天 HH:mm");
        }
        if (distance >= DateUtils.WEEK) {//消息大于1周,显示手机收发时间的日期
            return DateUtils.getDateFormatString(timeStr, "yyyy/MM/dd HH:mm");
        }
        //以下是一周内
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, -1);

        if (date.getDate() == calendar.get(Calendar.DATE)) {
            return DateUtils.getDateFormatString(timeStr, "昨天 HH:mm");
        }
        calendar.add(Calendar.DATE, -1);
        if (date.getDate() == calendar.get(Calendar.DATE)) {
            return DateUtils.getDateFormatString(timeStr, "前天 HH:mm");
        }
        return DateUtils.getDateFormatString(timeStr, "E HH:mm");
    }
}
