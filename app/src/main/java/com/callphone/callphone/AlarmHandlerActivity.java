package com.callphone.callphone;

import android.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hd.permission.PermissionHelper;
import com.hd.utils.DateUtils;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.toast.ToastUtils;

import java.util.Date;

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
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED //锁屏状态下显示
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD //解锁
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON //保持屏幕长亮
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); //打开屏幕
        setContentView(R.layout.activity_alarm_handler);
        textView = findViewById(R.id.text);
        String phone = getIntent().getStringExtra("phone");


        LogUitls.print(TAG, phone + "PHONE");

        textView.setText(String.format("拨打电话：" + phone));
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            textView.setText(String.format("拨打电话：" + phone));
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire();
            wl.release();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callPhone(phone);
            }
        }, 1000);
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
        LogUitls.print(TAG, "onNewIntent: 调用");
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            String phone = getIntent().getStringExtra("phone");


            LogUitls.print(TAG, phone + "PHONE");

            textView.setText(String.format("拨打电话：" + phone));
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire();
            wl.release();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callPhone(intent.getStringExtra("phone"));
            }
        }, 1000);
    }

}
