package com.hd.base.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.hd.R;
import com.hd.base.IBaseFragment;
import com.hd.base.adapterbase.MyFragmentPagerAdapter;
import com.hd.base.adapterbase.MyPagerAdapter;
import com.hd.base.inter.IViewPagerIndicator;
import com.hd.base.launch.AppLauncher;
import com.hd.utils.log.impl.LogUitls;
import com.hd.view.NoScrollViewPager;
import com.hd.view.PagerSlidingTabStrip;
import com.hd.view.TitleBar;
import com.hd.view.scrollablelayoutlib.ScrollableHelper;

import java.util.List;

/**
 * 含有指示器viewPager+PagerSlidingTabStrip的基类
 * Created by liugd on 2017/3/28.
 */

public abstract class IBaseViewPagerFragment extends IBaseFragment implements ScrollableHelper.ScrollableContainer {

    protected ViewPager viewPager;
    protected IViewPagerIndicator indicator;

    protected TitleBar titleBar;

    protected int mType = 0;

    protected String[] arrayTitles;

    protected Fragment fragments[];


    protected int initPagerIndex;//初始化的pager


    public void setInitPagerIndex(int initPagerIndex) {
        this.initPagerIndex = initPagerIndex;
        if (isInited()) {
            viewPager.setCurrentItem(initPagerIndex);
        }
    }

    /***
     * 可复写布局，但是布局里的ID 要对应得上，否则流程会出错
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.layout_base_pst_viewpager;
    }

    private static final String TAG = "IBaseViewPagerFragment";


    @Override
    public final void initView() {
        titleBar = findViewByID(R.id.titleBar);
        indicator = findViewByID(R.id.indicator);
        viewPager = findViewByID(R.id.viewPager);
        if (titleBar != null)
            titleBar.initFragmentSetting(this);
        boolean isNull = indicator == null;
        LogUitls.print(TAG, "indicator为" + (isNull ? "null" : indicator.getClass()));
        // 没有滑动动画
        if (!isViewPagerTransAnimation()) {
            if (viewPager instanceof NoScrollViewPager) {
                ((NoScrollViewPager) viewPager).setScrollAbleFalse();
            }
        }
        //采用view填充viewPager
        View[] listViews = getViewLists();
        if (arrayTitles == null) {
            arrayTitles = getTitleString().split("\\|");
        }
        int pagerIndex;
        if (listViews != null) {
            pagerIndex = initPagerIndex(listViews.length);
            viewPager.setOffscreenPageLimit(listViews.length);
            MyPagerAdapter adapter = new MyPagerAdapter(listViews);
            adapter.setTitles(arrayTitles);
            viewPager.setAdapter(adapter);
            if (indicator != null)
                indicator.setViewPager(viewPager);
        } else {//采用Fragment填充viewPager
            Fragment[] listFragments = getFragmentLists();
            fragments = listFragments;
            if (listFragments != null) {
                pagerIndex = initPagerIndex(listFragments.length);
                for (Fragment fragment : listFragments) {
                    //初始化时去掉titleBar
                    if (fragment instanceof IBaseTitleBarFragment) {
                        ((IBaseTitleBarFragment) fragment).setInitHasTitleBar(false);
                    }
                    //默认采用懒加载的处理，可关开关
                    if (fragment instanceof IBaseFragment && isChildFragmetLacyInit()) {
                        ((IBaseFragment) fragment).setLacyInit(true);
                    }
                }
                //解决懒加载初始化非第一页时，会自动初始化第一页的问题，设置开关为关
                if (pagerIndex != 0 && listFragments[0] instanceof IBaseFragment) {
                    ((IBaseFragment) listFragments[0]).setVisibleEnable(false);
                }
                viewPager.setOffscreenPageLimit(listFragments.length);
                MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), listFragments);
                adapter.setTitles(arrayTitles);
                viewPager.setAdapter(adapter);

                if (indicator != null)
                    indicator.setViewPager(viewPager);
                //解决懒加载初始化非第一页时，会自动初始化第一页的问题，设置开关为开，切到第一页就初始化
                if (pagerIndex != 0 && listFragments[0] instanceof IBaseFragment) {
                    ((IBaseFragment) listFragments[0]).setVisibleEnable(true);
                }
            } else {
                throw new RuntimeException("你必须实现setViewLists 和setFragmentLists两个方法中的一个");
            }
        }
        if (pagerIndex != 0) {
            viewPager.setCurrentItem(pagerIndex, false);
        }
        initViewPagerView();//这样处理顺序是不让viewPager.setCurrentItem 对其产生影响
        viewPager.addOnPageChangeListener(changeListener);

        //重新初始化其值
        mType = viewPager.getCurrentItem();
    }


    private ViewPager.OnPageChangeListener changeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mType = position;
            onPagerSelected(position);//另外要做的事
        }
    };


    /***
     * 是否有动画
     * @return
     */
    protected boolean isViewPagerTransAnimation() {
        return true;
    }


    /***
     * 起始时，这里跳转到指定页码
     */
    private int initPagerIndex(int maxType) {
        //初始化页
        if (initPagerIndex != 0) {
            return initPagerIndex;
        }
        //这里从外部跳转用
        mType = AppLauncher.getPagerIndex(mContext);
        if (mType < 0 || mType >= maxType) {
            mType = 0;
        }
        return mType;
    }


    protected abstract void initViewPagerView();

    protected void onPagerSelected(int mType) {
    }

    public void setArrayTitles(String[] arrayTitles) {
        this.arrayTitles = arrayTitles;
    }

    public void setArrayTitles(List<String> arrayTitles) {
        this.arrayTitles = new String[arrayTitles.size()];
        arrayTitles.toArray(this.arrayTitles);
    }

    public String[] getArrayTitles() {
        return arrayTitles;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }


    public PagerSlidingTabStrip getPstTabStip() {
        if (indicator instanceof PagerSlidingTabStrip)
            return (PagerSlidingTabStrip) indicator;
        return null;
    }

    public IViewPagerIndicator getIndicator() {
        return indicator;
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }


    /***
     * 菜单标题格式为  A|B|C
     *
     * @return
     */
    protected abstract String getTitleString();

    /***
     * 设置碎片
     *
     * @return
     */
    protected Fragment[] getFragmentLists() {
        return null;
    }


    /***
     * 设置views
     *
     * @return
     */
    protected View[] getViewLists() {
        return null;
    }

    /***
     * 获取当前是第几个页码
     *
     * @return
     */
    public int getType() {
        return mType;
    }


    public <T extends IBaseFragment> T getCurrentFragment() {
        if (fragments == null) {
            return null;
        }
        return (T) fragments[mType];
    }


    public <T extends IBaseFragment> T getFragmentAt(int index) {
        if (fragments == null) {
            return null;
        }
        return (T) fragments[index];
    }


    /***
     * 子碎片是否采用懒加载
     *
     * @return
     */
    public boolean isChildFragmetLacyInit() {
        return true;
    }


    @Override
    public View getScrollableView() {
        if (isInited()) {
            return ((ScrollableHelper.ScrollableContainer) getCurrentFragment()).getScrollableView();
        }
        return null;
    }

    @Override
    public void onGetActivityResult(boolean isResultOK, int requestCode, Intent data) {
        //支持回调
        try {
            Fragment fragment = getCurrentFragment();
            if (fragment instanceof IBaseFragment) {
                IBaseFragment fragment1 = (IBaseFragment) fragment;
                if (fragment1.isInited()) {
                    fragment1.onGetActivityResult(isResultOK, requestCode, data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
