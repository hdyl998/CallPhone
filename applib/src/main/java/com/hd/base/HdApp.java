package com.hd.base;

import android.app.Application;

import com.hd.utils.ProcessUtils;

/**
 * app基类,处理一些常用的问题
 */
public abstract class HdApp extends Application {

    private static HdApp app;


    public static Application getContext() {
        return app;
    }


    @Override
    public final void onCreate() {
        super.onCreate();
        app=this;
        //非主进程不处理,防止多进程程序初始化多次
        if (!ProcessUtils.isMainProcess(this)) {
            return;
        }

        initApp();
    }

    public abstract void initApp();
}
