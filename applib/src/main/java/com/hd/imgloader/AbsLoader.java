package com.hd.imgloader;

import android.content.Context;
import android.support.annotation.IntDef;
import android.widget.ImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Description: 图片加载接口
 * @author: <a href="http://xiaoyaoyou1212.360doc.com">DAWI</a>
 * @date: 2016-12-19 15:04
 */
public abstract class AbsLoader {


    protected LoaderInterceptor interceptor;


    public void setInterceptor(LoaderInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public abstract void loadImage(LoaderBuilder params);

    public abstract void loaderTest(LoaderBuilder params);


    public abstract void clearMemoryCache();

    public abstract void clearDiskCache();
}
