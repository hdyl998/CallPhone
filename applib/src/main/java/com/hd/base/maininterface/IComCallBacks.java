package com.hd.base.maininterface;

/**
 * 通用接口回调，参数为任意长度参数,需要回调时可采用这个接口，无需去重新定义
 * Created by liugd on 2017/1/13.
 */

public interface IComCallBacks<T> {
    void call(T obj);
}
