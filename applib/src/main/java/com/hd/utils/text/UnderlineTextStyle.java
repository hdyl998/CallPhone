package com.hd.utils.text;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;

/**
 * 下划线的样式
 * Created by liugd on 2017/5/3.
 */

public class UnderlineTextStyle extends ITextStyle {
    public UnderlineTextStyle(String mStr) {
        super(mStr);
    }

    @Override
    public CharSequence getString() {
        SpannableString ss = new SpannableString(mStr);
        ss.setSpan(new UnderlineSpan(), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
        return ss;
    }
}
