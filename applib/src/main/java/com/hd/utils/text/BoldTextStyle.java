package com.hd.utils.text;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

/**
 * 粗体的样式
 * Created by liugd on 2017/5/3.
 */

public class BoldTextStyle extends ITextStyle {
    public BoldTextStyle(String mStr) {
        super(mStr);
    }

    @Override
    public CharSequence getString() {
        SpannableString ss = new SpannableString(mStr);
        ss.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
        return ss;
    }



}
