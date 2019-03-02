package com.hd.utils;

import android.widget.EditText;

import com.hd.base.interfaceImpl.TextWatcherImpl;

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


    /****
     * 过滤非法密码字符
     * @param editText
     */
    public static void setEditTextPasswordFilter(EditText editText){
        TextWatcherImpl pwdtextWatcher=new TextWatcherImpl() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = editText.getText().toString();
                String str = stringPassword(editable.toString());
                if (!editable.equals(str)) {
                    editText.setText(str);
                    // 设置新的光标所在位置
                    editText.setSelection(str.length());
                }
            }
        };
        editText.addTextChangedListener(pwdtextWatcher);
    }
}
