package com.hd.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;


import com.hd.R;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.toast.ToastUtils;

/**
 * 应用跳转工具
 * Created by liugd on 2017/3/14.
 */

public class GoUtils {
    /***
     * 去应用市场
     */
    public static void goMarket(Context mContext) {
        try {
            Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
            LogUitls.print("uri", uri);
            Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent2);
        } catch (Exception e) {
            ToastUtils.show("抱歉,没有找到应用市场");
        }
    }


    public static void goMobileBrowser(Context mContext, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show(R.string.tips_fail_intent);
        }
    }

    public static void goDownload(Context mContext, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.show(R.string.tips_fail_intent);
        }
    }

    /***
     * 通过scheme唤起app
     * @param mContext
     * @param url
     */
    public static void goSchemeIntent(Context mContext, String url) {
        try {          // 以下固定写法
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();  // 防止没有安装的情况
            ToastUtils.show(R.string.tips_fail_intent);
        }
    }


    /***
     * 打开程序详情界面
     * @param mContext
     */
    public static void goAppDetailsSetting(Context mContext) {
        try {
            final Intent i = new Intent();
            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.setData(Uri.parse("package:" + mContext.getPackageName()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            mContext.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 去开发者选项
     *
     * @param mContext
     */
    public static void goDevelopmentSetting(Context mContext) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 去系统版本
     *
     * @param mContext
     */
    public static void goSystemVersionSetting(Context mContext) {
        try {
            Intent intent = new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 分享文本
     * @param mContext
     * @param text
     */
    public static void goShareText(Context mContext, String text) {
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, text);
        mContext.startActivity(Intent.createChooser(textIntent, "分享"));
    }


}
