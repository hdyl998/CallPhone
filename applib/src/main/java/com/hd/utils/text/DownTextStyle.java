package com.hd.utils.text;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.SubscriptSpan;

/**
 * 下标的
 * Created by liugd on 2017/4/12.
 */

public class DownTextStyle extends ITextStyle {

    public DownTextStyle(String string) {
        super(string);
    }

    @Override
    public CharSequence getString() {
        SpannableString ss = new SpannableString(mStr);
        ss.setSpan(new SubscriptSpan(), 0, mStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
