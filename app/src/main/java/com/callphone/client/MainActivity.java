package com.callphone.client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hd.base.IBaseActivity;
import com.hd.base.adapterbase.BaseViewHolder;
import com.hd.base.adapterbase.SuperAdapter;
import com.hd.base.maininterface.IComCallBacks;
import com.hd.cache.SpUtils;
import com.hd.permission.PermissionCallback;
import com.hd.permission.PermissionHelper;
import com.hd.utils.DateUtils;
import com.hd.utils.GoUtils;
import com.hd.utils.bufferknife.MyBindView;
import com.hd.utils.bufferknife.MyBufferKnifeUtils;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.toast.ToastUtils;
import com.hd.view.roundrect.ShapeCornerSelectView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends IBaseActivity {

    EditText editText;


    TextView tvPermissionInfo;

    Button btnRequestPermission;


    ListView listView;


    HistoryAdapter adapter;

    SuperAdapter<String> logAdapter;


    @MyBindView(R.id.editSelf)
    EditText editSelf;

    @MyBindView(R.id.selectView)
    ShapeCornerSelectView selectView;

    @MyBindView(R.id.tvSocketInfo)
    TextView tvSocketInfo;
    private ScreenListener l;


    @Override
    public int[] setClickIDs() {
        return new int[]{R.id.btnSave, R.id.btnSet};
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                checkStart();
                break;
            case R.id.btnSet:
                GoUtils.goAppDetailsSetting(mContext);
                break;
        }
    }


    private void checkStart() {
        localPhoneNum = editSelf.getText().toString().trim();
        if (localPhoneNum.length() != 11) {
            ToastUtils.show("电话号码格式不正确");

        } else {
            SpUtils.putString("cache", "phone", localPhoneNum);
            if (hasPermission) {
                createService();

            } else {
                ToastUtils.show("没有权限，请先申请权限");
            }
        }
    }

    LoopService mService;

    ServiceConnection conn = null;

    public ServiceConnection getConn() {
        if (conn == null) {
            conn = new ServiceConnection() {
                /**
                 * 与服务器端交互的接口方法 绑定服务的时候被回调，在这个方法获取绑定Service传递过来的IBinder对象， * 通过这个IBinder对象，实现宿主和Service的交互。
                 */
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    LogUitls.print(TAG, "绑定成功调用：onServiceConnected");
// 获取Binder
                    LoopService.LocalBinder binder = (LoopService.LocalBinder) service;

                    binder.setAdapter(adapter);
                    binder.setLogAdapter(logAdapter);
                    binder.setTvSocketInfo(tvSocketInfo);

// 获取服务对象
                    mService = binder.getService();
                }

                /**
                 * 当取消绑定的时候被回调。但正常情况下是不被调用的，它的调用时机是当Service服务被意外销毁时， * 例如内存的资源不足时这个方法才被自动调用。
                 */
                @Override
                public void onServiceDisconnected(ComponentName name) {
                    mService = null;
                }
            };

        }

        return conn;
    }


    private void createService() {
        Intent intent = new Intent(this, LoopService.class);
        intent.putExtra("phone", localPhoneNum);
        bindService(intent, getConn(), Service.BIND_AUTO_CREATE);
//        startService(intent);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWakeLock != null) {
            mWakeLock.release();
        }
        l.unregisterListener();

        unbindService(getConn());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    public void WifiNeverDormancy(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();

        int value = Settings.System.getInt(resolver, Settings.System.WIFI_SLEEP_POLICY, Settings.System.WIFI_SLEEP_POLICY_DEFAULT);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);

