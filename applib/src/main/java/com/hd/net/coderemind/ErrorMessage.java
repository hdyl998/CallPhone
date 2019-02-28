package com.hd.net.coderemind;

import com.hd.net.socket.NetEntity;

/**
 * Created by liugd on 2017/3/15.
 */

public class ErrorMessage implements IMessage {


    @Override
    public boolean isShowMsg(NetEntity netEntity) {
        return netEntity.CODE < 0 ;//&& !netEntity.isLoginError();
    }
}
