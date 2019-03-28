package com.callphone.client.webview;

import android.content.Context;
import android.text.TextUtils;

import com.hd.base.launch.AppLauncher;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Date:2017/11/7 14:28
 * Author:liugd
 * Modification:
 * ................
 * 佛祖保佑，永无BUG
 **/


public class WebViewManager {


    public static void launchWithAbsoluteUrl(Context mContext, String title, String absoluteUrl) {
        launch(mContext, ParamsBuilder.create().setTitle(title).setAbsoluteUrl(absoluteUrl));
    }

    /***
     * 启动WEBVIEW
     * @param mContext
     * @param builder 参数构建器
     */
    public static void launch(Context mContext, WebViewManager.ParamsBuilder builder) {
        AppLauncher.withFragment(mContext, WebViewFragment.class)
                .setObjs(builder)
                .launch(builder.getRequestCode());
    }





    public final static class ParamsBuilder implements Serializable {
        /***
         * fragment是以webview的形式展示的
         */
        public boolean isFragmentMode = false;
        public boolean isVip = false;//是否是会员
        public String title ;//标题
        public String absoluteUrl;//全链接，使用此链接

        public int requestCode = -1;

        public String data;//只加载数据形式


        public ParamsBuilder setData(String data) {
            this.data = data;
            return this;
        }

        public ParamsBuilder setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public int getRequestCode() {
            return requestCode;
        }

        private HashMap<String, String> addParams;


        public HashMap<String, String> getAddParams() {
            return addParams;
        }

        public static ParamsBuilder create() {
            return new ParamsBuilder();
        }


        public ParamsBuilder setVip(boolean vip) {
            isVip = vip;
            return this;
        }

        public ParamsBuilder setTitle(String title) {
            if (!TextUtils.isEmpty(title)) {
                this.title = title;
            }
            return this;
        }


        public ParamsBuilder setFragmentMode(boolean fragmentMode) {
            isFragmentMode = fragmentMode;
            return this;
        }


        public ParamsBuilder setAbsoluteUrl(String absoluteUrl) {
            this.absoluteUrl = absoluteUrl;
            return this;
        }

        public ParamsBuilder setAddParams(HashMap<String, String> addParams) {
            this.addParams = addParams;
            return this;
        }

        public ParamsBuilder addParams(String key, Object value) {
            if (value != null) {
                if (addParams == null) {
                    addParams = new HashMap<>();
                }
                addParams.put(key, value.toString());
            }
            return this;
        }
    }
}
