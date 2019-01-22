package com.hd.base.adapterbase;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hd.base.inter.IPagerAdapterTitle;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter implements IPagerAdapterTitle { // PagerView的适配器
    private View[] mList;


    public MyPagerAdapter(View[] viewList) {
        this.mList = viewList;
    }
    public MyPagerAdapter(List<View> viewList) {
        this.mList = new View[viewList.size()];
        viewList.toArray(this.mList);
    }

    @Override
    public int getCount() {
        return mList.length;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;// 官方提示这样写
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mList[position]);// 删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mList[position]);// 添加页卡
        return mList[position];
    }

    private String[] titles;
    @Override
    public void setTitles(String[] titles) {
        this.titles=titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public String[] getTitles() {
        return titles;
    }
}