package com.hd.net;

import android.content.Context;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by liugd on 2018/12/23.
 */

public abstract class AbsNetHelper {

    /****
     * 网络全局参数
     */
    protected INetConfig netConfig =new DefaultNetConfig();


    public void setGlobalConfig(INetConfig netParmas) {
        this.netConfig = netParmas;
    }


    public abstract void newRequest(NetBuilder builder);



    public abstract void cancelTag(Context tag);




    public abstract void cancelAll();



    /**
     * obj 转成tag
     *
     * @param obj
     * @return
     */
    public static Object obj2Tag(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj.hashCode();
    }



    /***
     * 安全删除map里面的空值
     * @param mapParams
     */
    public static void delMapNullData(Map<String, String> mapParams) {
        for (Iterator<Map.Entry<String, String>> it = mapParams.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> item = it.next();
            String val = item.getValue();
            if (val == null) {
                it.remove();
            }
        }
    }
}
