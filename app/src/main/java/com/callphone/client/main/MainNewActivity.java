package com.callphone.client.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.callphone.client.AlarmHandlerActivity;
import com.callphone.client.R;
import com.callphone.client.ScreenListener;
import com.callphone.client.base.AppConstants;
import com.callphone.client.home.GrayService;
import com.callphone.client.home.HomeFragment;
import com.callphone.client.main.bean.EventItem;
import com.callphone.client.main.mine.LoginManager;
import com.callphone.client.mine.MineFragment;
import com.hd.base.IBaseActivity;
import com.hd.base.IBaseFragment;
import com.hd.base.adapterbase.MyFragmentPagerAdapter;
import com.hd.base.dialog.SimpleDialog;
import com.hd.base.maininterface.IComCallBacks;
import com.hd.permission.PermissionCallback;
import com.hd.permission.PermissionHelper;
import com.hd.utils.Utils;
import com.hd.utils.bufferknife.MyBindView;
import com.hd.utils.bufferknife.MyBufferKnifeUtils;
import com.hd.utils.limiteddo.CheckTimeDo;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.toast.ToastUtils;
import com.hd.view.NoScrollViewPager;
import com.hd.view.navigation.NavigationBarItem;
import com.hd.view.navigation.NavigationBarView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

//https://www.easyicon.net/538275-VoIP_icon.html
public class MainNewActivity extends IBaseActivity {


    @MyBindView(R.id.navigationBar)
    NavigationBarView navigationBarView;

