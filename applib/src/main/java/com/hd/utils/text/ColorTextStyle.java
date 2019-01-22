package com.hd.utils.text;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

/**
 * Created by liugd on 2017/4/12.
 */

public class ColorTextStyle extends ITextStyle {
    int color;

    public ColorTextStyle(String string) {
        super(string);
    }

    public ColorTextStyle setResColor(Context context, @ColorRes int resColor) {
        this.color = context.getResources().getColor(resColor);
        return this;
    }

    public ColorTextStyle setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    public CharSequence getString() {
        SpannableString ss = new SpannableString(mStr);
        ss.setSpan(new ForegroundColorSpan(color), 0, mStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
