package com.hd.view.roundrect;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.hd.R;
import com.hd.utils.DensityUtils;


/**
 * 圆角矩形背景的线性布局
 * Created by liugd on 2017/1/3.
 */

public class ShapeBgLinearLayout extends LinearLayout {


    public ShapeBgLinearLayout(Context context) {
        super(context);
    }

    public ShapeBgLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeBgLinearLayout);
        int mRadius = mTypedArray.getDimensionPixelSize(R.styleable.ShapeBgLinearLayout_appRadius, DensityUtils.getDimenPx(5));
        int mColorBg = mTypedArray.getColor(R.styleable.ShapeBgLinearLayout_appBgColor, Color.BLACK);
        int mBorderColor = mTypedArray.getColor(R.styleable.ShapeBgLinearLayout_appBorderColor, Color.TRANSPARENT);
        int mBorderWidth = mTypedArray.getDimensionPixelSize(R.styleable.ShapeBgLinearLayout_appBorderWidth, DensityUtils.getDimenPx(1));
        mTypedArray.recycle();
        if (mBorderColor == Color.TRANSPARENT) {
            this.setBackgroundDrawable(RoundRectTool.getRoundRectBgDrawable( mRadius, mColorBg));
        } else {
            Drawable[] array = {
                    RoundRectTool.getRoundRectBgDrawable( mRadius, mColorBg),
                    RoundRectTool.getRoundRectBorderDrawable( mRadius, mBorderColor, mBorderWidth)};
            LayerDrawable ld = new LayerDrawable(array);
            this.setBackgroundDrawable(ld);
        }
    }
}


