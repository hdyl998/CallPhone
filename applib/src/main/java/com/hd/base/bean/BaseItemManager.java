package com.hd.base.bean;

import android.content.Context;


import com.hd.utils.log.impl.LogUitls;

import java.util.HashMap;

/**
 * 创建和context绑定的单例实例的基类
 * Date:2017/10/16 13:57
 * Author:liugd
 * Modification:创建一个同activity绑定的对象静态实例，这样可以在同一context下方便使用，而不用传值，传来传去的麻烦死了
 * 当context改变时，创建新的对象
 * 当context销毁时，销毁当前对象
 **/

public class BaseItemManager<T> {

//    private final static BaseItemManager instance = new BaseItemManager();
//
//    public static BaseItemManager getInstance() {
//        return instance;
//    }

    private Class<T> clazz;
    private HashMap<Integer, T> hashMap = new HashMap<>();

    public BaseItemManager(Class<T> clazz) {
        this.clazz = clazz;
    }

    private int context2Key(Context context) {
        return context.hashCode();
    }

    /***
     * 获取scoreItem
     *
     * @param context
     * @return
     */
    public T getItem(Context context) {
        T item = hashMap.get(context2Key(context));
        if (item == null) {
            LogUitls.print("scoreItem", "创建scoreItem" + context);
            try {
                item = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            putItem(context, item);
        }
        return item;
    }

    /***
     * 添加新的scoreitem
     *
     * @param context
     * @param item
     */
    public void putItem(Context context, T item) {
        hashMap.put(context2Key(context), item);
    }

    /***
     * 移除scoreItem，减少数据存储
     *
     * @param context
     */
    public void removeItem(Context context) {
        if (context != null) {
            hashMap.remove(context2Key(context));
            LogUitls.print("scoreItem", "移除scoreItem" + context);
        }
    }
}
