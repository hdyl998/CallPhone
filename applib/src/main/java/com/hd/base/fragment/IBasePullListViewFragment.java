package com.hd.base.fragment;

import android.content.Context;
import android.view.View;


import com.hd.utils.log.impl.LogUitls;
import com.hd.view.listview.PulltoRefreshAAMListView;
import com.hd.view.scrollablelayoutlib.ScrollableHelper;

/**
 * 有一个titleBar和一个listView的界面
 * Created by liugd on 2017/3/30.
 */

public abstract class IBasePullListViewFragment<T> extends IBasePullListViewComFragment<T, PulltoRefreshAAMListView> implements ScrollableHelper.ScrollableContainer {
    /***
     * 这里可以定制重写定义不同样式的下拉组件可以从layout里面解析，也可以NEW生成
     *
     * @param mContext
     * @return
     */
    @Override
    protected View setBodyView(Context mContext) {
        listView = new PulltoRefreshAAMListView(mContext);
        listView.setNoDivider();
        LogUitls.print("ttt", "create");
//        listView.addHeaderLine(10);
        return listView;
    }


    /***
     * 获得最后一个条目（用于最下拉刷新队尾的条目数）
     *
     * @return
     */
    public T getTailItem() {
        if (getListData().isEmpty() || listView.isFirstPager()) {
            return null;
        } else {
            return getListData().get(getListData().size() - 1);
        }
    }

    @Override
    public View getScrollableView() {
        if (isInited()) {
            return getListView().getRefreshableView();
        }
        return null;
    }
}
