package com.hd.utils.text;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.text.Spannable;
import android.text.SpannableString;



/**
 * Created by liugd on 2017/5/8.
 */

public class DrawableTextStyle extends ITextStyle {

    private int resourceId;
    private int pxSize;
    private Context mContext;

    public DrawableTextStyle(String mStr) {
        super(mStr);
    }


    public DrawableTextStyle setResourceId(Context context, @DrawableRes int resourceId) {
        this.resourceId = resourceId;
        this.mContext = context;
        return this;
    }

    /***
     * 设置实际大小
     *
     * @param pxSize
     * @return
     */
    public DrawableTextStyle setPxSize(int pxSize) {
        this.pxSize = pxSize;
        return this;
    }

    public DrawableTextStyle setResSize(Context context, @DimenRes int resSize) {
        this.pxSize = (int) context.getResources().getDimension(resSize);
        return this;
    }


    @Override
    public CharSequence getString() {
        if (resourceId == 0) {
            throw new IllegalArgumentException("resourceId不能为0");
        }
        SpannableString ss = new SpannableString(mStr);
        ss.setSpan(new EmojiconSpan(mContext, resourceId, pxSize), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}



