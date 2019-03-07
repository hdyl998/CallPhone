//package com.hd.net;
//
//import android.os.Handler;
//import android.os.Looper;
//
//import com.hd.BuildConfig;
//import com.hd.net.coderemind.IMessage;
//import com.hd.net.socket.NetEntity;
//
//import java.io.IOException;
//import java.lang.ref.WeakReference;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.Response;
//
///**
// * Note：None
// * Created by lgd on 2018/12/19 16:51
// * E-Mail Address：986850427@qq.com
// */
//public class NetImpl implements Callback {
//
//
//    WeakReference<NetCallback> netCallbackWeakReference;
//    NetEntity entity = new NetEntity();
//    IMessage iMessage;
//    Handler mHandler=new Handler(Looper.getMainLooper());
//
//    public NetImpl(NetBuilder builder) {
//        netCallbackWeakReference = new WeakReference<>(builder.callback);
//        entity.api = builder.api;
//        entity.flag = builder.flag;
//        entity.api=builder.getApi();
//        iMessage = builder.iMessage;
//    }
//
//    public NetCallback getNetCallback() {
//        return netCallbackWeakReference.get();
//    }
//
//    @Override
//    public void onFailure(Call call, IOException e) {
////        if (e instanceof SocketTimeoutException) {//判断超时异常
////            entity.setCode(NetEntity.ERROR_TIME_OUT);
////        } else if (e instanceof ConnectException) {//判断连接异常，我这里是报Failed to connect to 10.7.5.144
////            entity.setCode(NetEntity.ERROR_ConnectException);
////        } else {
////            entity.setCode(NetEntity.ERROR_DEFAULT);
////        }
//        if (BuildConfig.DEBUG) {
//            entity.setMsg(e.toString());
//        } else {
//            entity.setMsg(NetEntity.STR_ERROR);
//        }
//        entity.setCode(NetEntity.ERROR_DEFAULT);
//        callError();
//        mHandler.post(()->iMessage.showMessage(entity));
//    }
//
//    private void callError(){
//        if (getNetCallback() != null) {
//            mHandler.post(()->{
//                try {
//                    NetCallback callback=getNetCallback();
//                    if( callback!= null) {
//                        callback.onError(entity);
//                    }
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            });
//        }
//    }
//
//
//    @Override
//    public void onResponse(Call call, Response responseR) throws IOException {
//        NetCallback callback = getNetCallback();
//        if (callback != null) {
//            String response=responseR.body().toString();
//            if (!response.startsWith("{")) {
//                response = response.substring(response.indexOf("{"));
//            }
//            try {
//                callback = getNetCallback();
//                if(callback==null){
//                    return;
//                }
//                callback.doParse(entity, response);
//            } catch (Exception e) {
//                e.printStackTrace();
//                entity.setCode( NetEntity.ERROR_PARASE);
//                callError();
//            }
//            if( getNetCallback()!= null)
//            mHandler.post(()->{
//
//                if (entity.code > -1) {//0及以上为正常
//                    try {
//                        if( getNetCallback()!= null) {
//                            getNetCallback().onSuccess(entity);
//                        }
//                    } catch (Exception e1) {
//                        e1.printStackTrace();
//                    }
//                } else {
//                    if( getNetCallback()!= null) {
//                        try {
//                            getNetCallback().onError(entity);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (entity.isLoginError()) {// 登录超期或必须登录才能访问
////                    AppSaveData.getUserVInfo().setOffLine();
//                    }
//                }
//            });
//        }
//        mHandler.post(()->iMessage.showMessage(entity));
//    }
//}
