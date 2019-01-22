package com.hd.utils;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by liugd on 2017/3/21.
 */

public class TextViewUtils {

    /***
     * 设置textview有下划线
     *
     * @param textView
     */
    public static void setTextViewUnderLine(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textView.getPaint().setAntiAlias(true);//抗锯齿
    }

    /**
     * 设置textView 右边的 drawable (默认右边的drawable)
     *
     * @param textView 控件
     * @param resID    资源ID
     */
    public static void setDrawable(TextView textView, @DrawableRes int resID) {
        setDrawable(textView, resID, DIRECTION_RIGHT);
    }


    public static void setDrawableNull(TextView textView) {
        setDrawable(textView, 0, DIRECTION_NONE);
    }

    /**
     * 设置textView drawable
     *
     * @param textView  控件
     * @param resID     资源ID
     * @param direction 方向 0-左，1-上 2-右 3下
     */
    public static void setDrawable(TextView textView, @DrawableRes int resID, @TextDrawableDirection int direction) {
        if (direction == DIRECTION_NONE) {
            textView.setCompoundDrawables(null, null, null, null);
        } else {
            Drawable rightDrawable = textView.getResources().getDrawable(resID);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
            switch (direction) {
                case DIRECTION_LEFT:
                    textView.setCompoundDrawables(rightDrawable, null, null, null);
                    break;
                case DIRECTION_UP:
                    textView.setCompoundDrawables(null, rightDrawable, null, null);
                    break;
                case DIRECTION_RIGHT:
                    textView.setCompoundDrawables(null, null, rightDrawable, null);
                    break;
                case DIRECTION_DOWN:
                    textView.setCompoundDrawables(null, null, null, rightDrawable);
                    break;
            }
        }


    }

    //定义范围
    @IntDef({DIRECTION_LEFT, DIRECTION_RIGHT, DIRECTION_UP, DIRECTION_DOWN, DIRECTION_NONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextDrawableDirection {
    }

    public final static int DIRECTION_LEFT = 0;
    public final static int DIRECTION_UP = 1;
    public final static int DIRECTION_RIGHT = 2;
    public final static int DIRECTION_DOWN = 3;
    public final static int DIRECTION_NONE = 4;

}
