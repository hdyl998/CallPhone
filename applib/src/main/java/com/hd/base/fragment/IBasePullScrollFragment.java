package com.hd.base.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.hd.R;
import com.hd.net.NetCallback;
import com.hd.net.socket.NetEntity;
import com.hd.utils.reflect.ClassUtils;
import com.hd.view.scrollablelayoutlib.ScrollableHelper;
import com.hd.view.scrollview.ObservableScrollView;
import com.hd.view.ui.PullToRefreshBase;
import com.hd.view.ui.PullToRefreshScrollView;

/**
 * Date:2017/11/24 19:28
 * Author:liugd
 * Modification:
 * ................
 * 佛祖保佑，永无BUG
 **/


public abstract class IBasePullScrollFragment<T> extends IBaseTitleBarFragment implements NetCallback, ScrollableHelper.ScrollableContainer {
    protected PullToRefreshScrollView scrollView;
    protected T dataItem;
    protected Class<T> tClass;//类型


    @Override
    protected final View setBodyView(Context mContext) {
        scrollView = new PullToRefreshScrollView(mContext);
        return scrollView;
    }

    @Override
    public final void initTitleBarView() {
        //寻找用布局创建的scrollview
        int layoutID = getLayoutId();
        if (layoutID != 0) {
            scrollView = findViewByID(R.id.scrollView);
        }
//        scrollView.setErrorDataInterface(this);
        if (getBodyId() != 0) {
            scrollView.addScorllViewBody(getBodyId());
        } else {
            View view = setScrollBody(mContext);
            if (view != null)
                scrollView.addScorllViewBody(view);
        }

        tClass = (Class<T>) ClassUtils.getClassType(getClass());
        initScrollView();
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ObservableScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ObservableScrollView> refreshView) {
                startGetMyData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ObservableScrollView> refreshView) {

            }
        });
        startGetMyData();
    }

    protected abstract void initScrollView();

    /***
     * 设置内部布局
     *
     * @return
     */
    @Override
    protected abstract int getBodyId();


    protected View setScrollBody(Context mContext) {
        return null;
    }

    /**
     * 设置网络请求
     */
    @Override
    public void startGetMyData() {
        if (!isEverGetData()) {
            scrollView.showLoading();
        }
        setGetData(true);
        setAPIRequest();
    }


    public ScrollView getScrollView() {
        return scrollView.getRefreshableView();
    }

    public T getDataItem() {
        return dataItem;
    }

    public void setDataItem(T dataItem) {
        this.dataItem = dataItem;
    }

    /***
     * 设置请求接口，这里与通用命名保持一致
     */
    protected abstract void setAPIRequest();

    //这里看是否需要重写，些方法，是否需要保留父类
    @Override
    public void onSuccess(NetEntity entity) throws Exception {
        isGetData = true;
        setEverGetData(true);
        dataItem = JSON.parseObject(entity.data, tClass);
        scrollView.showNormal();
    }

    @Override
    public void onError(NetEntity entity) throws Exception {
        scrollView.showError();
        setGetData(false);
    }

    @Override
    public View getScrollableView() {
        if (isInited()) {
            return getScrollView();
        }
        return null;
    }
}
