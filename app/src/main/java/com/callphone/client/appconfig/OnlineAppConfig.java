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
        //https://jiekou.zhudai.com/wh/index.php
        baseUrl = "https://jiekou.zhudai.com/wh/index.php/?act=";
        msgSocketUrl = "http://waihuapi.zhudai.com:8001";
    }
}
