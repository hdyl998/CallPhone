package com.hd.net;

import android.content.Context;

import com.hd.base.HdApp;
import com.hd.net.coderemind.IMessage;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Date:2017/10/26 11:07
 * Author:liugd
 * Modification:
 **/


public class NetBuilder {
    //POST 参数
    private Map<String, String> postMap = new HashMap<>();
    //url用到的MAP 加上顺序
    private Map<String, String> urlMap = new LinkedHashMap<>();

    private Map<String, String> headerMap = new LinkedHashMap<>();

    NetCallback callback;
    IMessage iMessage = IMessage.errorMessage;
    Object flag;

    WeakReference<Context> mContext;

    public Context getContext() {
        return mContext.get();
    }

    String baseUrl;//为空则去取默认的，否则以最新的为准


    public NetBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public Map<String, String> getPostMap() {
        return postMap;
    }

    public Map<String, String> getUrlMap() {
        return urlMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public NetBuilder(Context mContext) {
        this.mContext = new WeakReference<>(mContext);
    }


    public NetBuilder setApi(String api) {
        this.api = api;
        return this;
    }


    public NetBuilder setMessage(IMessage message) {
        this.iMessage = message;
        return this;
    }

//    public static void test(Context mContext) {
//        NetBuilder.create(mContext).setApi("user", "sendsms")
//                .add("mobile", "18684928370")
//                .add("type", 1)
//                .start(new NetCallbackImpl() {
//                    @Override
//                    public void onError(NetEntity entity) throws Exception {
//                        super.onError(entity);
//                    }
//                });
//    }


    public static NetBuilder create(Context mContext) {
        return new NetBuilder(mContext);
    }

    public static NetBuilder create() {
        return new NetBuilder(HdApp.getContext());
    }

    public NetBuilder add2Post(String key, String value) {
        this.postMap.put(key, value);
        return this;
    }


    public NetBuilder add2Post(String key, int value) {
        this.postMap.put(key, String.valueOf(value));
        return this;
    }

    public NetBuilder add2Post(String key, long value) {
        this.postMap.put(key, String.valueOf(value));
        return this;
    }


    public NetBuilder add2Post(String key, Object value) {
        this.postMap.put(key, String.valueOf(value));
        return this;
    }

    public NetBuilder add2PostAll(Map<String, String> map) {
        if (map != null)
            this.postMap.putAll(map);
        return this;
    }

    public NetBuilder add2Url(String key, String value) {
        this.urlMap.put(key, value);
        return this;
    }

    public NetBuilder add2Url(String key, Object value) {
        this.urlMap.put(key, String.valueOf(value));
        return this;
    }

    public NetBuilder add2Url(String key, int value) {
        this.urlMap.put(key, Integer.toString(value));
        return this;
    }

    public NetBuilder add2UrlAll(Map<String, String> map) {
        if (map != null)
            this.urlMap.putAll(map);
        return this;
    }


    public NetBuilder add2Header(String key, String value) {
        this.headerMap.put(key, value);
        return this;
    }

    public NetBuilder add2Header(String key, Object value) {
        this.headerMap.put(key, String.valueOf(value));
        return this;
    }

    public NetBuilder add2Header(String key, int value) {
        this.headerMap.put(key, Integer.toString(value));
        return this;
    }

    public NetBuilder add2HeaderAll(Map<String, String> map) {
        if (map != null)
            this.headerMap.putAll(map);
        return this;
    }


    public NetBuilder setCallback(NetCallback callback) {
        this.callback = callback;
        return this;
    }


    public NetBuilder setFlag(Object flag) {
        this.flag = flag;
        return this;
    }


    public void start() {
        HttpFactory.getNetHelper().newRequest(this);
    }

//    public void start(NetCallback callback) {
//        this.setCallback(callback);
//        new NetImpl(this);
//    }

    public String api;

    public String getApi() {
        return api;
    }


    /***
     * 启动网络，方便使用而采用的
     */
    public void start(String api, NetCallback callback) {
        setApi(api).setCallback(callback);
        start();
    }


    public void startUpload(File file){

    }


    public void startDownload(){

    }

}
