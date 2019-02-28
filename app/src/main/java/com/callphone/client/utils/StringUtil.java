package com.callphone.client.utils;

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
}
