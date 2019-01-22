package com.hd.net;

import com.alibaba.fastjson.JSON;
import com.hd.net.socket.NetEntity;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.reflect.ClassUtils;

import java.lang.reflect.Type;
import java.util.List;

public interface NetCallback<T> {

    void onSuccess(NetEntity<T> entity) throws Exception;

    void onError(NetEntity entity) throws Exception;


    String TAG = "NetCallback";


    /***
     * 把请求到的数据转换成对应的bean
     * @param response
     * @return
     */
    default INetDataBean getNetDataProvider(String response){
        return JSON.parseObject(response, DefaultNetDataBean.class);
    }


    /***
     * 解析数据(在子线程中处理)
     * @param item 返回的实体类
     * @param response 解析用到的数据
     */
    default void doParse(NetEntity<T> item, String response) throws Exception {
        INetDataBean inter=getNetDataProvider(response);
        item.code=inter.getCode();
        item.msg=inter.getMsg();
        if (item.code > -1) {//0及以上为正常
            item.data = inter.getData();
            try {
                item.dataBean = onParseData(item.data);
                LogUitls.print(TAG, "dataBean是否为空" + (item.dataBean == null));
                if (item.dataBean instanceof List) {
                    LogUitls.print(TAG, "item.dataBean 为 list size=" + ((List) item.dataBean).size());
                }
            } catch (Exception e) {
                e.printStackTrace();
                item.dataBean = null;
                LogUitls.print(TAG, "解析数据失败");
                throw new RuntimeException("解析数据失败"+item.data);
            }
        } else {
            item.dataBean = null;
        }
    }


    /***
     * 默认解析方式,根据传入泛型去解析,可以覆盖这个方法自定义解析方式
     * @param data
     * @return
     */
    default T onParseData(String data) {
        Type type = ClassUtils.getClassType(this.getClass());//获取泛型的类型

        LogUitls.printString(TAG, this.getClass() + "第一个泛型参数是" + type);
        if (type == null) {
            return null;
        }
        //string类型直接转换
        if (type == String.class) {
            return (T) data;
        }
        return JSON.parseObject(data, type);
    }


}