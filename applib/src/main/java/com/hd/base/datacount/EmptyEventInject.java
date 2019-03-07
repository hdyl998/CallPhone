package com.hd.base.datacount;

import android.content.Context;

/**
 * Note：None
 * Created by lgd on 2019/1/4 14:30
 * E-Mail Address：986850427@qq.com
 */
public class EmptyEventInject implements IEventInject {

    @Override
    public void onPause(Context context, String key) {

    }

    @Override
    public void onResume(Context context, String key) {

    }

    @Override
    public void reportError(Throwable e) {

    }
}
