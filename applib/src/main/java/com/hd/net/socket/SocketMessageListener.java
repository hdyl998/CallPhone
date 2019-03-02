package com.hd.net.socket;


import java.util.HashMap;

import io.socket.client.Socket;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public abstract class SocketMessageListener {

    //服务端传来的数据
    public abstract void onServerMessage(String event, String data) throws Exception;

    //本地事件
    public final void onLocalMessage(String event) throws Exception {
        switch (event) {
            case Socket.EVENT_CONNECT://连接
                onLocalMessageConnect();
                break;
            case Socket.EVENT_CONNECT_ERROR://错误
                onLocalMessageError();
                break;
            case Socket.EVENT_CONNECT_TIMEOUT://超时
                onLocalMessageTimeOut();
                break;
            case Socket.EVENT_DISCONNECT://断开连接
                onLocalMessageDisconnect();
                break;
        }
        onLocalMessageStr(hashMap.get(event));
    }

    final static HashMap<String,String>hashMap=new HashMap<>(4);
    final static String MSG_ERROR = "连接错误，请检查网络";
    final static String MSG_CONNECT = "连接成功";
    final static String MSG_TIMEOUT = "连接超时";
    final static String MSG_DISCONNECT = "连接已断开";

    static {
        hashMap.put(Socket.EVENT_CONNECT,MSG_CONNECT);
        hashMap.put(Socket.EVENT_CONNECT_ERROR,MSG_ERROR);
        hashMap.put(Socket.EVENT_DISCONNECT,MSG_DISCONNECT);
        hashMap.put(Socket.EVENT_CONNECT_TIMEOUT,MSG_TIMEOUT);
    }
//

    //本地事件
    public void onLocalMessageStr(String note) throws Exception {
    }

    //本地事件
    public void onLocalMessageConnect() throws Exception {
    }

    //本地事件
    public void onLocalMessageError() throws Exception {
    }

    //本地事件
    public void onLocalMessageTimeOut() throws Exception {
    }

    //本地事件
    public void onLocalMessageDisconnect() throws Exception {
    }
}
