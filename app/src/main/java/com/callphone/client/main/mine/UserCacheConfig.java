package com.callphone.client.main.mine;

import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.callphone.client.login.LoginSuccItem;
import com.callphone.client.mine.MinItem;
import com.hd.cache.ICacheConfig;

/**
 * Note：None
 * Created by Liuguodong on 2018/12/29 11:29
 * E-Mail Address：986850427@qq.com
 */
public class UserCacheConfig extends ICacheConfig {


    public String username = "";
    public String userid = "";
    public String token = "";
    public String headimg = "";
    public int isdaren;


    public void setUserInfo(LoginSuccItem info){
        this.userid=info.userid;
        this.token=info.usersn;
        //this.headimg=info.headimg;
        saveConfig();
    }

    public void setUserInfo(UserItem info){
        this.userid=info.userid;
        this.username=info.nickname;
        this.headimg=info.headimg;
        saveConfig();
    }
    public void setUserInfo(MinItem info){
        this.username=info.nickname;
        this.headimg=info.headimg;
        this.isdaren=info.isdaren;
        saveConfig();
    }

    public String getUid() {
        return userid;
    }

    @JSONField(serialize = false)
    public boolean isOffLine(){
        return TextUtils.isEmpty(token);
    }

    public String getToken() {
        return token;
    }
    public void setOffLine(){
        token=null;
    }

    @JSONField(serialize = false)
    public boolean isOnline(){
        return !isOffLine();
    }


    private final static UserCacheConfig INSTANCE=createFromCache(UserCacheConfig.class);

    public static UserCacheConfig get() {
        return INSTANCE;
    }

    /***
     * 是否是达人
     * @return
     */
    @JSONField(serialize = false)
    public boolean isDarenPlayer(){
        return 1==isdaren;
    }
}
