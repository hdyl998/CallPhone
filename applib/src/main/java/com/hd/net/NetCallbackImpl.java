package com.hd.net;

import com.hd.net.socket.NetEntity;

/***
 * 一个网络回调实现类，可以选择性的实现其中的一个方法
 */
public abstract class NetCallbackImpl<T> implements NetCallback<T> {

    @Override
    public void onSuccess(NetEntity<T> entity) throws Exception {
    }

    @Override
    public void onError(NetEntity entity) throws Exception {
    }
}