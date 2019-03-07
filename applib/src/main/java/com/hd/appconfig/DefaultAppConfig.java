package com.hd.appconfig;

/**
 * Note：None
 * Created by lgd on 2019/1/4 10:07
 * E-Mail Address：986850427@qq.com
 */
public class DefaultAppConfig implements IBaseAppConfig {
    @Override
    public boolean isDebug() {
        return false;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public boolean isTestFun() {
        return false;
    }
}
