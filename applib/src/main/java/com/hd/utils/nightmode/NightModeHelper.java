package com.hd.utils.nightmode;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

/**
 * Created by liugd on 2017/2/3.
 */

public class NightModeHelper {
    public View mView;
    public Context mContext;
    public Window mWindow;

    public NightModeHelper(Context mContext, Window mWindow) {
        this.mContext = mContext;
        this.mWindow = mWindow;
    }


    /**
     * 调整亮度(夜间模式)
     *
     * @param isNight 是否是夜间模式
     */
    public void setNightMode(boolean isNight) {
        if (isNight) {
            if (mView == null) {
                mView = new View(mContext);
                mView.setBackgroundColor(0x80000000);
                FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                mWindow.addContentView(mView, ll);
            } else {
                mView.setVisibility(View.VISIBLE);
            }
        } else {
            if (mView != null) {
                mView.setVisibility(View.GONE);
            }
        }
    }
}
