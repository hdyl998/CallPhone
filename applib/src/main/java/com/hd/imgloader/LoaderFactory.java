package com.hd.imgloader;

/**
 * @Description: 加载工厂，可定制图片加载框架
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 2016-12-19 15:16
 */
public class LoaderFactory {
    private static AbsLoader loader = new GlideLoader();

    public static AbsLoader getLoader() {
        return loader;
    }


    public static void setLoader(AbsLoader loader) {
        LoaderFactory.loader = loader;
    }
}
