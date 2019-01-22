package com.hd.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hd.base.HdApp;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * Note：None
 * Created by Liuguodong on 2018/12/19 14:21
 * E-Mail Address：986850427@qq.com
 */
public class Utils {
    public static String stringNotNull(String str) {
        return str == null ? "" : str;
    }


    public static boolean isListEmpty(List<?> mList) {
        if (mList == null || mList.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isArrayEmpty(Object[] objs) {
        if (objs == null || objs.length == 0) {
            return true;
        }
        return false;
    }

    public static String[] listString2Array(List<String> list) {
        String objs[] = new String[list.size()];
        list.toArray(objs);
        return objs;
    }

    /***
     * 安全地获得list的Item
     * @param list
     * @param position 位置
     * @param <T>通用参数
     * @return
     */
    public static <T> T getListItemSafely(List<T> list, int position) {
        if (isListEmpty(list)) {
            return null;
        }
        if (position < list.size() && position > -1) {
            return list.get(position);
        }
        return null;
    }

    /***
     * listString转成String,以逗号（，）隔开
     * @param tokens
     * @return
     */
    public static String listString2String(Iterable tokens) {
        return listString2String(tokens, ",");
    }


    /***
     * listString转成String,以逗号指定符号隔开
     * @param tokens
     * @param seprate 分隔符
     * @return
     */
    public static String listString2String(Iterable tokens, CharSequence seprate) {
        if (tokens == null) {
            return "";
        }
        return TextUtils.join(seprate, tokens);
    }


    /***
     * 重新启动Activity 以前的intent是保存的
     * @param mContext
     */
    public static void restartActivitySelf(Activity mContext) {
        Intent intent = mContext.getIntent();
        mContext.overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        mContext.finish();
        mContext.overridePendingTransition(0, 0);
        mContext.startActivity(intent);
    }


    public static void setViewVisibility(View view, boolean Visibility) {
        view.setVisibility(Visibility ? View.VISIBLE : View.GONE);
    }


    public static boolean isArrayContains(Object arrays[], Object target) {
        return Arrays.binarySearch(arrays, target) > -1;
    }

    public static boolean isJSONStringNULL(String string) {
        return string == null || "[]".equals(string) || "{}".equals(string);
    }


    public static String getVersionName() {
        try {
            return HdApp.getContext().getPackageManager().getPackageInfo(HdApp.getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return "";
    }

    /**
     * @return String
     * @方法名: getVersionCode
     * @功能描述:获得APK版本号
     */
    public static String getVersionCode() {
        try {
            return HdApp.getContext().getPackageManager().getPackageInfo(
                    HdApp.getContext().getPackageName(), 0).versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 类型String:手机版本
     * @方法名: getSystemAndroidVersion
     * @功能描述:获得手机版本
     */
    public static String getSystemAndroidVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * @return 类型String 手机型号
     * @方法名: getPhoneType
     * @功能描述:获得手机型号
     */
    public static String getPhoneType() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
     */
    public static String getAppMetaData(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = HdApp.getContext().getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(HdApp.getContext().getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }


    @TargetApi(11)
    public static void copyToClipBoard(Context context, String content) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context
                .CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("from qqsd", content));
    }


    /***
     * 是否支持7.0新特性
     * @return
     */
    public static boolean isSupportAndroidNFeature() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.M && HdApp.getContext().getApplicationInfo().targetSdkVersion > Build.VERSION_CODES.M;
    }

    public static Uri file2Uri(File vFile) {
        Uri mUri;
        if (isSupportAndroidNFeature()) {
            mUri = FileProvider.getUriForFile(HdApp.getContext(), HdApp.getContext().getPackageName() + ".provider", vFile);
        } else {
            mUri = Uri.fromFile(vFile); //7.0以下
        }
        return mUri;
    }

    /**
     * 设置VIEW的WEIGHTS
     *
     * @param views
     * @param weights
     */
    public static void setViewWeight(View views[], int weights[]) {
        int min = Math.min(views.length, weights.length);
        for (int i = 0; i < min; i++) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) views[i].getLayoutParams();
            layoutParams.weight = weights[i];
        }
    }




    public static boolean isObjectEmptyFinal(Object object) {
        return isObjectEmptyFinal(object, null);
    }


    /***
     * 对象是否为空终极判定
     *
     * @param object
     * @param  emptys 认为为空的字符串
     * @return
     */
    public static boolean isObjectEmptyFinal(Object object, List<String> emptys) {
        if (object == null) {
            return true;
        }
        if (object instanceof List) {
            return Utils.isListEmpty((List<?>) object);
        } else if (object instanceof Array) {
            return Array.getLength(object) == 0;
        }
        //一般只比较数据,String两种类型的数据类型
        //getFields()获得某个类的所有的公共（public）的字段，包括父类。
//        getDeclaredFields()获得某个类的所有申明的字段，即包括public、private和proteced，
//        但是不包括父类的申明字段。
        java.lang.reflect.Field[] flds = object.getClass().getFields();// 得到字段
        for (Field field : flds) {
            try {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers)) {//静态字段
                    continue;
                }
//                LogUitls.print("类型", field.getType() + "");
                Object obj = field.get(object);
                if (obj instanceof String) {
                    if (emptys != null) {
                        if (!emptys.contains(obj)) {
                            return false;
                        }
                    }
                } else if (obj instanceof Number) { //判断类型
                    Number o = (Number) obj;
                    //防止转换异常
                    if (o.longValue() > 0) {//默认非负数不为空
                        return false;
                    }
                } else {
                    if (obj != null) {
                        return false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
