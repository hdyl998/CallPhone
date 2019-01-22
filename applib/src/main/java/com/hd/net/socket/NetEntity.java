package com.hd.net.socket;

import android.text.TextUtils;
import android.util.SparseArray;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.hd.net.DefaultNetDataBean;

import java.io.Serializable;
import java.util.List;

/**
 * 网络请求返回的实体类
 * Created by liugd on 2017/3/17.
 */

public class NetEntity<T> extends DefaultNetDataBean implements Serializable {
    public Object flag;//标志
    public String api;
    public T dataBean;//数据

    public T getDataBean() {
        return dataBean;
    }


    private boolean isCache;//是否是缓存的标志

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public void setFlag(Object flag) {
        this.flag = flag;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getFlag() {
        return flag;
    }

    @JSONField(serialize = false) //不去序列化免得
    public int getFlagAsInteger() {
        return (int) flag;
    }

    @JSONField(serialize = false) //不去序列化免得
    public boolean getFlagAsBoolean() {
        return (boolean) flag;
    }

    public String getMsg() {
        return msg;
    }

    public String getData() {
        return data;
    }

//    @JSONField(serialize = false)
//    public Context getContext() {
//        return context;
//    }
//
//    public void setContext(Context context) {
//        this.context = context;
//    }


    public boolean isLoginError() {
        return ERROR_NOT_LOGIN == code;
    }

    public boolean isParaseError() {
        return ERROR_PARASE == code;
    }


    /***
     * 解析成指定的对象类型
     *
     * @param clazz 类型
     * @param <T>   class
     * @return
     */
    @JSONField(serialize = false) //不去序列化免得
    public <T> T getObjectData(Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }

    @JSONField(serialize = false) //不去序列化免得
    public JSONObject getJSONObject() {
        return JSON.parseObject(data);
    }

    /***
     * 解析成指定的数组类型
     *
     * @param clazz 类型
     * @param <T>   class
     * @return
     */
    @JSONField(serialize = false) //不去序列化免得
    public <T> List<T> getArrayData(Class<T> clazz) {
        return JSON.parseArray(data, clazz);
    }

    /***
     * 判断返回的数据是否为空
     *
     * @return
     */
    @JSONField(serialize = false) //不去序列化免得
    public boolean isDataEmpty() {
        return TextUtils.isEmpty(data) || "[]".equals(data) || "{}".equals(data);
    }

    /***
     * 自定义的一些错误
     */
    public static final int ERROR_DEFAULT = -10085;//其它异常
    public static final int ERROR_ConnectException = -10088;//连接异常
    public static final int ERROR_TIME_OUT = -10086;//超时异常
    public static int ERROR_NOT_LOGIN = -18;


    public static final int ERROR_PARASE = -10087;

    public static final int ERROR_DOUBLE_CHECK = -113;


    public final static SparseArray<String> msgContainer = new SparseArray<>(2);

    static {
        msgContainer.put(ERROR_PARASE, "服务器异常~");
        msgContainer.put(ERROR_DEFAULT, "居然崩溃了~");
    }


    public String getCodeString() {
        if(TextUtils.isEmpty(msg)){
            return msgContainer.get(code);
        }
        return msg;
    }

}
