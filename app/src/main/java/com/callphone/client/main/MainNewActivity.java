package com.callphone.client.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.callphone.client.R;
import com.callphone.client.base.AppConstants;
import com.callphone.client.home.HomeFragment;
import com.callphone.client.mine.MineFragment;
import com.hd.base.IBaseActivity;
import com.hd.base.IBaseFragment;
import com.hd.base.adapterbase.MyFragmentPagerAdapter;
import com.hd.permission.PermissionCallback;
import com.hd.permission.PermissionHelper;
import com.hd.utils.bufferknife.MyBindView;
import com.hd.utils.bufferknife.MyBufferKnifeUtils;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.toast.ToastUtils;
import com.hd.view.NoScrollViewPager;
import com.hd.view.navigation.NavigationBarItem;
import com.hd.view.navigation.NavigationBarView;

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
//        Intent startMain = new Intent(Intent.ACTION_MAIN);
//        startMain.addCategory(Intent.CATEGORY_HOME);
//        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(startMain);
//        android.os.Process.killProcess(android.os.Process.myPid());
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //    @Override
//    public boolean isStatusBarColor() {
//        return false;
//    }

    private void createNavigationBar() {
        //点击无动画
        navigationBarView.setClickAnimation(false);
        List<NavigationBarItem> listsMenus = new ArrayList<>(2);
        listsMenus.add(new NavigationBarItem("首页", KEY_PAGE_SCORE,
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
                case KEY_PAGE_SCORE://比赛
                    break;
                case KEY_PAGE_ATT:
                    break;
                case KEY_PAGE_MINE:
                    break;
            }
            selectedPage(obj.itemId);
        });
        goToIndexPage(getIntent());//启动时去页码指引
    }

    private static final String TAG = "MainActivity";


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
        }
    }


    public final static int KEY_PAGE_SCORE = 112;//比分

    public final static int KEY_PAGE_DEFAULT = KEY_PAGE_SCORE;//比分

    public final static int KEY_PAGE_ATT = 113;//关注
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
        // 连按两次退出应用
        if (System.currentTimeMillis() - lastPressedTime > 2000) {
            lastPressedTime = System.currentTimeMillis();
            ToastUtils.show("再按一次退出程序");
        } else {
            super.onBackPressed();
        }
    }

    public boolean isMatchItem() {
        return navigationBarView.isCurrentPage(KEY_PAGE_SCORE);
    }


    public boolean isAttItem() {
        return navigationBarView.isCurrentPage(KEY_PAGE_ATT);
    }
}
