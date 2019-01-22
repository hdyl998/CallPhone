package com.hd.utils.save;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import com.hd.base.HdApp;
import com.hd.cache.SpUtils;
import com.hd.utils.log.impl.LogUitls;

import java.util.HashSet;

/**
 * Created by Administrator on 2018/3/27.
 */

public class HashSetSaveHelper<T> {

    private String fileName;
    private String keyName;
    private HashSet<T> hashSet;//这里面的数据表示的是已经读过的数据
    private final Context mContext = HdApp.getContext();

    public HashSetSaveHelper(String fileName, String keyName) {
        this.fileName = fileName;
        this.keyName = keyName;
        this.hashSet = read();
    }


    /***
     * 存入
     */
    public void add(T t) {
        boolean isAdd = hashSet.add(t);//存一个字,hashset自带去重效果
        if (isAdd) {
            save();
        }
    }

    public void clear() {
        if (hashSet.size() != 0) {
            hashSet.clear();
            save();
        }
    }


    public void save() {
        SpUtils.putString( fileName, keyName, JSON.toJSONString(hashSet));
    }

    public HashSet<T> getHashSet() {
        return hashSet;
    }

    public boolean contains(Object o) {
        return hashSet.contains(o);
    }

    private HashSet<T> read() {
        String string = SpUtils.getString( fileName, keyName);
        if (string != null) {
            LogUitls.print("HASHSET 数据" + string);
            try {
                return JSON.parseObject(string, HashSet.class);
            } catch (Exception ex) {
            }
        }
        return new HashSet<>();
    }
}
