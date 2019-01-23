package com.callphone.callphone;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hd.base.HdApp;
import com.hd.base.adapterbase.SuperAdapter;
import com.hd.base.maininterface.IComCallBacks;
import com.hd.net.NetBuilder;
import com.hd.net.NetCallbackImpl;
import com.hd.net.socket.MapBuilder;
import com.hd.net.socket.NetEntity;
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

    public static class DataItem {
        public int type;
        //public string msg;
        //public int code;
        public String person;
        public String Data;
        public final static int TYPE_MSG = 0;
        public final static int TYPE_MSG_SERVER = 1;
        public final static int TYPE_DOWNLINE = 2;
    }

    private void closeSocket() {
        if (socket != null) {
            socket.stopSocket();
        }
    }

    static boolean isRequest = true;

    PhoneSocket socket;

    HanderLoopHelper handerLoopHelper;

    int nums = 0;
    PowerManager pm;

    Handler handler = new Handler();

    private void record() {
        //!Network.isConnected(LoopService.this)
        if (!pm.isScreenOn()) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "mywakelogtag");
            wl.acquire();
            wl.release();

            addLog("唤醒屏幕");
        } else {
            addLog("唤醒屏幕 屏亮=" + pm.isScreenOn() + " net is Connected" + Network.isConnected(LoopService.this));
        }
    }
    //唤醒屏幕并解锁
    public void wakeUpAndUnlock(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }
    /**
     * author: wu
     * date: on 2018/11/14.
     * describe: 静态常量类
     */

    public static class Constant {
        public static Integer TYPE1 = 1;
        public static Integer TYPE2 = 2;
        public static Integer TYPE3 = 3;
    }

    //设置通知栏消息样式
    private void setNotification(int type) {
        //点击通知栏消息跳转页
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        //创建通知消息管理类
        Notification notification;
        NotificationManager  manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)//创建通知消息实例
                .setContentTitle("我是标题")
                .setContentText("我是内容")
                .setWhen(System.currentTimeMillis())//通知栏显示时间
                .setSmallIcon(R.mipmap.ic_launcher)//通知栏小图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))//通知栏下拉是图标
                .setContentIntent(pendingIntent)//关联点击通知栏跳转页面
                .setPriority(NotificationCompat.PRIORITY_MAX)//设置通知消息优先级
                .setAutoCancel(true)//设置点击通知栏消息后，通知消息自动消失
