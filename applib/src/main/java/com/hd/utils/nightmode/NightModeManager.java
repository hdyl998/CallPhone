package com.hd.utils.nightmode;

import com.hd.cache.SpUtils;

/**
 * 夜间模式存
 * Created by liugd on 2017/2/3.
 */

public class NightModeManager {
    /***
     * 设置系统亮度
     */
    final static String EXTRA_SYSTEM = "system";
    final static String EXTRA_BRIGHT = "bright";

    //是否是夜间模式
    private boolean isNightMode = false;
    //静态量
    private final static NightModeManager instance = new NightModeManager();


    private NightModeManager() {
        isNightMode = SpUtils.getBoolean( EXTRA_SYSTEM, EXTRA_BRIGHT, isNightMode);
    }


    public static NightModeManager getInstance() {
        return instance;
    }

    /***
     * 得到夜间模式
     *
     * @return
     */
    public boolean isNightMode() {
        return isNightMode;
    }

    /***
     * 设置夜间模式的方法
     *
     * @param nightMode
     */
    public void setNightMode(boolean nightMode) {
        if (nightMode != isNightMode) {
            isNightMode = nightMode;
            SpUtils.putBoolean( EXTRA_SYSTEM, EXTRA_BRIGHT, isNightMode);
        }
    }

}
