package com.hd.view.roundrect;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.hd.R;


/**
 * 圆角矩形背景的线性布局
 * Created by liugd on 2017/1/3.
 */

public class ShapeBgLinearLayout extends LinearLayout {


    private int mBorderColor;
    private int mColorBg;
    private int mRadius;
    private int mBorderWidth;
    private int mColorBgEnd;
    private int mBorderColorEnd;

    public ShapeBgLinearLayout(Context context) {
        super(context);
    }

    public ShapeBgLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeBgLinearLayout);
        mRadius = mTypedArray.getDimensionPixelSize(R.styleable.ShapeBgLinearLayout_appRadius, RoundRectConstants.cornerRadius);
        mColorBg = mTypedArray.getColor(R.styleable.ShapeBgLinearLayout_appBgColor, Color.BLACK);
        mBorderColor = mTypedArray.getColor(R.styleable.ShapeBgLinearLayout_appBorderColor, Color.TRANSPARENT);
        mBorderWidth = mTypedArray.getDimensionPixelSize(R.styleable.ShapeBgLinearLayout_appBorderWidth, RoundRectConstants.shapeLineWidth);

        mColorBgEnd = mTypedArray.getColor(R.styleable.ShapeBgLinearLayout_appBgColorEnd, Color.TRANSPARENT);
        mBorderColorEnd = mTypedArray.getColor(R.styleable.ShapeBgLinearLayout_appBorderColorEnd, Color.TRANSPARENT);
        mTypedArray.recycle();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(getBackground()==null){
            setBackgroundDrawable(getBgDrawable());
        }
    }

    private Drawable getBgDrawable(){
        if (mBorderColor == Color.TRANSPARENT) {
            return RoundRectTool.getRoundRectBgDrawable(mRadius, mColorBg, mColorBgEnd,getWidth());
        } else {
            Drawable[] array = {
                    RoundRectTool.getRoundRectBgDrawable(mRadius, mColorBg, mColorBgEnd,getWidth()),
                    RoundRectTool.getRoundRectBorderDrawable(mRadius, mBorderColor, mBorderWidth, mBorderColorEnd,getWidth())};
            LayerDrawable ld = new LayerDrawable(array);
            return ld;
        }
    }
}


