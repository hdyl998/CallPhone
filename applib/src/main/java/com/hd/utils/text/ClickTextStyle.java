package com.hd.utils.text;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by liugd on 2017/4/12.
 */

public class ClickTextStyle extends ITextStyle {
    View.OnClickListener clickListener;
    int color;

    public ClickTextStyle(String string) {
        super(string);
    }

    public ClickTextStyle setResColor(Context context, @ColorRes int resColor) {
        this.color = context.getResources().getColor(resColor);
        return this;
    }

    public ClickTextStyle setColor(int color) {
        this.color = color;
        return this;
    }


    public ClickTextStyle setOnClickListener(View.OnClickListener onClickListener) {
        this.clickListener = onClickListener;
        return this;
    }

    @Override
    public CharSequence getString() {
        SpannableString ss = new SpannableString(mStr);
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                clickListener.onClick(widget);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                if (color != 0) {
                    ds.setColor(color);
                } else {
                    super.updateDrawState(ds);
                }
            }
        }, 0, mStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
