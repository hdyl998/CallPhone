package com.hd.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.hd.base.interfaceImpl.TextWatcherImpl;
import com.hd.utils.log.impl.LogUitls;

/**
 * 视图工具类
 * Created by liugd on 2017/1/3.
 */

public class EditTextUtil {


    /***
     * 设置编辑框最大限值，如果超过这个值，则取最大值
     *
     * @param editText  编辑框
     * @param maxVarStr 最大值时的显示数字
     * @param maxVar    最大值
     */
    public static void setEditMaxVar(final EditText editText, final String maxVarStr, final float maxVar) {
        editText.addTextChangedListener(new TextWatcherImpl() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                double data = NumberUtil.convertToFloat(s.toString().trim());
                if (data > maxVar) {
                    LogUitls.print("ttt", data + "大于" + maxVar);
                    editText.setText(maxVarStr);
                    editText.setSelection(maxVarStr.length());
                }
            }
        });
    }

    /***
     * 设置编辑框最大限值，如果超过这个值，则取最大值
     *
     * @param editText  编辑框
     * @param maxVarStr 最大值时的显示数字
     * @param maxVar    最大值
     */
    public static void setEditVarRange(final EditText editText, final String minVarStr, final double minVar, final String maxVarStr, final double maxVar) {
        editText.addTextChangedListener(new TextWatcherImpl() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString().trim();
                if (string.length() != 0) {//长度不为0才做处理，不然不能删除数据
                    double data = NumberUtil.convertToDouble(string);
                    if (data > maxVar) {
                        editText.setText(maxVarStr);
                        editText.setSelection(maxVarStr.length());
                    } else if (data < minVar) {
                        editText.setText(minVarStr);
                        editText.setSelection(minVarStr.length());
                    }
                }
            }
        });
    }

    public static void addTextViewFilter(TextView source, InputFilter inputFilter) {
        if (inputFilter == null) {
            return;
        }
        InputFilter[] inputFilters = new InputFilter[source.getFilters() != null ? source.getFilters().length + 1 : 1];
        inputFilters[0] = inputFilter;
        if (source.getFilters() != null) {
            for (int i = 0; i < source.getFilters().length; i++)
                inputFilters[i + 1] = source.getFilters()[i];
        }
        source.setFilters(inputFilters);
    }



    /**
     * 限制文只能输入两位小数点
     *
     * @param editText
     */
    public static void setEditPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcherImpl() {

            @Override
            public void onTextChanged(CharSequence tt, int start, int before, int count) {
                String s = tt.toString();
                if (s.contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.substring(0, s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                    }
                }
            }
        });
    }


    /***
     * 银行卡号码的格式
     *
     * @param mEditText
     */
    public static void setTypeBankCardNumAddSpace(final EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0;// 记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3
                        || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = mEditText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    mEditText.setText(str);
                    Editable etable = mEditText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }

    /****
     * 手机号码的格式
     *
     * @param mEditText
     */
    public static void setTypePhoneNumAddSpace(final EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0;// 记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3
                        || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = mEditText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 3 || index == 8)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    mEditText.setText(str);
                    Editable etable = mEditText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }


}
