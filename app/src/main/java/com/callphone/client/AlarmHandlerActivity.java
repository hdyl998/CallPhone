package com.callphone.client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.callphone.client.common.DeviceHelper;
import com.hd.permission.PermissionHelper;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.toast.ToastUtils;

/**
 * Created by liugd on 2019/1/19.
 */

public class AlarmHandlerActivity extends Activity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUitls.print(TAG, "onCreate:启动了消息内容的activity ");
        Window win = getWindow();
        int flag = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED //锁屏状态下显示
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD //解锁
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        if (!DeviceHelper.getInstance().isScreenOff()) {
            flag |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; //保持屏幕长亮
        }
        win.addFlags(flag); //打开屏幕
        setContentView(R.layout.activity_alarm_handler);
        textView = findViewById(R.id.text);

        textView.setText(getString(R.string.app_name) + "正在运行中...");
        DeviceHelper.getInstance().screenAcquire();
    }


    @SuppressLint("MissingPermission")
    private void callPhone(String phone) {

        if (PermissionHelper.hasPermissions(android.Manifest.permission.CALL_PHONE)) {
            try {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.show("没有权限，拨打电话失败`1");
            }
        } else {
            ToastUtils.show("没有权限，拨打电话失败`1");
        }
    }

    private static final String TAG = "AlarmHandlerActivity";


    @Override
    protected void onNewIntent(Intent intent) {
        DeviceHelper.getInstance().screenAcquire();
    }

}
