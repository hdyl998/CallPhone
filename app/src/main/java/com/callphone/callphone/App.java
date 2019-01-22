package com.callphone.callphone;

import com.hd.appconfig.IAppConfigFactory;
import com.hd.base.HdApp;
import com.hd.net.HttpFactory;
import com.hd.net.INetConfig;
import com.hd.net.NetBuilder;

/**
 * Created by liugd on 2019/1/17.
 */

public class App extends HdApp {
    @Override
    public void initApp() {
        HttpFactory.getNetHelper().setGlobalConfig(new INetConfig() {
            @Override
            public void onBeforeRequest(NetBuilder params) {

            }

            @Override
            public void onHandleCodeMessage(int code) {

            }

            @Override
            public String getBaseUrl() {
                return "http://47.106.179.199:8000/?act=get&";
            }
        });
    }
}
