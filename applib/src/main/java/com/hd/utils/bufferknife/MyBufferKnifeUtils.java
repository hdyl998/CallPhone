package com.hd.utils.bufferknife;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class MyBufferKnifeUtils {

//    @MyBindView(R.id.text)
//    private TextView textView;

    public static void inject(Object obj, View rootView) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                MyBindView bindView = field.getAnnotation(MyBindView.class);
                if (bindView != null) {
                    field.setAccessible(true);
                    View var=rootView.findViewById(bindView.value());
                    field.set(obj,var );
                    boolean isClick=bindView.click();
                    if(isClick&& obj instanceof View.OnClickListener){
                        var.setOnClickListener((View.OnClickListener) obj);
                    }
                }
            }
        } catch (Exception e) {
            RuntimeException runtimeException = new RuntimeException("注解异常");
            runtimeException.initCause(e);
            throw runtimeException;
        }
    }

    public static void injetClick(Fragment fragment) {
        injetClick(fragment, fragment.getView());
    }

    public static void injetClick(Activity activity) {
        injetClick(activity, activity.getWindow().getDecorView());
    }

    public static void injetClick(Object obj, View rootView) {
        try {
            Method[] methods = obj.getClass().getDeclaredMethods();
            for (Method method : methods) {
                MyOnClick onClick = method.getAnnotation(MyOnClick.class);
                if (onClick != null) {
                    for (int value : onClick.value()) {
                        rootView.findViewById(value).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                method.setAccessible(true);
                                try {
                                    method.invoke(obj, v);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            RuntimeException runtimeException = new RuntimeException("注解异常");
            runtimeException.initCause(e);
            throw runtimeException;
        }
    }

    public static void inject(Activity activity) {
        inject(activity, activity.getWindow().getDecorView());
    }

    public static void inject(Fragment fragment) {
        inject(fragment, fragment.getView());
    }

}
