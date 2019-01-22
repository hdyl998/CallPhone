package com.hd.net.coderemind;

import com.hd.net.socket.NetEntity;

/**
 * Created by liugd on 2017/3/15.
 */

public class SuccessMessage implements IMessage {


    @Override
    public boolean isShowMsg(NetEntity netEntity) {
        return netEntity.code >= 0;
    }
}
