package com.hd.base.fragment;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.hd.view.listview.MyTipsListView;

import java.util.List;

/**
 * Created by liugd on 2017/5/15.
 */

public abstract class IBaseTipsListViewFragment<T> extends IBaseTipsViewFragment<T, MyTipsListView> {
    @Override
    protected View setBodyView(Context mContext) {
        tipsView = new MyTipsListView(mContext);
        tipsView.setNoDivider();
        return tipsView;
    }


    @Override
    public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position -= getTipsView().getContentView().getHeaderViewsCount();//去掉头部的影响
        if (position < 0) {
            return;
        }
        List<T> list = listData;
        if (list != null && list.size() > 0 && position < list.size()) {
            onItemClickSafely(list.get(position), position);
        }
    }

    //点击回调，这里作了去掉头部容错处理
    protected void onItemClickSafely(T item, int position) {
    }

    public ListView getContentView() {
        return tipsView.getContentView();
    }
}
