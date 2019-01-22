package com.hd.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class ProcessUtils {

    /****
     * 获得进程名字
     * @return
     */
    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 是否是主进程
     * @param context
     * @return
     */
    public static boolean isMainProcess(Context context) {
        String processName = getProcessName();
        if (!TextUtils.isEmpty(processName) && processName.equals(context.getPackageName())) {//判断进程名，保证只有主进程运行
            //在这里进行主进程初始化逻辑操作
            return true;
        }
        return false;
    }
}
