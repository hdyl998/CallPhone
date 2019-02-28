package com.hd.net;

/**
 * Note：None
 * Created by Liuguodong on 2018/12/24 10:41
 * E-Mail Address：986850427@qq.com
 */
public class DefaultNetDataBean implements INetDataBean {
    public int CODE;
    public String MSG;
    public String DATA;

    public DefaultNetDataBean(){

    }

    public DefaultNetDataBean(String data){
        this.DATA=data;
    }

    @Override
    public int getCode() {
        return CODE;
    }

    @Override
    public String getMsg() {
        return MSG;
    }

    @Override
    public String getData() {
        return DATA;
    }
}
