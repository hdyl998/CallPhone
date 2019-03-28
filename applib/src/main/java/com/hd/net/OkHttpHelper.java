package com.hd.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.widget.TableRow;

import com.hd.appconfig.IAppConfigFactory;
import com.hd.appconfig.IBaseAppConfig;
import com.hd.net.socket.NetEntity;
import com.hd.utils.log.impl.LogUitls;

import java.io.IOException;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Note：None
 * Created by lgd on 2018/12/19 17:50
 * E-Mail Address：986850427@qq.com
 */
public class OkHttpHelper extends AbsNetHelper {
    private OkHttpClient okHttpClient;

    OkHttpHelper() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(20, TimeUnit.SECONDS);//设置读取超时时间
        //线上不让代理上网
        if (IAppConfigFactory.getConfig().isOnline()) {
            builder.proxy(Proxy.NO_PROXY);
        }
        okHttpClient = builder
//            .addInterceptor(new RetryIntercepter(3))
                .build();
    }

//    /**
//     * 重试拦截器
//     */
//    public static class RetryIntercepter implements Interceptor {
//        public int maxRetry;//最大重试次数
//        private int retryNum = 0;//假如设置为3次重试的话,则最大可能请求4次(默认1次+3次重试)
//
//        public RetryIntercepter(int maxRetry) {
//            this.maxRetry = maxRetry;
//        }
//
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//            System.out.println("retryNum=" + retryNum);
//            Response response = chain.proceed(request);
//            while (!response.isSuccessful() && retryNum < maxRetry) {
//                retryNum++;
//                System.out.println("retryNum=" + retryNum);
//                response = chain.proceed(request);
//            }
//            return response;
//        }
//    }

    Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void newRequest(NetBuilder params) {
        //可以这里面配置需要的全局header url  post参数
        netConfig.onBeforeRequest(params);
        if (params.baseUrl == null) {
            params.baseUrl = netConfig.getBaseUrl();
        }
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.getPostMap().entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (v == null) {
                continue;
            }
            formBuilder.add(k, v);
        }
        delMapNullData(params.getUrlMap());
        params.baseUrl = params.baseUrl + params.getApi() + AppDataCollectUtils.map2Url(params.getUrlMap(), true);
        Request.Builder requestBulder = new Request.Builder()
                .url(params.baseUrl)
                .post(formBuilder.build())
                .tag(obj2Tag(params.getContext()));

        if (params.getHeaderMap() != null) {
            delMapNullData(params.getHeaderMap());
            requestBulder.headers(Headers.of(params.getHeaderMap()));
        }

        Request request = requestBulder.build();
        Call call = getOkHttpClient().newCall(request);
        call.enqueue(new MyCallBack(params));
    }

    private class MyCallBack implements Callback {
        NetEntity entity = new NetEntity();
        NetBuilder builder;

        public MyCallBack(NetBuilder builder) {
            this.builder = builder;

            entity.api = builder.api;
            entity.flag = builder.flag;
        }

        /***
         * 判断回调是否可用
         * @return
         */
        private boolean isCallBackStateOK() {
            if (builder.callback == null) {
                return false;
            }
            if (builder.callback instanceof Fragment) {
                if (((Fragment) builder.callback).getContext() == null)
                    return false;
            }
            return true;
        }

        private static final String TAG = "MyCallBack";

        @Override
        public void onFailure(Call call, IOException e) {
            LogUitls.print(TAG, e.toString());
            entity.setCode(NetEntity.ERROR_DEFAULT);
            mHandler.post(() -> {
                netConfig.onHandleCodeMessage(entity.CODE);
                if (isCallBackStateOK()) {
                    showMsg();
                    try {
                        builder.callback.onError(entity);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                }
            });
            if (IAppConfigFactory.getConfig().isDebug()) {
                LogUitls.print(builder.getApi() + "请求地址:", builder.baseUrl + AppDataCollectUtils.map2Url(builder.getPostMap(), true));
                LogUitls.print(builder.getApi() + "请求head头部信息", builder.getHeaderMap());
                LogUitls.print(builder.getApi() + "请求失败(有否有回调):" + (builder.callback != null), e.toString());
            }
        }

        @Override
        public void onResponse(Call call, Response responseBean) throws IOException {
            String response = null;
            try {
                response = responseBean.body().string();
                if (!response.startsWith("{")) {
                    response = response.substring(response.indexOf("{"));
                }
                if (isCallBackStateOK()) {
                    builder.callback.doParse(entity, response);
                    mHandler.post(() -> runOnUI());
                }
            } catch (Exception e) {//解析异常
                e.printStackTrace();
                if (isCallBackStateOK()) {
                    entity.setCode(NetEntity.ERROR_PARASE);
                    mHandler.post(() -> onError());
                }
            }

            if (IAppConfigFactory.getConfig().isDebug()) {
                LogUitls.print(builder.getApi() + "请求地址:", builder.baseUrl + AppDataCollectUtils.map2Url(builder.getPostMap(), true));
                LogUitls.print(builder.getApi() + "请求head头部信息", builder.getHeaderMap());
                LogUitls.printString(builder.getApi() + "请求成功(有否有回调):" + (builder.callback != null), response);
                LogUitls.print(builder.getApi() + "请求成功(有否有回调):" + (builder.callback != null), response);
                if (responseBean.code() != 200) {
                    LogUitls.print(builder.getApi() + "请求失败code(正常为200):", responseBean.code());
                }
            }
        }

        private void runOnUI() {
            netConfig.onHandleCodeMessage(entity.CODE);
            if (!isCallBackStateOK()) {
                return;
            }
            if (entity.CODE > -1) {//0及以上为正常
                try {
                    builder.callback.onSuccess(entity);
                    showMsg();
                } catch (Exception e) {//解析异常
                    e.printStackTrace();
                    entity.setCode(NetEntity.ERROR_PARASE);
                    onError();
                }
            } else {//服务端报的异常
                onError();
            }
        }


        private void onError() {
            try {
                showMsg();
                builder.callback.onError(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void showMsg() {
            builder.iMessage.showMessage(entity);
        }
    }


    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }


    /**
     * 根据Tag取消请求
     */
    @Override
    public void cancelTag(Context context) {
        if (context == null) return;
        Object tag = obj2Tag(context);
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }


    /**
     * 取消所有请求请求
     */
    @Override
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

}
