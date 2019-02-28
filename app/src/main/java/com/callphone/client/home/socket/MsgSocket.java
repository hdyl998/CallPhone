package com.callphone.client.home.socket;

import com.callphone.client.appconfig.AppConfigFactory;
import com.callphone.client.base.data.AppSaveData;
import com.hd.net.socket.ISocketBase;
import com.hd.net.socket.MapBuilder;

/**
 * Created by liugd on 2019/3/1.
 */

public class MsgSocket extends ISocketBase {
    private final static MsgSocket instance=new MsgSocket();

    public static MsgSocket getInstance() {
        return instance;
    }

    @Override
    protected String setSocketUrl() {
        return AppConfigFactory.getConfig().getMsgSocketUrl();
    }

    public final static String change = "change";
    public final static String init = "init";

    public final static String hearting="hearting";

    public final static String err="err";


    public void sendMyInfo() {
        sendSockData(init, MapBuilder.create().add("ck", AppSaveData.getUserVInfo().getToken()));
    }


}
