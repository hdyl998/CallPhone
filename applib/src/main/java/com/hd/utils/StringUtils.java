package com.hd.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by liugd on 2019/1/3.
 */

public class StringUtils {
    /**
     * @param str 需要过滤的特殊字符
     * @return String 过滤结果
     * @throws PatternSyntaxException
     * @方法名: StringFilter
     * @功能描述: 过滤特殊字符
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符!*'();:@&=+$,/?%#[]
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
