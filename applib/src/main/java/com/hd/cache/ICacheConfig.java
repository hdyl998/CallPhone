package com.hd.cache;

import com.alibaba.fastjson.JSON;
import com.hd.utils.log.impl.LogUitls;

/**
 * Note：None
 * Created by Liuguodong on 2018/12/29 14:55
 * E-Mail Address：986850427@qq.com
 */
public abstract class ICacheConfig {

    /***
     * 存档
     */
    public void saveConfig() {
        SpUtils.putString(SpConsts.File_cache_config, clazz2Key(getClass()), JSON.toJSONString(this));
        LogUitls.print("存档", this);
    }

    /***
     * clazz得到key
     * @param clazz
     * @return
     */
    public static String  clazz2Key(Class clazz){
        return clazz.getSimpleName();
    }
    /***
     * 从缓存里面创建实例
     * @param clazz
     * @param <T>
     * @return
     */
    protected static <T> T createFromCache(Class<T> clazz) {
        String string = SpUtils.getString(SpConsts.File_cache_config, clazz2Key(clazz));
        try {
            if (string != null) {
                T config = JSON.parseObject(string, clazz);
                LogUitls.print(clazz.getSimpleName(),config);
//                //从上次存档的地方读取配置,如果配置超过指定时间则存档
//                checkTimeAndSave(config);
                return config;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
