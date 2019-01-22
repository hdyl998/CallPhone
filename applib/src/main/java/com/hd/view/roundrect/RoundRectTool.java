package com.hd.view.roundrect;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

/**
 * Created by liugd on 2017/1/3.
 */

public class RoundRectTool {

    public RoundRectTool() {
        throw new IllegalArgumentException("工具类不需要实例化");
    }

    /***
     *设置视图背景为圆角矩形
     * @param view
     * @param mRadius 圆角幅度
     * @param color 颜色
     */
    public static Drawable getRoundRectBgDrawable(int mRadius, int color) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius}, null, null));
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

//    /***
//     *设置视图背景为圆角矩形
//     * @param view 视图
//     * @param color
//     */
//    public static void setViewRoundRectBg(View view, int color) {
//        setViewRoundRectBg(view, DensityUtils.getDimenPx(5), color);
//    }


    /***
     *设置视图背景为圆角矩形
     * @param view
     * @param mRadius 圆角幅度
     * @param color 颜色
     */
    public static Drawable getRoundRectBorderDrawable(int mRadius, int color, int borderWidth) {
        RectF rectF = new RectF(borderWidth, borderWidth, borderWidth, borderWidth);
        float radii[] = {mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius};
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(radii, rectF, radii));
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

}
