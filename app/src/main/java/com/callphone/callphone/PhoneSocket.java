package com.callphone.callphone;

import com.hd.net.socket.ISocketBase;
import com.hd.net.socket.MapBuilder;

/**
 * Created by liugd on 2019/1/21.
 */

public class PhoneSocket extends ISocketBase {


    @Override
    protected String setSocketUrl() {
        return "http://47.106.179.199:8001";
    }

    public final static String change = "change";
    public final static String init = "init";

    public final static String hearting="hearting";

    public void sendMyInfo(String phone) {
        sendSockData(init, MapBuilder.create().add("phone", phone));
    }
}
