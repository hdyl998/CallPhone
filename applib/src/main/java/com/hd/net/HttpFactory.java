package com.hd.net;

/**
 * Created by liugd on 2018/12/23.
 */

public class HttpFactory {



    public  static AbsNetHelper netHelper=new OkHttpHelper();


    public static AbsNetHelper getNetHelper() {
        return netHelper;
    }


    public static void setNetHelper(AbsNetHelper netHelper) {
        HttpFactory.netHelper = netHelper;
    }
}
