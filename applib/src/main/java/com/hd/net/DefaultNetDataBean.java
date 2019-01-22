package com.hd.net;

/**
 * Note：None
 * Created by Liuguodong on 2018/12/24 10:41
 * E-Mail Address：986850427@qq.com
 */
public class DefaultNetDataBean implements INetDataBean {
    public int code;
    public String msg;
    public String data;

    public DefaultNetDataBean(){

    }

    public DefaultNetDataBean(String data){
        this.data=data;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String getData() {
        return data;
    }
}
