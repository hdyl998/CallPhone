package com.hd.base.theme;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class ThemeUtils {


    /***
     * 初始化UI样式
     * @param mContext
     * @param iTheme
     */
    public static void initThemeStyle(Activity mContext, ITheme iTheme) {
        //设置背景色，改系统默认色为灰色
        Window window = mContext.getWindow();
        window.setBackgroundDrawableResource(iTheme.setBackgroundColor());
        //大于5.0才能生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (iTheme.isStatusBarColor()) {//有状态栏颜色
//                //修改状态栏的颜色
                if (iTheme.isStatusBarLightStyle()) {
                    StatusBarColorUtils.setLightStyle(mContext);
                } else {
                    StatusBarColorUtils.setDarkStyle(mContext);
                }
            } else { //全屏沉浸,无状态栏颜色
                window.setStatusBarColor(Color.TRANSPARENT);   //设置状态栏的颜色
                window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }
}
