package com.hd.utils;

import android.content.Context;
import android.util.SparseIntArray;

import com.hd.base.HdApp;

import java.lang.reflect.Field;

/**
 * Note：None
 * Created by lgd on 2018/12/17 15:44
 * E-Mail Address：986850427@qq.com
 */
public class DensityUtils {
    /**
     * 方法名: getStatusBarHeight
     * <p/>
     * 功能描述:得到状态栏高度
     *
     * @return int 状态栏高度
     * <p/>
     * throws
     */
    public static int getStatusBarHeight(Context context) {
        int x;
        int sbar;
        Class<?> c;
        Object obj;
        Field field;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
            return sbar;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return 0;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = HdApp.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip( float pxValue) {
        final float scale = HdApp.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    private final static SparseIntArray fontDimenCaches = new SparseIntArray();


    /***
     * 获取字体对应的font
     * @param dimen
     * @return
     */
    public static int getDimenPxForText(int dimen){
        int px = fontDimenCaches.get(dimen,-1);
        if (px != -1) {
            return px;
        }
        Context context=HdApp.getContext();
        //先从资源中找对应的dimen px ,如果没有自已计算得到
        int resId = ResourceUtil.getDimenId(context, String.format("s%dpx", dimen));
        if (resId != 0) {
            px = context.getResources().getDimensionPixelSize(resId);
            fontDimenCaches.put(dimen, px);
            return px;
        }
        float dp = dimen * 360f / CURRENT_WIDTH;//除以CURRENT_WIDTH表示用的是CURRENT_WIDTH的标注
        px = dip2px(dp);
        fontDimenCaches.put(dimen, px);
        return px;
    }

    //缓存
    private final static SparseIntArray dimenCaches = new SparseIntArray();
    /***
     * 获取对应dimen的px
     * @param dimen
     * @return
     */
    public static int getDimenPx(int dimen) {
        int px = dimenCaches.get(dimen,-1);
        if (px != -1) {
            return px;
        }
        Context context=HdApp.getContext();
        //先从资源中找对应的dimen px ,如果没有自已计算得到
        int resId = ResourceUtil.getDimenId(context, String.format("n%dpx", dimen));
        if (resId != 0) {
            px = context.getResources().getDimensionPixelSize(resId);
            dimenCaches.put(dimen, px);
            return px;
        }
        float dp = dimen * 360f / CURRENT_WIDTH;//除以CURRENT_WIDTH表示用的是CURRENT_WIDTH的标注
        px = dip2px(dp);
        dimenCaches.put(dimen, px);
        return px;
    }

    public final static int CURRENT_WIDTH=750;
}
