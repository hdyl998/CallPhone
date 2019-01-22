package com.callphone.callphone;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;

import com.hd.permission.PermissionHelper;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.toast.ToastUtils;
import com.hd.view.EmptyLayout;

/**
 * Created by liugd on 2019/1/19.
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        LogUitls.print(TAG, "onReceive" + intent.getStringExtra("phone"));

//
//        Intent alarmIntent = new Intent(context, AlarmHandlerActivity.class); //携带数据
//        alarmIntent.putExtra("phone", intent.getStringExtra("phone")); //activity需要新的任务栈
//        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//        context.startActivity(alarmIntent);


        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (!pm.isScreenOn()) {
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                wl.acquire();
                wl.release();
            }
            callPhone(context, intent.getStringExtra("phone"));
//            Intent alarmIntent = new Intent(context, AlarmHandlerActivity.class);
//            alarmIntent.putExtra("phone",intent.getStringExtra("phone"));
//            alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(alarmIntent);
        }
        else {
            callPhone(context, intent.getStringExtra("phone"));
        }

    }


    @SuppressLint("MissingPermission")
    private void callPhone(Context context, String phone) {

        if (PermissionHelper.hasPermissions(android.Manifest.permission.CALL_PHONE)) {
            try {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.show("没有权限，拨打电话失败`1");
            }
        } else {
            ToastUtils.show("没有权限，拨打电话失败`1");
        }
    }
}
