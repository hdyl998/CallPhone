package com.callphone.client.home;

import java.io.Serializable;

/**
 * Created by liugd on 2019/2/28.
 */

public class CallInfoItem implements Serializable{
    public String id;
    public String phone;
    public int status;
    public String updatetime;
    //  id: 日志id
//    phone：拨打的手机号
//    status：状态 1=成功 0=是吧
//    updatetime：更新时间


    public String getStatusString(){
        return status==1?"成功":"失败";
    }

    @Override
    public String toString() {
        return String.format("拨号id：%s\n手机号：%s\n状态：%s\n拨号时间：\n",id,phone,getStatusString(),updatetime);
    }
}
