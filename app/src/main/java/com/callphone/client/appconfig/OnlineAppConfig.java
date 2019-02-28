package com.callphone.client.appconfig;

/**
 * Note：None
 * Created by Liuguodong on 2018/12/17 14:50
 * E-Mail Address：986850427@qq.com
 */
public class OnlineAppConfig extends BaseAppConfig {

    public OnlineAppConfig(){
        isOnline=true;
        isTestFun=false;
        isDebug=false;
        baseUrl="http://api.topspeedcloud.com/index.php?s=index/";
        msgSocketUrl="http://203.195.170.189:20001";
    }
}
