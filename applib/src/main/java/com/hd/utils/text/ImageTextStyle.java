package com.hd.utils.text;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

/**
 * Created by liugd on 2017/5/8.
 */

public class ImageTextStyle extends ITextStyle {

    private Bitmap bitmap;
    private Context mContext;

    public ImageTextStyle(String mStr) {
        super(mStr);
    }


    public ImageTextStyle setBitmap(Context mContext, Bitmap bitmap) {
        this.mContext = mContext;
        this.bitmap = bitmap;
        return this;
    }


    @Override
    public CharSequence getString() {
        if (bitmap == null) {
            throw new IllegalArgumentException("bitmap不能为null");
        }
        SpannableString ss = new SpannableString(mStr);
        ss.setSpan(new ImageSpan(mContext, bitmap), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
