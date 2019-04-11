package com.callphone.client;

import android.view.WindowManager;

import com.callphone.client.common.DeviceHelper;

/**
 * Created by liugd on 2019/4/12.
 */

public class Test {
    public static void main(String[] args) {
        int flag = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED //锁屏状态下显示
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD //解锁
                ;
        flag |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; //保持屏幕长亮
        flag |= WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;

        System.out.println(flag);
    }
}
