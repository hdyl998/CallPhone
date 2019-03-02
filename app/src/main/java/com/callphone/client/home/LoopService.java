package com.callphone.client.home;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.callphone.client.R;
import com.callphone.client.home.socket.MsgSocket;
import com.callphone.client.main.MainNewActivity;
import com.callphone.client.main.mine.LoginManager;
import com.hd.base.adapterbase.SuperAdapter;
import com.hd.base.maininterface.IComCallBacks;
import com.hd.net.socket.MapBuilder;
import com.hd.net.socket.SocketMessageListener;
import com.hd.permission.PermissionHelper;
import com.hd.utils.DateUtils;
import com.hd.utils.Network;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.loopdo.HanderLoopHelper;
import com.hd.utils.toast.ToastUtils;

import java.util.Date;

/**
 * Created by liugd on 2019/1/19.
 */

public class LoopService extends Service {


    HanderLoopHelper loopHelper;
    public final static int FOREGROUND_SERVICE = 101;

    HanderLoopHelper handerLoopHelper;

    int nums = 0;
    PowerManager pm;

    Handler handler = new Handler();

    private void record() {
        //!Network.isConnected(LoopService.this)
        if (!pm.isScreenOn()) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "mywakelogtag");
            wl.acquire();
            addLog("唤醒屏幕");
        } else {
            addLog("唤醒屏幕 屏亮=" + pm.isScreenOn() + " net is Connected" + Network.isConnected(LoopService.this));
        }
    }

    //唤醒屏幕并解锁
    public void wakeUpAndUnlock() {

        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
    }

    /***
     * 唤醒屏幕并解锁
     * 高亮
     */
    public void fullWakeup() {

        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright1");
        wl.acquire();
    }


    private void stopLooper() {
        if (handerLoopHelper != null) {
            handerLoopHelper.stopLoop();
        }
        MsgSocket.getInstance().stopSocket();
        LogUitls.print(TAG, "stopLooper");
        isLooper = false;
    }


    boolean isLooper = false;


    private void startLooper() {
        if (isLooper) {
            return;
        }
        nums = 0;
        stopLooper();
        isLooper = true;
        LogUitls.print(TAG, "createLooper");
        MsgSocket.getInstance().addOnGetSocketDataListener(listener);
        MsgSocket.getInstance().startSocket();
        handerLoopHelper = new HanderLoopHelper();
        handerLoopHelper.setLoopTimeMillis(3000);
        handerLoopHelper.setLoopCallBacks(new IComCallBacks() {
            @Override
            public void call(Object obj) {
                nums++;
                if (MsgSocket.getInstance().isConnectSuccess()) {
                    MsgSocket.getInstance().sendSocketMessage("hearting", nums + "");
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            record();
                            wakeUpAndUnlock();
                        }
                    });
                }
                //每15秒唤醒一次
                if (nums % 5 == 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            wakeUpAndUnlock();
                        }
                    });
                }

            }
        });
        handerLoopHelper.startLoop();
    }

    public void addLog(String str) {
//        if (binder.logAdapter != null)
//            binder.logAdapter.add(DateUtils.getSimpleDate().format(new Date()) + " " + str + " islocked=" + km.isKeyguardLocked());
    }


    KeyguardManager km;

    public boolean aotoMobile(String phone) {
        fullWakeup();
        //拿到锁屏管理者
        if (km.isKeyguardLocked()) {
            Intent intent = new Intent();
            intent.setAction("com.callphone");
            intent.putExtra("phone", phone);
            this.sendBroadcast(intent);
            LogUitls.print(TAG, "发送广播" + phone);
            CallInfoItem historyItem = new CallInfoItem();
            historyItem.phone = phone;
            historyItem.status = 1;
            historyItem.updatetime = DateUtils.getSimpleDate().format(new Date());
            historyItem.extraMsg="(锁屏拨打)";
            binder.addCallInfoItem(historyItem);
            return true;
        } else {
            return callPhone(phone);
        }

    }

    @SuppressLint("MissingPermission")
    private boolean callPhone(String phone) {
        LogUitls.print(TAG, "callPhone" + phone);
        if (PermissionHelper.hasPermissions(android.Manifest.permission.CALL_PHONE)) {
            try {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);

                CallInfoItem historyItem = new CallInfoItem();
                historyItem.phone = phone;
                historyItem.status = 1;
                historyItem.updatetime = DateUtils.getSimpleDate().format(new Date());
                binder.addCallInfoItem(historyItem);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.show("没有权限，拨打电话失败`1");
                return false;
            }
        } else {
            ToastUtils.show("没有权限，拨打电话失败`1");
            return false;
        }
    }


    private LocalBinder binder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        init();
        return binder;
    }

    private void init() {
        pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        km = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        bindNotification();
    }


    private void bindNotification() {
        CharSequence text = "程序正在运行中，点击进入";
        CharSequence title = getText(R.string.app_name);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainNewActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle(title)  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .getNotification();
        startForeground(FOREGROUND_SERVICE, notification);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUitls.print(TAG, "onUnbind");
        if (loopHelper != null)
            loopHelper.stopLoop();
        binder.removeAll();
        if (handerLoopHelper != null)
            handerLoopHelper.stopLoop();
        return super.onUnbind(intent);
    }

    private static final String TAG = "LoopService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUitls.print(TAG, "onStartCommand");
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLooper();
        stopForeground(true);
    }

    long recordTime = System.currentTimeMillis();

    private boolean isMoreTime() {
        if (System.currentTimeMillis() - recordTime > 6000) {
            return true;
        }
        return false;
    }

    private void recordTime() {
        recordTime = System.currentTimeMillis();
    }


    private SocketMessageListener listener = new SocketMessageListener() {
        @Override
        public void onServerMessage(String event, String data) throws Exception {

            switch (event) {
                case MsgSocket.init:
                    JSONObject object = JSON.parseObject(data);
                    int code = object.getInteger("code");
                    String msg = object.getString("msg");
                    binder.setInfoText("服务初始化成功！");
                    break;
                case MsgSocket.change:
                    JSONObject obj1 = JSON.parseObject(data);
                    String phone = obj1.getString("target_phone");
                    String id = obj1.getString("id");
                    binder.setInfoText(String.format("拨打电话...(%s)", phone));
                    boolean isSuccess = aotoMobile(phone);
                    if (isSuccess) {
                        MsgSocket.getInstance().sendSockData("notice", MapBuilder.create().add("id", id));
                    }
                    recordTime();
                    break;
                case MsgSocket.hearting:
                    if (isMoreTime()) {
                        binder.setInfoText(String.format("已连接...(%s)", data));
                    }
                    break;
                case MsgSocket.err:
                    JSONObject object1 = JSON.parseObject(data);
                    int code1 = object1.getInteger("code");
                    String msg1 = object1.getString("msg");
                    ToastUtils.show(msg1);
                    binder.setInfoText(msg1);
                    switch (code1) {
                        case -1:
                        case -9999:
                            LoginManager.logout();
                            break;
                        default:
                            break;
                    }
                    recordTime();
                    break;
            }
        }

        @Override
        public void onLocalMessageStr(String note) throws Exception {
            binder.setInfoText(note);
        }

        @Override
        public void onLocalMessageConnect() throws Exception {
            //发送登录成功的消息
            MsgSocket.getInstance().sendMyInfo();
        }
    };


    /**
     * 创建Binder对象，返回给客户端即Activity使用，提供数据交换的接口
     */
    public class LocalBinder extends Binder {
        // 声明一个方法，getService。（提供给客户端调用）
        public LoopService getService() {
            // 返回当前对象LocalService,这样我们就可在客户端端调用Service的公共方法了
            return LoopService.this;
        }

        SuperAdapter<CallInfoItem> adapter;


        TextView tvInfo;

        public void setTvInfo(TextView tvInfo) {
            this.tvInfo = tvInfo;
        }

        public void setAdapter(SuperAdapter<CallInfoItem> adapter) {
            this.adapter = adapter;
        }

        public void removeAll() {
            tvInfo = null;
            this.adapter = null;
        }

        public void addCallInfoItem(CallInfoItem item) {
            if (adapter != null) {
                adapter.add(0, item);
            }
        }


        public void setInfoText(String text) {
            if (tvInfo != null) {
                tvInfo.setText(String.format("连接状态: %s", text));
            }
        }

        /***
         * 开始循环
         */
        public void startLooper() {
            setInfoText("开始连接...");
            LoopService.this.startLooper();
        }

        /***
         * 开始退出
         */
        public void stopLooper() {
            LoopService.this.stopLooper();
            setInfoText("连接断开!");
        }

    }

}
