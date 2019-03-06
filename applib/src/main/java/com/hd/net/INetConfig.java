package com.hd.net;

/**
 * Note：None
 * Created by lgd on 2018/12/19 18:03
 * E-Mail Address：986850427@qq.com
 */
public interface INetConfig {

    /***
     * 请求前的一些处理,设置全局请求HEADER,POST 参数,URL GET参数等
     * @param params
     */
    void onBeforeRequest(NetBuilder params);

    /***
     * 处理登录等消息
     * @param code
     */
    void onHandleCodeMessage(int code);


    /***
     * 获取baseUrl,全局设定,但优先以Builder的url为主
     * @return
     */
    String getBaseUrl();

}