//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putInt(mContext.getString(android.R.string.wifi_sleep_policy_default), value);
//        editor.commit();

        if (Settings.System.WIFI_SLEEP_POLICY_NEVER != value) {
            Settings.System.putInt(resolver, Settings.System.WIFI_SLEEP_POLICY, Settings.System.WIFI_SLEEP_POLICY_NEVER);

        }
        ToastUtils.show("wifi value:" + value);
    }

    @Override
    protected void initView() {

        WifiNeverDormancy(this);
        MyBufferKnifeUtils.inject(this);
        tvPermissionInfo = findViewById(R.id.tvPermissionInfo);
        editText = findViewById(R.id.editText);


        localPhoneNum = SpUtils.getString("cache", "phone");

        editSelf.setText(localPhoneNum);

        btnRequestPermission = findViewByID(R.id.button2);


        listView = findViewByID(R.id.listView);
        adapter = new HistoryAdapter(mContext);

        adapter.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryItem item = (HistoryItem) v.getTag();
                callPhone(item.targetPhone, true);
            }
        });


        logAdapter = new SuperAdapter<String>(mContext, new ArrayList<>(), android.R.layout.simple_list_item_1) {
            @Override
            protected void onBind(BaseViewHolder holder, String item, int position) {
                holder.setText(android.R.id.text1, item);
            }
        };

        selectView.initButtons(new String[]{"拨打记录", "运行日志"}, new IComCallBacks<Integer>() {
            @Override
            public void call(Integer obj) {
                if (obj == 0) {
                    listView.setAdapter(adapter);
                } else {
                    listView.setAdapter(logAdapter);
                }

            }
        });

        listView.setAdapter(adapter);
        onClickPermission(null);

        if (!TextUtils.isEmpty(localPhoneNum)) {
            checkStart();
        }

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (powerManager != null) {
            //PARTIAL_WAKE_LOCK :保持CPU 运转，屏幕和键盘灯有可能是关闭的。
            // SCREEN_DIM_WAKE_LOCK ：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
            // SCREEN_BRIGHT_WAKE_LOCK ：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
            //FULL_WAKE_LOCK ：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
            ToastUtils.show("请求屏幕常亮");
            mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "WakeLock");
        }


        l = new ScreenListener(this);
        l.begin(new ScreenListener.ScreenStateListener() {

            @Override
            public void onUserPresent() {
                Log.e("onUserPresent", "onUserPresent");

            }

            @Override
            public void onScreenOn() {
                Log.e("onScreenOn", "onScreenOn");
            }

            @Override
            public void onScreenOff() {

                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mWakeLock != null) {
                            mWakeLock.acquire();
                        }
                        startActivity(new Intent(mContext,AlarmHandlerActivity.class));
                        Log.e("onScreenOff", "onScreenOff");
                    }
                },500);


            }
        });
    }

    PowerManager.WakeLock mWakeLock;

    @Override
    protected void onResume() {
        super.onResume();
        if (mWakeLock != null) {
            mWakeLock.acquire();
        }
    }


    public void onClickCall(View view) {
//        String phoneNum = editText.getText().toString().trim().replace(" ", "");
//        if (phoneNum.length() == 0) {
//            ToastUtils.show("电话号码不能为空");
//            return;
//        }
//        callPhone(phoneNum, true);
    }


    @SuppressLint("MissingPermission")
    private void callPhone(String phone, boolean isCallByHand) {

        if (PermissionHelper.hasPermissions(Manifest.permission.CALL_PHONE)) {
            try {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);

                HistoryItem historyItem = new HistoryItem();
                historyItem.isCallByHand = isCallByHand;
                historyItem.targetPhone = phone;
                historyItem.date = DateUtils.getSimpleDate().format(new Date());
                adapter.add(0, historyItem);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.show("没有权限，拨打电话失败`1");
            }
        } else {
            ToastUtils.show("没有权限，拨打电话失败`1");
        }
    }

    boolean hasPermission = false;

    public void onClickPermission(View view) {
        PermissionHelper.create(mContext).setPermissions(Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE)
                .request(new PermissionCallback() {
                    @Override
                    public void onPermissionGranted() {
                        tvPermissionInfo.setText("权限状态：申请成功");
                        ToastUtils.show(tvPermissionInfo.getText().toString());
                        btnRequestPermission.setEnabled(false);
                        hasPermission = true;
                    }

                    @Override
                    public void onPermissionDenied(List<String> permissions) {
                        super.onPermissionDenied(permissions);
                        tvPermissionInfo.setText("权限状态：申请失败！请授权");
                        ToastUtils.show(tvPermissionInfo.getText().toString());
                        btnRequestPermission.setEnabled(true);
                        hasPermission = false;
                    }
                });
    }

    private String localPhoneNum;

    private static final String TAG = "MainActivity";

    private void getPhoneInfo() {
        PhoneInfo siminfo = new PhoneInfo(this);
        localPhoneNum = siminfo.getNativePhoneNumber();

        LogUitls.print(TAG, "getProvidersName:" + siminfo.getProvidersName());
        LogUitls.print(TAG, "getNativePhoneNumber:" + localPhoneNum);
        LogUitls.print(TAG, "------------------------");
        LogUitls.print(TAG, "getPhoneInfo:" + siminfo.getPhoneInfo());
        if (localPhoneNum.startsWith("+861")) {
            localPhoneNum = localPhoneNum.substring(4);
        }
    }

    static String ISDOUBLE;
    static String SIMCARD;
    static String SIMCARD_1;
    static String SIMCARD_2;
    static boolean isDouble;


    public void onclickType(View view) {
        LoopService.isRequest = !LoopService.isRequest;
        TextView textView = (TextView) view;
        textView.setText(LoopService.isRequest ? "网页请求" : "socket请求");
    }


