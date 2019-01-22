package com.hd.base.adapterbase;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.hd.utils.Utils;

import java.util.HashMap;
import java.util.List;

public abstract class CycleAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener { // PagerView的适配器
    private List<T> mDatas;

    HashMap<Integer, View> hashMap = new HashMap<>();
    public Context mContext;

    public CycleAdapter(Context mContext, List<T> mDatas) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    //是否正在被用户拖动
    boolean isScrolling = false;


    ViewPager viewPager;

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        //移除之前的回调
        Object adapter = viewPager.getAdapter();
        if (adapter instanceof ViewPager.OnPageChangeListener) {
            viewPager.removeOnPageChangeListener((ViewPager.OnPageChangeListener) adapter);
        }
        viewPager.setAdapter(this);
        viewPager.setCurrentItem(getCount() / 2);//前后都能无限滑动
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Indicates that the pager is currently being dragged by the user.
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            isScrolling = true;
        } else {
            isScrolling = false;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }


    /***
     * 下一页
     */
    public void nextPage() {
        if (isScrolling) {
            return;
        }
        if (Utils.isListEmpty(mDatas) || mDatas.size() <= 1) {
            return;
        }
        int nextPosi = (viewPager.getCurrentItem() + 1) % getCount();
        viewPager.setCurrentItem(nextPosi);
    }


    //放大因子
    private int mScaleFactory = 10000;

    @Override
    public int getCount() {
        return mDatas.size() > 1 ? mDatas.size() * mScaleFactory : 1;
    }

    public int getRealCount() {
        return mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;// 官方提示这样写
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = hashMap.get(position);
        container.removeView(view);// 删除页卡
    }

    public abstract View onCreateView(T t);

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = onCreateView(mDatas.get(getRealIndex(position)));
        hashMap.put(position, view);
        container.addView(view);
        return view;
    }

    private int getRealIndex(int position) {
        return position % getRealCount();
    }
}