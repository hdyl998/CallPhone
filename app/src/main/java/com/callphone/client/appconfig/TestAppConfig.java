package com.callphone.client.appconfig;

/**
 * Note：None
 * Created by Liuguodong on 2018/12/17 14:48
 * E-Mail Address：986850427@qq.com
 */
public class TestAppConfig extends BaseAppConfig {


    public TestAppConfig() {
        isTestFun = true;
        isDebug = true;
        baseUrl = "http://47.106.179.199:8000/?act=";
        msgSocketUrl = "http://47.106.179.199:8001";
    }

}
