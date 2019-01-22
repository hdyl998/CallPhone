package com.hd.utils.text;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

/**
 * Created by liugd on 2017/4/12.
 */

public class SizeTextStyle extends ITextStyle {
    int pxSize;

    public SizeTextStyle(String string) {
        super(string);
    }

    /***
     * 设置实际大小
     *
     * @param pxSize
     * @return
     */
    public SizeTextStyle setPxSize(int pxSize) {
        this.pxSize = pxSize;
        return this;
    }

    public SizeTextStyle setResSize(Context context, @DimenRes int resSize) {
        this.pxSize = (int) context.getResources().getDimension(resSize);
        return this;
    }

    @Override
    public CharSequence getString() {
        SpannableString ss = new SpannableString(mStr);
        ss.setSpan(new AbsoluteSizeSpan(pxSize), 0, mStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