//    private void getNumber()
//    {
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
//        String phoneNumber1 = tm.getLine1Number();
//
//        // String phoneNumber2 = tm.getGroupIdLevel1();
//
//        initIsDoubleTelephone(this);
//        if (isDouble)
//        {
//            // tv.setText("这是双卡手机！");
//            tvInfoPhone.setText("本机号码是：" + "   " + phoneNumber1 + "   " + "这是双卡手机！");
//        } else
//        {
//            // tv.setText("这是单卡手机");
//            tvInfoPhone.setText("本机号码是：" + "   " + phoneNumber1 + "   " + "这是单卡手机");
//        }
//
//    }
//
//    public void initIsDoubleTelephone(Context context)
//    {
//        isDouble = true;
//        Method method = null;
//        Object result_0 = null;
//        Object result_1 = null;
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        try
//        {
//            // 只要在反射getSimStateGemini 这个函数时报了错就是单卡手机（这是我自己的经验，不一定全正确）
//            method = TelephonyManager.class.getMethod("getSimStateGemini", new Class[]
//                    { int.class });
//            // 获取SIM卡1
//            result_0 = method.invoke(tm, new Object[]
//                    { new Integer(0) });
//            // 获取SIM卡2
//            result_1 = method.invoke(tm, new Object[]
//                    { new Integer(1) });
//        } catch (SecurityException e)
//        {
//            isDouble = false;
//            e.printStackTrace();
//            // System.out.println("1_ISSINGLETELEPHONE:"+e.toString());
//        } catch (NoSuchMethodException e)
//        {
//            isDouble = false;
//            e.printStackTrace();
//            // System.out.println("2_ISSINGLETELEPHONE:"+e.toString());
//        } catch (IllegalArgumentException e)
//        {
//            isDouble = false;
//            e.printStackTrace();
//        } catch (IllegalAccessException e)
//        {
//            isDouble = false;
//            e.printStackTrace();
//        } catch (InvocationTargetException e)
//        {
//            isDouble = false;
//            e.printStackTrace();
//        } catch (Exception e)
//        {
//            isDouble = false;
//            e.printStackTrace();
//            // System.out.println("3_ISSINGLETELEPHONE:"+e.toString());
//        }
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = sp.edit();
//        if (isDouble)
//        {
//            // 保存为双卡手机
//            editor.putBoolean(ISDOUBLE, true);
//            // 保存双卡是否可用
//            // 如下判断哪个卡可用.双卡都可以用
//            if (result_0.toString().equals("5") && result_1.toString().equals("5"))
//            {
//                if (!sp.getString(SIMCARD, "2").equals("0") && !sp.getString(SIMCARD, "2").equals("1"))
//                {
//                    editor.putString(SIMCARD, "0");
//                }
//                editor.putBoolean(SIMCARD_1, true);
//                editor.putBoolean(SIMCARD_2, true);
//
//                tvInfoPhone2.setText("双卡可用");
//
//            } else if (!result_0.toString().equals("5") && result_1.toString().equals("5"))
//            {// 卡二可用
//                if (!sp.getString(SIMCARD, "2").equals("0") && !sp.getString(SIMCARD, "2").equals("1"))
//                {
//                    editor.putString(SIMCARD, "1");
//                }
//                editor.putBoolean(SIMCARD_1, false);
//                editor.putBoolean(SIMCARD_2, true);
//
//                tvInfoPhone2.setText("卡二可用");
//
//            } else if (result_0.toString().equals("5") && !result_1.toString().equals("5"))
//            {// 卡一可用
//                if (!sp.getString(SIMCARD, "2").equals("0") && !sp.getString(SIMCARD, "2").equals("1"))
//                {
//                    editor.putString(SIMCARD, "0");
//                }
//                editor.putBoolean(SIMCARD_1, true);
//                editor.putBoolean(SIMCARD_2, false);
//
//                tvInfoPhone2.setText("卡一可用");
//
//            } else
//            {// 两个卡都不可用(飞行模式会出现这种种情况)
//                editor.putBoolean(SIMCARD_1, false);
//                editor.putBoolean(SIMCARD_2, false);
//
//                tvInfoPhone2.setText("飞行模式");
//            }
//        } else
//        {
//            // 保存为单卡手机
//            editor.putString(SIMCARD, "0");
//            editor.putBoolean(ISDOUBLE, false);
//        }
//        editor.commit();
//    }
}
