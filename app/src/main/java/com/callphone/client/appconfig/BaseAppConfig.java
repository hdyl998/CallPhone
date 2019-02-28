package com.callphone.client.appconfig;

import com.hd.appconfig.IBaseAppConfig;

/**
 * Note：None
 * Created by lgd on 2018/12/17 14:47
 * E-Mail Address：986850427@qq.com
 */
public abstract class BaseAppConfig implements IBaseAppConfig {
    protected String baseUrl;

    protected boolean isOnline;
    protected boolean isTestFun;//是否显示本地功能
    protected boolean isDebug;//是否是调试，调试则打印日志
    protected String h5Url;//H5url
    protected String msgSocketUrl;//消息Socket
    protected String imgUrl;//图片
    public String getBaseUrl() {
        return baseUrl;
    }



    public String getMsgSocketUrl() {
        return msgSocketUrl;
    }

    @Override
    public boolean isOnline() {
        return isOnline;
    }
    @Override
    public  boolean isTestFun(){
        return isTestFun;
    }

    @Override
    public boolean isDebug() {
        return isDebug;
    }




    public String getH5Url(){
        return h5Url;
    }

    public String getImgUrl() {
        return imgUrl;
    }


}
