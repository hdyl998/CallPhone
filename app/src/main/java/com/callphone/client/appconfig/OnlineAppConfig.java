package com.callphone.client.appconfig;

/**
 * Note：None
 * Created by lgd on 2018/12/17 14:50
 * E-Mail Address：986850427@qq.com
 */
public class OnlineAppConfig extends BaseAppConfig {

    public OnlineAppConfig(){
        isOnline=true;
        isTestFun=false;
        isDebug=false;
        baseUrl = "http://47.106.179.199:8000/?act=";
        msgSocketUrl = "http://47.106.179.199:8001";
    }
}
