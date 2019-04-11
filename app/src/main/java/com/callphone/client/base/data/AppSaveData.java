package com.callphone.client.base.data;

import com.callphone.client.main.mine.UserCacheConfig;

/**
 * Note：None
 * Created by lgd on 2018/12/24 17:06
 * E-Mail Address：986850427@qq.com
 */
public class AppSaveData {


    private final static AppSaveData instance=new AppSaveData();


    public static AppSaveData get() {
        return instance;
    }

    public static AppConst getAppConst() {
        return AppConst.getInstace();
    }



    public static UserCacheConfig getUserVInfo() {
        return UserCacheConfig.get();
    }

}
