package com.callphone.client.common;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

import com.callphone.client.base.App;
import com.callphone.client.base.SPConstants;
import com.hd.utils.save.IntValueSaveHelper;
import com.hd.utils.toast.ToastUtils;

/**
 * Created by liugd on 2019/4/11.
 */

public class DeviceHelper {


    Context mContext = App.getContext();
    private static final DeviceHelper helper = new DeviceHelper();
    KeyguardManager keyguardManager;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    KeyguardManager.KeyguardLock keyguardLock;
    private IntValueSaveHelper saveHelper = new IntValueSaveHelper(SPConstants.File_cache, SPConstants.KEY_mine_screen_mode);


    public IntValueSaveHelper getSaveHelper() {
        return saveHelper;
    }

    int wakeLevel;

    public boolean isScreenOff(){
        return wakeLevel==PowerManager.PARTIAL_WAKE_LOCK;
    }

    int levels[] = {PowerManager.SCREEN_DIM_WAKE_LOCK, PowerManager.PARTIAL_WAKE_LOCK, PowerManager.FULL_WAKE_LOCK};

    private DeviceHelper() {
        keyguardManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
        powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        wakeLevel = levels[saveHelper.getValue()];
    }

    public KeyguardManager getKeyguardManager() {
        return keyguardManager;
    }

    public PowerManager getPowerManager() {
        return powerManager;
    }


    /****
     * 亮屏
     */
    public void screenAcquire() {
        if (wakeLock == null) {
            wakeLock = powerManager.newWakeLock(wakeLevel, "brightXXAAACCC");
        }

        //
        if (keyguardLock == null) {
            keyguardLock = keyguardManager.newKeyguardLock("unLock");
        }
        //解锁
        keyguardLock.disableKeyguard();
        wakeLock.acquire();

    }
    public void screenAcquireTimeOut(int timeOut) {
        //重新启动一个用于拨号界面高亮显示
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "aaaffffaa");
        wakeLock.acquire(timeOut);
    }


    public void updateNewRequire(int which) {
        int wakeLevel0 = levels[which];
        if (wakeLevel0 != wakeLevel) {
            wakeLevel = wakeLevel0;
            saveHelper.setValue(which);
            screenRelease();
            screenAcquire();
            switch (wakeLevel0) {
                case PowerManager.SCREEN_DIM_WAKE_LOCK:
                    ToastUtils.show("切换到-低亮屏模式");
                    break;
                case PowerManager.PARTIAL_WAKE_LOCK:
                    ToastUtils.show("切换到-熄屏模式");
                    break;
                case PowerManager.FULL_WAKE_LOCK:
                    ToastUtils.show("切换到-高亮屏模式");
                    break;
            }
        }
    }


    /***
     * 熄屏
     */
    public void screenRelease() {
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }


    public static DeviceHelper getInstance() {
        return helper;
    }

    public boolean isKeyguardLocked() {
        return keyguardManager.isKeyguardLocked();
    }
}
