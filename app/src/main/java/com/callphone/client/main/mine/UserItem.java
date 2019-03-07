package com.callphone.client.main.mine;

import com.alibaba.fastjson.annotation.JSONField;
import com.callphone.client.main.bean.BaseUInfoItem;

/**
 * Note：None
 * Created by lgd on 2018/12/28 14:52
 * E-Mail Address：986850427@qq.com
 */
public class UserItem extends BaseUInfoItem {
    public String token = "";

    public String status;//string 性别
    public String note;//格言


    public int sex = SEX_TYPE_UNKNOWN;//string 性别（0：女；1：男；-1：未知）


    public final static String SEX_STRING[] = {"男", "女", "保密"};

    @JSONField(serialize = false)
    public String getSexText() {
        switch (sex) {
            case SEX_TYPE_GIRL:
                return SEX_STRING[1];
            case SEX_TYPE_BOY:
                return SEX_STRING[0];
            default:
                return SEX_STRING[2];
        }
    }


    public final static int SEX_TYPE_GIRL = 0;
    public final static int SEX_TYPE_BOY = 1;
    public final static int SEX_TYPE_UNKNOWN = -1;

}
