package com.hd.base.fragment;

import android.content.Context;
import android.view.View;


import com.hd.view.listview.RTPullPinnedHeaderAAMListView;

/**
 * 有一个titleBar和一个listView的界面，其中listView可以浮动头部
 * Created by liugd on 2017/3/30.
 */

public abstract class IBasePullPHListViewFragment<T> extends IBasePullListViewComFragment<T, RTPullPinnedHeaderAAMListView> {
    /***
     * 这里可以定制重写定义不同样式的下拉组件可以从layout里面解析，也可以NEW生成
     *
     * @param mContext
     * @return
     */
    @Override
    protected View setBodyView(Context mContext) {
        listView = new RTPullPinnedHeaderAAMListView(mContext);
        listView.setNoDivider();
        return listView;
    }
}
