package com.hd.view.roundrect;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.hd.R;
import com.hd.utils.DensityUtils;


/**圆角矩形背景的相对布局
 * Created by liugd on 2017/1/3.
 */

public class ShapeBgRelativeLayout extends RelativeLayout {

    public ShapeBgRelativeLayout(Context context) {
        super(context);
    }

    public ShapeBgRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeBgRelativeLayout);
        int  mRadius = mTypedArray.getDimensionPixelSize(R.styleable.ShapeBgRelativeLayout_appRadius, DensityUtils.getDimenPx(5));
        int  mColorBg = mTypedArray.getColor(R.styleable.ShapeBgRelativeLayout_appBgColor, Color.BLACK);
        int mBorderColor = mTypedArray.getColor(R.styleable.ShapeBgRelativeLayout_appBorderColor, Color.TRANSPARENT);
        int mBorderWidth = mTypedArray.getDimensionPixelSize(R.styleable.ShapeBgRelativeLayout_appBorderWidth, DensityUtils.getDimenPx(1));
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