//                .setSound(Uri.fromFile(new File("/system/MP3/music.mp3"))) //通知栏消息提示音
                .setVibrate(new long[]{0, 1000, 1000, 1000}) //通知栏消息震动
                .setLights(Color.GREEN, 1000, 2000) //通知栏消息闪灯(亮一秒间隔两秒再亮)
                .setDefaults(NotificationCompat.DEFAULT_ALL); //通知栏提示音、震动、闪灯等都设置为默认

        if (type == 1) {
            //短文本
            notification = builder.build();
            //Constant.TYPE1为通知栏消息标识符，每个id都是不同的
            manager.notify(Constant.TYPE1, notification);
        } else if (type == 2) {
            //长文本
            notification = builder.setStyle(new NotificationCompat.BigTextStyle().
                    bigText("我是长文字内容:　今年双十一结束后，一如既往又出现了一波冲动剁手党被理智唤醒的退货潮。不过，一位来自福建厦门的网友在这其中贡献了堪称历史里程碑式的高光时刻。别人退衣服退鞋子，而他要退的是一只蓝孔雀、一只宠物小香猪、还有一斤娃娃鱼……"))
                    .build();
            manager.notify(Constant.TYPE2, notification);
        } else {
            //带图片
            notification = builder.setStyle(new NotificationCompat.BigPictureStyle().
                    bigPicture(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)))
                    .build();
            manager.notify(Constant.TYPE3, notification);
        }
    }


    private void createLooper() {

        if (handerLoopHelper != null) {
            handerLoopHelper.stopLoop();
        }
        pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        km = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        closeSocket();

        socket = new PhoneSocket();


        socket.addOnGetSocketDataListener(new SocketMessageListener() {
            @Override
            public void onServerMessage(String event, String data) throws Exception {
                switch (event) {
                    case PhoneSocket.init:
                        JSONObject object = JSON.parseObject(data);
                        int code = object.getInteger("code");
                        String msg = object.getString("msg");
                        ToastUtils.show(code + msg);
                        break;
                    case PhoneSocket.change:
                        JSONObject obj1 = JSON.parseObject(data);
                        String phone = obj1.getString("target_phone");
                        isLocked = km.isKeyguardLocked();
                        if (binder.logAdapter != null)
                            binder.logAdapter.add(DateUtils.getSimpleDate().format(new Date()) + "isLocked=" + isLocked + phone);
                        aotoMobile(phone);
                        break;
                    case PhoneSocket.hearting:
                        addLog(event + " " + data);
                        break;
                }
            }

            @Override
            public void onLocalMessageStr(String note) throws Exception {
                if (binder.tvSocketInfo != null) {
                    binder.tvSocketInfo.setText(note);
                }
                addLog(note);
                if (!note.contains("成功")) {
                    record();
                    setNotification(1);
                    wakeUpAndUnlock(LoopService.this);
                }
            }

            @Override
            public void onLocalMessageConnect() throws Exception {
                socket.sendMyInfo(localPhoneNum);
            }
        });
        socket.startSocket();
        handerLoopHelper = new HanderLoopHelper();
        handerLoopHelper.setLoopTimeMillis(3000);
        handerLoopHelper.setLoopCallBacks(new IComCallBacks() {
            @Override
            public void call(Object obj) {
                nums++;
                if (socket.isConnectSuccess()) {
                    socket.sendSocketMessage("hearting", nums + "");
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            record();
                            setNotification(1);
                            wakeUpAndUnlock(LoopService.this);
                        }
                    });
                }

                if(nums%20==0){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setNotification(1);
                            wakeUpAndUnlock(LoopService.this);
                        }
                    });
                }

            }
        });
        handerLoopHelper.startLoop();
