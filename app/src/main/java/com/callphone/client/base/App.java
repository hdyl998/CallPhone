package com.callphone.client.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.callphone.client.appconfig.AppConfigFactory;
import com.callphone.client.base.data.AppSaveData;
import com.callphone.client.main.mine.LoginManager;
import com.hd.appconfig.IAppConfigFactory;
import com.hd.base.HdApp;
import com.hd.base.datacount.EventInjectHelper;
import com.hd.base.datacount.IEventInject;
import com.hd.net.HttpFactory;
import com.hd.net.INetConfig;
import com.hd.net.NetBuilder;
import com.hd.net.NetCallback;
import com.hd.net.socket.NetEntity;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.other.MyRunTimeException;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Created by liugd on 2019/1/17.
 */

public class App extends HdApp {

    public static App getContext() {
        return (App) HdApp.getContext();
    }


    private void initUMengChannel(Context mContext) {
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(mContext, UMConfigure.DEVICE_TYPE_PHONE, null);

    }

    @Override
    public void initApp() {

        AppConfigFactory.init();
        initUMengChannel(this);
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


        EventInjectHelper.setInject(new IEventInject() {


            @Override
            public void onPause(Context context, String key) {
                MobclickAgent.onPause(context);
            }

            @Override
            public void onResume(Context context, String key) {
                MobclickAgent.onResume(context);
            }

            @Override
            public void reportError(Throwable e) {
                MobclickAgent.reportError(App.getContext(), e);
            }

        });
    }


    public void reportError(Throwable throwable, String str) {
        String text = AppSaveData.getUserVInfo().phone + "--" + str;
        MyRunTimeException exception = new MyRunTimeException(text);
        exception.addSuppressed(throwable);
        MobclickAgent.reportError(getContext(), exception);
    }

    public void reportError(String str) {
        String text = AppSaveData.getUserVInfo().phone + "--" + str;
        MobclickAgent.reportError(getContext(), text);
    }
}
