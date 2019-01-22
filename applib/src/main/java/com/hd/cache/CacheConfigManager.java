package com.hd.cache;

import com.alibaba.fastjson.JSON;
import com.hd.utils.log.impl.LogUitls;

import java.util.HashMap;

/**
 * Created by liugd on 2018/12/27.
 */

public final class CacheConfigManager {


    private HashMap<Class, Object> dataMap = new HashMap<>();


    private static final CacheConfigManager INSTANCE = new CacheConfigManager();


    private CacheConfigManager(){

    }

    public static <T> T get(Class<T> clazz) {
        return (T) INSTANCE.getObj(clazz);
    }

    public static void saveConfig(Class clazz){
        Object object=INSTANCE.getObj(clazz);
        SpUtils.putString(SpConsts.File_cache_config,ICacheConfig.clazz2Key(clazz), JSON.toJSONString(object));
        LogUitls.print("存档", object);
    }

    private Object getObj(Class clazz) {
        Object t =  dataMap.get(clazz);
        if (t == null) {
            t=ICacheConfig.createFromCache(clazz);
            dataMap.put(clazz,t);
        }
        return t;
    }






}
