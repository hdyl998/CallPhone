package com.hd.imgloader;

/**
 * Created by liugd on 2018/12/23.
 */

public interface LoaderInterceptor {

    /***
     * 返回true表示拦截
     * @param builder
     * @return
     */
    boolean intercept(LoaderBuilder builder);
}
