package com.hd.base.datacount;

import android.content.Context;

/**
 * Note：None
 * Created by lgd on 2019/1/4 14:12
 * E-Mail Address：986850427@qq.com
 */
public interface IEventInject {

    void onPause(Context context,String key);
    void onResume(Context context,String key);

    void reportError(Throwable e);
}
