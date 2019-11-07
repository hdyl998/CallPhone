package com.callphone.client;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.callphone.client.base.App;
import com.callphone.client.common.DeviceHelper;
import com.hd.base.datacount.EventInjectHelper;
import com.hd.permission.PermissionHelper;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.other.MyRunTimeException;
import com.hd.utils.toast.ToastUtils;

/**
 * Created by liugd on 2019/1/19.
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    public static final String CMD="com.callphone.start";

    @Override
    public void onReceive(Context context, Intent intent) {

        LogUitls.print(TAG, "onReceive" + intent.getStringExtra("phone"));

//
        Intent alarmIntent = new Intent(context, AlarmHandlerActivity.class); //携带数据
        alarmIntent.putExtra("phone", intent.getStringExtra("phone")); //activity需要新的任务栈
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(alarmIntent);
        DeviceHelper.getInstance().screenAcquire();
        callPhone(context, intent.getStringExtra("phone"));

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
                App.getContext().reportError(e,"MyReceiver没有权限，拨打电话失败`1");
            }
        } else {
            ToastUtils.show("没有权限，拨打电话失败`1");
            App.getContext().reportError("MyReceiver没有权限，拨打电话失败`1");
        }
    }
}
