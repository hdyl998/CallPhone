package com.hd.base.adapterbase;

/**
 * Created by liugd on 2017/3/28.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hd.base.inter.IPagerAdapterTitle;

import java.util.List;

/**
 * Created by cpoopc on 2015-02-10.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter implements IPagerAdapterTitle {

    private Fragment[] fragments;


    public MyFragmentPagerAdapter(FragmentManager fm, List<? extends Fragment> fragmentList) {
        super(fm);
        this.fragments = new Fragment[fragmentList.size()];
        fragmentList.toArray(this.fragments);
    }

    public MyFragmentPagerAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.fragments=fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public void setTitles(String[] titles) {
        this.titles=titles;
    }
    private String[] titles;
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public String[] getTitles() {
        return titles;
    }

}
