package com.hd.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * class的工具类
 * Created by liugd on 2017/4/15.
 */

public class ClassUtils {

    /***
     * 得到T的class
     *
     * @return
     */
    public static Type getClassType(Class<?> t) {
        Type genType = t.getGenericSuperclass();
        System.out.println(genType);
        if (!(genType instanceof ParameterizedType)) {
            System.out.println("没有填写泛型参数1");
            return null;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        Type finalNeedType;
        if (params.length > 1) {
            if (!(type instanceof ParameterizedType)) {
                System.out.println("没有填写泛型参数");
                return null;
            }
            finalNeedType = ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            finalNeedType = type;
        }
        return finalNeedType;
    }

//
//    /***
//     * 得到T的class
//     *
//     * @return
//     */
//    public static <T> Class<T> getClassType(Class<?> clz) {
//        return getClassType(clz, 0);
//    }
//
//    public static <T> Class<T> getClassType(Class<?> clz, int index) {
//        try {
//            //为了得到T的Class，采用如下方法
//            //1得到该泛型类的子类对象的Class对象
//            //2得到子类对象的泛型父类类型（也就是BaseDaoImpl<T>）
//            ParameterizedType type = (ParameterizedType) clz.getGenericSuperclass();
//            Type[] types = type.getActualTypeArguments();
//            return (Class<T>) types[index];
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public static <T> T getClassNewInstance(Class<T> sourcesClazz, Object... params) {
        try {
            Class<?>[] parameterTypes = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                parameterTypes[i] = params[i].getClass();
            }
            Constructor<T> constructor = sourcesClazz.getConstructor(parameterTypes);
            T instance = constructor.newInstance(params);
            return instance;
        } catch (Exception e) {
        }
        return null;
    }
}
