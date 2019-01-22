package com.hd.utils.text;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.SuperscriptSpan;

/**
 * 复合样式控件
 * Created by liugd on 2017/4/13.
 */

public class ComplexTextStyle extends ITextStyle {

    public int pxSize;//字的大小
    public int color;//颜色
    public boolean isUp;//是否是上角标

    public boolean isBold;

    public ComplexTextStyle(String string) {
        super(string);
    }


    public ComplexTextStyle setBold(boolean bold) {
        isBold = bold;
        return this;
    }

    /***
     * 设置实际大小
     *
     * @param pxSize
     * @return
     */
    public ComplexTextStyle setPxSize(int pxSize) {
        this.pxSize = pxSize;
        return this;
    }

    public ComplexTextStyle setResSize(Context context, @DimenRes int resSize) {
        this.pxSize = (int) context.getResources().getDimension(resSize);
        return this;
    }

    public ComplexTextStyle setResColor(Context context, @ColorRes int resColor) {
        this.color = context.getResources().getColor(resColor);
        return this;
    }

    public ComplexTextStyle setColor(int color) {
        this.color = color;
        return this;
    }


    public ComplexTextStyle setUp(boolean up) {
        isUp = up;
        return this;
    }

    @Override
    public CharSequence getString() {
        SpannableString ss = new SpannableString(mStr);
        int len = ss.length();
        if (pxSize != 0)
            ss.setSpan(new AbsoluteSizeSpan(pxSize), 0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (isUp) {
            ss.setSpan(new SuperscriptSpan(), 0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (color != 0)
            ss.setSpan(new ForegroundColorSpan(color), 0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if(isBold){
            ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
        }
        return ss;
    }
}
