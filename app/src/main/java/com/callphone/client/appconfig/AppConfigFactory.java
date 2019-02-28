package com.callphone.client.appconfig;

import com.hd.appconfig.IAppConfigFactory;

/**
 * Note：None
 * Created by lgd on 2018/12/17 14:55
 * E-Mail Address：986850427@qq.com
 */
public class AppConfigFactory {

    private static BaseAppConfig INSTANCE = new TestAppConfig();

    //这里配置打包时间
    public static BaseAppConfig getConfig() {
        return INSTANCE;
    }


    /***
     * 初始化，需要在application里面调用
     */
    public static void init() {
        IAppConfigFactory.setConfig(INSTANCE);
    }



    /**
     * 当配置完全不同时才做改变
     *
     * @param config
     */
    public static void setAppConfig(BaseAppConfig config) {
        if (INSTANCE != null && config != null) {
            if (INSTANCE.getClass() != config.getClass()) {
                INSTANCE = config;
                IAppConfigFactory.setConfig(INSTANCE);
            }
        }
    }

}
