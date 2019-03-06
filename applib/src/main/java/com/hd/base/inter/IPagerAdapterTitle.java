package com.hd.base.inter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Note：None
 * Created by lgd on 2019/1/10 11:01
 * E-Mail Address：986850427@qq.com
 */
public interface IPagerAdapterTitle {


    void setTitles(String[] titles);

    CharSequence getPageTitle(int position);


    String[] getTitles();
}
