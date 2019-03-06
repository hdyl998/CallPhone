package com.hd.utils.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.hd.base.HdApp;
import com.hd.utils.NumberUtil;
import com.hd.utils.Utils;

import java.io.File;

/**
 * Created by liugd on 2017/8/2.
 */

public class UpdateUtils {
    /***
     * app缓存目录,卸载app会被清空
     * @return
     */
    public static String getAppCachePath() {
        if (isExistSDCard()) {
            return HdApp.getContext().getExternalCacheDir().getPath();
        } else {
            return HdApp.getContext().getCacheDir().getPath();
        }
    }

    /**
     * 方法名: hasSdcard
     * <p/>
     * 功能描述:检查是否存在SDCard
     *
     * @return 类型boolean:true:存在,false:不存在
     * <p/>
     * </br>throws
     */
    public static boolean isExistSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
    /**
     * @param curVer 当前app版本号
     * @param serVer 服务器版本号
     * @return boolean true:需要更新,false:不需要
     * @方法名: isNeedUpdate
     * @功能描述: 判断是否需要更新
     */
    public static boolean isNeedUpdate(String curVer, String serVer) {
        try {
            if (curVer.equals(serVer)) {
                return false;
            }
            //目前是四位版本号
            int[] curCode = {0, 0, 0, 0};
            int[] serCode = {0, 0, 0, 0};
            String current[] = curVer.split("\\.");
            String service[] = serVer.split("\\.");

            for (int i = 0; i < curCode.length && i < current.length; i++) {
                curCode[i] = NumberUtil.convertToInt(current[i]);
            }
            for (int i = 0; i < serCode.length && i < service.length; i++) {
                serCode[i] =  NumberUtil.convertToInt(service[i]);
            }
            for (int i = 0; i < curCode.length; i++) {
                if (curCode[i] < serCode[i]) {// 如果当前版本比服务器版本要小,则需要更新
                    return true;
                } else if (curCode[i] > serCode[i]) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;// 全部相等不需要更新
    }

    /**
     * 安装APK文件
     */
    public static void installApk(Context mContext, File apkfile) {
        if (apkfile.exists()) {
            Uri mUri;
            // 通过Intent安装APK文件
            Intent i = new Intent(Intent.ACTION_VIEW);

            if (Utils.isSupportAndroidNFeature()) {
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            mUri = Utils.file2Uri(apkfile);
            i.setDataAndType(mUri, "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//加上这句话，不会导致装APP 到一半时闪退
            mContext.startActivity(i);
        }
    }
}
