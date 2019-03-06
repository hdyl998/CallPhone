package com.hd.utils.device;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.hd.R;
import com.hd.base.HdApp;
import com.hd.utils.DensityUtils;

/**
 * Note：None
 * Created by lgd on 2018/12/17 15:40
 * E-Mail Address：986850427@qq.com
 */
public class AppDeviceInfo {

    public final static int mScreenWidth;
    public final static int mScreenHeight;
    public final static int mDialogWidth;
    public final static int mWidthDP;//宽度的DP
    public final static int mStatusBarHeight;//状态栏高度
    public final static int mTitleBarHeight;
    public final static String appName;


    static {
        Context context = HdApp.getContext();
        DisplayMetrics display = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(display);
        mScreenHeight = Math.max(display.heightPixels, display.widthPixels);
        mScreenWidth = Math.min(display.heightPixels, display.widthPixels);
        mDialogWidth = (int) (mScreenWidth * 0.8f);
        mWidthDP = DensityUtils.px2dip( AppDeviceInfo.mScreenWidth);
        //API19以上取大于0的值,否则不给值
        mStatusBarHeight = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? DensityUtils.getStatusBarHeight(HdApp.getContext()) : 0;
        mTitleBarHeight = HdApp.getContext().getResources().getDimensionPixelSize(R.dimen.titleBarHeight);
        appName = DeviceInfoUtils.getAppName(context);
    }

}
