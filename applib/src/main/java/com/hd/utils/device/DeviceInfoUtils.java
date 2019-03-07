package com.hd.utils.device;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.hd.R;


/**
 * Note：None
 * Created by lgd on 2018/12/17 15:46
 * E-Mail Address：986850427@qq.com
 */
public class DeviceInfoUtils {


    /**
     * app track end
     */


    public static String getAppName(Context context) {
        PackageManager packageManager;
        ApplicationInfo applicationInfo;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            return (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return context.getString(R.string.app_name);
        }
    }
}
