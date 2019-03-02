package com.callphone.client.base;

import android.app.Application;
import android.content.pm.ApplicationInfo;

import com.callphone.client.appconfig.AppConfigFactory;
import com.callphone.client.base.data.AppSaveData;
import com.callphone.client.main.mine.LoginManager;
import com.hd.appconfig.IAppConfigFactory;
import com.hd.base.HdApp;
import com.hd.net.HttpFactory;
import com.hd.net.INetConfig;
import com.hd.net.NetBuilder;
import com.hd.net.NetCallback;
import com.hd.net.socket.NetEntity;
import com.hd.utils.log.impl.LogUitls;

/**
 * Created by liugd on 2019/1/17.
 */

public class App extends HdApp {

    public static Application getContext() {
        return HdApp.getContext();
    }

    @Override
    public void initApp() {

        AppConfigFactory.init();
        NetEntity.ERROR_NOT_LOGIN = -9999;

        HttpFactory.getNetHelper().setGlobalConfig(new INetConfig() {
            @Override
            public void onBeforeRequest(NetBuilder params) {
                params.add2Url("ck", AppSaveData.getUserVInfo().getToken());
            }

            @Override
            public void onHandleCodeMessage(int code) {
//                if (NetEntity.ERROR_NOT_LOGIN == code) {
//                    LogUitls.print("logint","codelogout");
//                    LoginManager.logout();
//                }
            }

            @Override
            public String getBaseUrl() {
                return AppConfigFactory.getConfig().getBaseUrl();
            }
        });
    }
}
