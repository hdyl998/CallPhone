package com.hd.net.coderemind;

import com.hd.net.socket.NetEntity;

/**
 * 什么也不显示
 * Created by liugd on 2017/3/15.
 */

public class NoMessage implements IMessage {

    @Override
    public boolean isShowMsg(NetEntity netEntity) {
        return false;
    }
}
