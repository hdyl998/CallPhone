package com.hd.net.socket;

import android.os.Handler;


import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.reflect.ReflectUtils;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * socket的基类，已将数据回调发送的主线程处理
 * create by liugd
 * socket消息订阅的基类
 */
public abstract class ISocketBase {
    protected Handler mHandler = new Handler();

    private Socket mSocket;// socket通信对象
    private final String LOG_TAG = getClass().getSimpleName();

    public Handler getHandler() {
        return mHandler;
    }


    private boolean isSubOnMainThread = true;


    public void setSubOnMainThread(boolean subOnMainThread) {
        isSubOnMainThread = subOnMainThread;
    }

    /***
     * 设置socket的URL
     *
     * @return
     */
    protected abstract String setSocketUrl();

    /***
     * socket是否连接成功
     *
     * @return
     */
    public boolean isConnectSuccess() {
        if (mSocket != null && mSocket.connected()) {
            return true;
        }
        return false;
    }


    //向socket发送消息
    public void sendSocketMessage(String event, String message) {
        if (mSocket != null) {
            LogUitls.print("sendSocketMessage", event + ":" + message);
            mSocket.emit(event, message);
        }
    }

    /***
     *发送JSON消息
     * @param order 命令
     * @param creator 参数构建器
     */
    public void sendSockData(String order, MapBuilder creator) {
        sendSocketMessage(order, creator.toString());
    }


    /***
     * 发送JSON消息
     * @param order 命令
     */
    public void sendSockData(String order) {
        sendSockData(order, MapBuilder.create());
    }

    /**
     * 停止Socket
     */
    public final void stopSocket() {
        if (mSocket == null)
            return;
        mSocket.disconnect();// 先断开连接
        mSocket.off();
        mSocket = null;
        removeAllListener();
        LogUitls.print(LOG_TAG, "停止Socket" + getClass().getSimpleName());
    }


    /***
     * 初始化socket的配置
     */
    private void initSocketOpinion() {
        try {
            IO.Options options = new IO.Options();
            options.transports = new String[]{"websocket", "flashsocket", "htmlfile", "xhr-multipart", "polling-xhr", "jsonp-polling"};
            options.reconnection = true;
            options.forceNew = true;
            options.upgrade = true;
            String url = setSocketUrl();
            int indexSymbol = url.lastIndexOf("/");
            if (indexSymbol > 7) {
                options.path = String.format("/%s/socket.io", url.substring(indexSymbol + 1));
                url = url.substring(0, indexSymbol);
            }
            mSocket = IO.socket(url, options);
            LogUitls.print(LOG_TAG + "地址", url);
            LogUitls.print(LOG_TAG + "path", options.path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 销毁
     */
    public void onDestory() {
        stopSocket();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void initLocalListener() {
        //定制本地默认协议
        String[] defaultDefines = {Socket.EVENT_CONNECT, Socket.EVENT_CONNECT_ERROR, Socket.EVENT_CONNECT_TIMEOUT, Socket.EVENT_DISCONNECT};
        for (String event : defaultDefines) {
            mSocket.on(event, new MyLocalEmitterListener(event));
        }
    }

    private void initServerLinstener() {
        //定制子类的协议
        List<String> userDefines = ReflectUtils.getAllPublicStaticFiledsValues(getClass());
        for (String string : userDefines) {
            mSocket.on(string, new MyServerEmitterListener(string));
        }
    }


    /***
     * 开启Socket通信
     */
    public void startSocket() {
        if (isConnectSuccess()) {
            LogUitls.print(LOG_TAG, "startSocket 已连接成功，无需再连接");
            return;
        }
        LogUitls.print(LOG_TAG, "startSocket 开始连接");
        stopSocket();
        if (mSocket == null) {
            initSocketOpinion();
        }
        initLocalListener();
        initServerLinstener();
        mSocket.connect();
    }


    protected List<SocketMessageListener> listeners;

    public void addOnGetSocketDataListener(SocketMessageListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>(3);//一般三个容量就足够了
        }
        if (listener != null && !this.listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeOnGetSocketDataListener(SocketMessageListener listener) {
        if (listeners != null && listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void removeAllListener() {
        if (listeners != null) {
            listeners.clear();
            listeners = null;
        }
    }

    private class MyLocalEmitterListener implements Emitter.Listener {
        private String event;

        public MyLocalEmitterListener(String event) {
            this.event = event;
        }

        @Override
        public void call(Object... args) {
            LogUitls.print("Socket消息 -local-", event + "/// " + listeners);
            if (isSubOnMainThread) {
                mHandler.post(() -> submitLocalMessage());
            } else {
                submitLocalMessage();
            }
        }

        private void submitLocalMessage() {
            try {
                if (listeners != null) {
                    for (SocketMessageListener listener : listeners) {
                        listener.onLocalMessage(event);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class MyServerEmitterListener implements Emitter.Listener {
        private String event;

        public MyServerEmitterListener(String event) {
            this.event = event;
        }



        @Override
        public void call(Object... args) {
            String backData;
            if (args != null && args.length > 0) {
                backData = args[0].toString();
            } else {
                backData = "";
            }
            LogUitls.print("socket消息 -server-", event + " " + backData);

            if (isSubOnMainThread) {
                mHandler.post(() -> submitServerMessage(backData));
            } else {
                submitServerMessage(backData);
            }
        }

        private void submitServerMessage(String backData) {
            if (listeners != null) {
                for (SocketMessageListener listener : listeners) {
                    try {
                        listener.onServerMessage(event, backData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}