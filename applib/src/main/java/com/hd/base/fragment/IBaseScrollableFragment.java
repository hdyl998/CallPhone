package com.hd.base.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hd.R;
import com.hd.base.IBaseFragment;
import com.hd.base.adapterbase.MyFragmentPagerAdapter;
import com.hd.base.adapterbase.MyPagerAdapter;
import com.hd.utils.device.AppDeviceInfo;
import com.hd.view.PagerSlidingTabStrip;
import com.hd.view.TitleBar;
import com.hd.view.scrollablelayoutlib.ScrollableHelper;
import com.hd.view.scrollablelayoutlib.ScrollableLayout;

/**
 * Created by liugd on 2017/3/29.
 */

public abstract class IBaseScrollableFragment extends IBaseFragment implements ScrollableLayout.IOnTopChangedListener {

    protected ScrollableHelper.ScrollableContainer containers[];
    protected IBaseFragment fragments[];
    protected ScrollableLayout slLayout;
    protected ViewPager viewPager;
    protected TitleBar titleBar;
    protected PagerSlidingTabStrip pstTabStrip;
    protected ViewGroup topFrame;//顶部的容器
    protected int pageType = 0;


    /****
     * 这里基本上不存在变化所以写成final
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.fragment_scrollable_base;
    }

    /**
     * 标题是否透明的开关
     *
     * @return
     */
    protected boolean isTitleAlpha() {
        return true;
    }


    @Override
    public final void initView() {
        //findview
        titleBar = findViewByID(R.id.titleBar);
        pstTabStrip = findViewByID(R.id.pts_tabstrip);
        viewPager = findViewByID(R.id.vp_team);
        topFrame = findViewByID(R.id.rl_head_content);
        slLayout = findViewByID(R.id.scrollableLayout);
        //添加头部
        topFrame.addView(LayoutInflater.from(mContext).inflate(setScrollableHeadID(), topFrame, false));//添加数据
        if (isTitleAlpha()) {
            titleBar.getTvTitle().setAlpha(0);
        }
        if (titleBar != null)
            titleBar.initFragmentSetting(this);
        titleBar.getTvTitle().setText(null);
        fragments = createFragments();
        if (fragments != null) {
            containers = getContainers(fragments);
            for (Fragment fragment : fragments) {
                //初始化时去掉titleBar
                if (fragment instanceof IBaseTitleBarFragment) {
                    ((IBaseTitleBarFragment) fragment).setInitHasTitleBar(false);
                }
                //默认采用懒加载的处理，可关开关
                if (isChildFragmetLacyInit()) {
                    ((IBaseFragment) fragment).setLacyInit(true);
                }
            }
            MyFragmentPagerAdapter mFrameAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragments);
            mFrameAdapter.setTitles(setTitleString().split("\\|"));
            viewPager.setAdapter(mFrameAdapter);
        } else {
            View[] views = createViews();//创建底部容器
            if (views == null) {
                throw new RuntimeException("createFragments 和 createViews两个方法你必须实现一个");
            }
            containers = getContainers(views);
            MyPagerAdapter adapter = new MyPagerAdapter(views);
            adapter.setTitles(setTitleString().split("\\|"));
            viewPager.setAdapter(adapter);
        }
        viewPager.setOffscreenPageLimit(containers.length);
        //无纯色状态栏
        if (isStatusBarColor() == false) {
            //修正样式
//            if (titleBar.getThemeStyle() == TitleBar.STYLE_DARK) {
//                StatusBarColorUtils.setStatusBarColor(mContext, R.color._00000000);
//            }
            //取一半的的title_ly和状态栏的高度，API 21以下状态栏高度为0，之上为24dp
//            int topPadding = (int) (getResources().getDimension(R.dimen.activity_title_ly)) / 2 + (int) getResources().getDimension(R.dimen.status_height);

            int topPadding = AppDeviceInfo.mTitleBarHeight / 2 + AppDeviceInfo.mStatusBarHeight;
            topFrame.setPadding(0, topPadding, 0, 0);
            slLayout.setOffsetHeight(AppDeviceInfo.mTitleBarHeight + AppDeviceInfo.mStatusBarHeight);
        } else {//有纯色状态栏
            int topPadding = AppDeviceInfo.mTitleBarHeight;
            topFrame.setPadding(0, topPadding / 2, 0, 0);
            slLayout.setOffsetHeight(topPadding);
        }
        slLayout.getHelper().setCurrentScrollableContainer(containers[0]);
        pstTabStrip.setViewPager(viewPager);
        pstTabStrip.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                slLayout.getHelper().setCurrentScrollableContainer(containers[position]);
                if (fragments != null)
                    fragments[position].startGetDataSafely();
                pageType = position;
                onPagerSelected(position);//另外要做的事
            }
        });
        initScrollAbleView();
        slLayout.setOnTopChangedListener(this);
    }

    @Override
    public void onTopChanged(float var, int currntY, int maxY) {
        if (isTitleAlpha()) {
            float aa = 1f / 3;
            if (var >= aa) {
                titleBar.getTvTitle().setAlpha((var - aa) * 3 / 2);
            } else {
                titleBar.getTvTitle().setAlpha(0);
            }
            aa = 2f / 3;
            if (var > aa) {
                topFrame.getChildAt(0).setAlpha(0);
            } else {
                topFrame.getChildAt(0).setAlpha(1 - var * 3 / 2);
            }
        }
    }

    /***
     * 子碎片是否采用懒加载
     *
     * @return
     */
    public boolean isChildFragmetLacyInit() {
        return true;
    }

    private ScrollableHelper.ScrollableContainer[] getContainers(Object[] objects) {
        ScrollableHelper.ScrollableContainer[] containers = new ScrollableHelper.ScrollableContainer[objects.length];
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof ScrollableHelper.ScrollableContainer) {
                containers[i] = (ScrollableHelper.ScrollableContainer) objects[i];
            } else {
                throw new RuntimeException("子碎片或View必须实现ScrollableHelper.ScrollableContainer接口");
            }
        }
        return containers;
    }


    /***
     * 设置顶部的浮动的布局
     *
     * @return
     */
    protected abstract int setScrollableHeadID();


    /***
     * 设置框架里可滑动的容器
     *
     * @return
     */
    protected View[] createViews() {
        return null;
    }

    /***
     * 设置框架里可滑动的碎片
     *
     * @return
     */
    protected IBaseFragment[] createFragments() {
        return null;
    }

    /***
     * 滑动的指示器的
     *
     * @return
     */
    protected abstract String setTitleString();


    protected abstract void initScrollAbleView();

    /**
     * 当页码选择时
     *
     * @param position
     */
    protected abstract void onPagerSelected(int position);


    public TitleBar getTitleBar() {
        return titleBar;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }


    public ViewGroup getTopFrame() {
        return topFrame;
    }


    public PagerSlidingTabStrip getPstTabStrip() {
        return pstTabStrip;
    }


    public IBaseFragment getCurFragment() {
        return fragments[pageType];
    }


    public int getPageType() {
        return pageType;
    }

}