    @MyBindView(R.id.viewPager)
    NoScrollViewPager viewPager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_page;
    }

    @Override
    protected void initView() {
        MyBufferKnifeUtils.inject(this);
        viewPager.setScrollAbleFalse();
        createNavigationBar();
        initOthers();
        checkPermission();
        startGrayService();
    }


    /***
     * 启动一个灰色服务，用于保活，不被杀死
     */
    private void startGrayService() {
        startService(new Intent(mContext, GrayService.class));
    }

    private void stopGrayService() {
        stopService(new Intent(mContext, GrayService.class));
    }


    private void createNavigationBar() {
        //点击无动画
        navigationBarView.setClickAnimation(false);
        List<NavigationBarItem> listsMenus = new ArrayList<>(2);
        listsMenus.add(new NavigationBarItem("首页", KEY_PAGE_HOME,
                R.mipmap.icon_home_home_sel, R.mipmap.icon_home_home, new HomeFragment()));
        listsMenus.add(new NavigationBarItem("我的", KEY_PAGE_MINE,
                R.mipmap.icon_home_mine_sel, R.mipmap.icon_home_mine, new MineFragment()));

        navigationBarView.setDataItems(listsMenus);

        List<IBaseFragment> iBaseFragments = new ArrayList<>(listsMenus.size());

        for (NavigationBarItem item : listsMenus) {
            iBaseFragments.add((IBaseFragment) item.getTag());
        }
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), iBaseFragments);
        viewPager.setOffscreenPageLimit(iBaseFragments.size());
        viewPager.setAdapter(adapter);


        navigationBarView.setClickCallBacks(obj -> {
            switch (obj.itemId) {
                case KEY_PAGE_HOME://比赛
                    break;
                case KEY_PAGE_MINE:
                    break;
            }
            selectedPage(obj.itemId);
        });
        goToIndexPage(getIntent());//启动时去页码指引
    }


    /**
     * 通过索引获取碎片
     *
     * @param index
     * @return
     */
    private IBaseFragment getFragmentByIndex(int index) {
        return navigationBarView.getTagByIndex(index);
    }

    /***
     * 选中页码
     *
     * @param pageId
     */
    private void selectedPage(int pageId) {
        int index = navigationBarView.getIndexById(pageId);
        LogUitls.print(TAG, "选中页码" + index);
        if (index != -1) {
            navigationBarView.setSelectIndex(index);
            viewPager.setCurrentItem(index, false);
            Fragment fragment = getFragmentByIndex(index);
            if (fragment instanceof OnPageCheckedListener) {
                ((OnPageCheckedListener) fragment).onPageChecked();
            }
        }
    }


    public final static int KEY_PAGE_HOME = 112;//比分

    public final static int KEY_PAGE_DEFAULT = KEY_PAGE_HOME;

    public final static int KEY_PAGE_MINE = 114;//我的

    public void goToPage(int page, Object parsms) {
        selectedPage(page);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            goToIndexPage(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUitls.print("newIntent", intent.getExtras() + "");
    }

    long lastPressedTime = 0l;// 上一次按返回键的时间

    /**
     * @param intent
     * @return void
     * @方法名: goToPushTaget
     * @功能描述: 更新推送类型不同跳转至不同的页面
     */
    private void goToIndexPage(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object object = bundle.getSerializable(ID_NEW_PARAM1);
            if (object instanceof Integer)
                goToPage((Integer) object, bundle.getSerializable(ID_NEW_PARAM2));//去指定TAB页面  push交给PushEntryActiviy代理了
        }
    }

    @Override
    public void onBackPressed() {
//        // 连按两次退出应用
//        if (System.currentTimeMillis() - lastPressedTime > 2000) {
//            lastPressedTime = System.currentTimeMillis();
//            ToastUtils.show("再按一次退出程序");
//        } else {
//            super.onBackPressed();
//        }
        SimpleDialog.create(mContext).setTvTitle("确认退出")
                .setTvContent("退出将无法呼叫号码")
                .setBtnLeft("退出")
                .setBtnRight("不退出")
                .setOnClickListener(new SimpleDialog.SimpleDialogClick() {
                    @Override
                    public void onLeftClick(SimpleDialog simpleDialog) {
                        mContext.finish();
                    }
                });

    }


    private ScreenListener screenListener;

    Handler handler = new Handler();


    PowerManager.WakeLock mWakeLock;

    @Override
    public void onResume() {
        super.onResume();
        if (mWakeLock != null) {
            mWakeLock.acquire();
        }
    }


    private static final String TAG = "MainNewActivity";

    private void initOthers() {
        PowerManager powerManager = (PowerManager) mContext.getSystemService(Activity.POWER_SERVICE);
        if (powerManager != null) {
            //PARTIAL_WAKE_LOCK :保持CPU 运转，屏幕和键盘灯有可能是关闭的。
            // SCREEN_DIM_WAKE_LOCK ：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
            // SCREEN_BRIGHT_WAKE_LOCK ：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
            //FULL_WAKE_LOCK ：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
            LogUitls.print(TAG, "请求屏幕常亮");
            mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "WakeLock");
        }
        screenListener = new ScreenListener(mContext);
        screenListener.begin(new ScreenListener.ScreenStateListener() {

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

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mWakeLock != null) {
                            mWakeLock.acquire();
                        }
                        startActivity(new Intent(mContext, AlarmHandlerActivity.class));
                        Log.e("onScreenOff", "onScreenOff");
                    }
                }, 300);
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
        if (mWakeLock != null) {
            mWakeLock.release();
        }
        screenListener.unregisterListener();
        stopGrayService();
    }


    @Override
    protected boolean isEventBus() {
        return true;
    }


    /***
     * 当用户登录时
     */
    @Subscribe
    public void onUserLogout(EventItem.LoginOutEvent item) {
        if (navigationBarView.isCurrentPage(KEY_PAGE_HOME)) {
            selectedPage(KEY_PAGE_HOME);
        }
        checkTimeDo.checkDo();
    }

    //防止多次调用登录
    CheckTimeDo checkTimeDo;

    {
        checkTimeDo = new CheckTimeDo(3000);
        checkTimeDo.setComCallBacks(new IComCallBacks() {
            @Override
            public void call(Object obj) {
                if (PermissionHelper.hasPermissions(AppConstants.permissionStart)) {
                    LoginManager.isLoginAndRedict(mContext);
                }
            }
        });
    }


    boolean hasPermission = false;

    private void checkPermission() {
        PermissionHelper.create(mContext).setPermissions(AppConstants.permissionStart)
                .request(new PermissionCallback() {
                    @Override
                    public void onPermissionGranted() {
                        hasPermission = true;
                    }

                    @Override
                    public void onPermissionDenied(List<String> permissions) {
                        super.onPermissionDenied(permissions);
                        hasPermission = false;
                    }
                });
    }

}