//        if(!isRequest) {
//
//        }
//        else {
////            if (loopHelper == null) {
////                loopHelper = new HanderLoopHelper();
////                loopHelper.setLoopTimeMillis(1 * 1000);
////                loopHelper.setLoopCallBacks(new IComCallBacks() {
////                    @Override
////                    public void call(Object obj) {
////                        if (km.isKeyguardLocked()) {
////                            isLocked = true;
////                            request();
////                        } else if (times % 3 == 0) {
////                            isLocked = false;
////                            request();
////                        }
////                        times++;
////                    }
////                });
////            }
////            loopHelper.startLoop();
//        }


    }

    public void addLog(String str) {
        if (binder.logAdapter != null)
            binder.logAdapter.add(DateUtils.getSimpleDate().format(new Date()) + " " + str + " islocked=" + km.isKeyguardLocked());
    }

    boolean isLocked = false;

    int times = 0;

    public String localPhoneNum;

    KeyguardManager km;

    public void aotoMobile(String phone) {
        //拿到锁屏管理者
        if (km == null) {
            if (binder.logAdapter != null)
                binder.logAdapter.add(DateUtils.getSimpleDate().format(new Date()) + " KeyguardManager is Null");
        } else {
            if (km.isKeyguardLocked()) {

//                Notification.Builder messageNotification = new  Notification.Builder(getApplication());
//                messageNotification.setDefaults(Notification.DEFAULT_ALL);
//                messageNotification.setAutoCancel(true);
//                Notification notification  = messageNotification.build();
//                NotificationManager  messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                messageNotificatioManager.cancel(8848);
//                messageNotificatioManager.notify(8848,notification);


                Intent intent = new Intent();
                intent.setAction("com.callphone");
                intent.putExtra("phone", phone);
                this.sendBroadcast(intent);
                LogUitls.print(TAG, "发送广播" + phone);


                HistoryItem historyItem = new HistoryItem();
                historyItem.isCallByHand = false;
                historyItem.targetPhone = phone;
                historyItem.date = DateUtils.getSimpleDate().format(new Date()) + "(锁屏拨打)";
                if (binder.adapter != null)
                    binder.adapter.add(0, historyItem);

            } else {

//                Intent intent = new Intent();
//                intent.setAction("com.callphone");
//                intent.putExtra("phone", phone);
//                this.sendBroadcast(intent);
//                LogUitls.print(TAG, "发送广播" + phone);

                callPhone(phone, false);
            }
        }

    }

    @SuppressLint("MissingPermission")
    private void callPhone(String phone, boolean isCallByHand) {
        LogUitls.print(TAG, "callPhone" + phone);
        if (PermissionHelper.hasPermissions(android.Manifest.permission.CALL_PHONE)) {
            try {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);

                HistoryItem historyItem = new HistoryItem();
                historyItem.isCallByHand = isCallByHand;
                historyItem.targetPhone = phone;
                historyItem.date = DateUtils.getSimpleDate().format(new Date());
                if (binder.adapter != null)
                    binder.adapter.add(0, historyItem);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.show("没有权限，拨打电话失败`1");
            }
        } else {
            ToastUtils.show("没有权限，拨打电话失败`1");
        }
    }

    private void request() {
        if (localPhoneNum == null) {
            return;
        }
        LogUitls.print(TAG, "request" + localPhoneNum);
        NetBuilder.create(HdApp.getContext()).add2Url("mobile", localPhoneNum)
                .start("", new NetCallbackImpl() {
                    @Override
                    public void onSuccess(NetEntity entity) throws Exception {
                        if (entity.data != null) {
                            aotoMobile(entity.getJSONObject().getString("target_mobile"));
                        }
                        if (binder.logAdapter != null)
                            binder.logAdapter.add(0, String.format("%s 请求成功！code=%d data=%s isLocked=" + isLocked, DateUtils.getSimpleDate().format(new Date()), entity.getCode(), entity.getData()));

                    }

                    @Override
                    public void onError(NetEntity entity) throws Exception {
                        super.onError(entity);
                        if (entity.data != null) {
                            aotoMobile(entity.getJSONObject().getString("target_mobile"));
                        }
                        if (binder.logAdapter != null)
                            binder.logAdapter.add(0, String.format("%s 请求失败！code=%d data=%s", DateUtils.getSimpleDate().format(new Date()), entity.getCode(), entity.getData()));
                    }
                });
    }

    /**
     * 创建Binder对象，返回给客户端即Activity使用，提供数据交换的接口
     */
    public class LocalBinder extends Binder {
        // 声明一个方法，getService。（提供给客户端调用）
        public LoopService getService() {
            // 返回当前对象LocalService,这样我们就可在客户端端调用Service的公共方法了
            return LoopService.this;
        }

        SuperAdapter<String> logAdapter;

        TextView tvSocketInfo;


        public void setTvSocketInfo(TextView tvSocketInfo) {
            this.tvSocketInfo = tvSocketInfo;
        }

        public void setLogAdapter(SuperAdapter<String> logAdapter) {
            this.logAdapter = logAdapter;
        }

        HistoryAdapter adapter;

        public void setAdapter(HistoryAdapter adapter) {
            this.adapter = adapter;
        }

        public void removeAll() {
            adapter = null;
            logAdapter = null;
            tvSocketInfo = null;
        }
    }

    private LocalBinder binder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUitls.print(TAG, "onBind" + binder);

        return binder;
    }


    private void fun() {
        CharSequence text = "启动了";
        CharSequence title = getText(R.string.app_name);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

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
//        if (socket != null)
//            socket.stopSocket();
        if (handerLoopHelper != null)
            handerLoopHelper.stopLoop();
        return super.onUnbind(intent);
    }

    private static final String TAG = "LoopService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUitls.print(TAG, "onStartCommand");

        String phone = intent.getStringExtra("phone");
        localPhoneNum = phone;
        LogUitls.print(TAG, "onStartCommand" + localPhoneNum);
        createLooper();
        ToastUtils.show("开始looper");
        fun();

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loopHelper != null) {
            loopHelper.onDestory();
        }
        stopForeground(true);
    }
}
