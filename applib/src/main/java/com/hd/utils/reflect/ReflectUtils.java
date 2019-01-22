package com.hd.utils.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/13.
 */

public class ReflectUtils {

    /***
     * 通过反射得到本类所有的公有静态字段(不包括父类)
     * 所表示的所有的协议
     *
     * @param clazz 类名
     * @return
     */
    public static List<String> getAllPublicStaticFiledsValues(Class clazz) {
        List<String> listString = new ArrayList<>();
        Field fields[] = clazz.getDeclaredFields();//获得本类所有的静态公有字段(不包括父类)
        try {
            for (int i = 0; i < fields.length; i++) {
                int modifiers = fields[i].getModifiers();
                if (fields[i].getType() == String.class && Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {//静态公有字段并且是String类型
                    listString.add(fields[i].get(null) + "");
                }
            }
        } catch (Exception e) {
        }
        return listString;
    }
}
