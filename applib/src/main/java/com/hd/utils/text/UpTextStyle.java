package com.hd.utils.text;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.SuperscriptSpan;

/**
 * 上标
 * Created by liugd on 2017/4/12.
 */

public class UpTextStyle extends ITextStyle {

    public UpTextStyle(String string) {
        super(string);
    }

    @Override
    public CharSequence getString() {
        SpannableString ss = new SpannableString(mStr);
        ss.setSpan(new SuperscriptSpan(), 0, mStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
