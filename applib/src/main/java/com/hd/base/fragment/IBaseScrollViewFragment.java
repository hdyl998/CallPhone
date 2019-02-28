package com.hd.base.fragment;

import android.content.Context;
import android.view.View;

import com.alibaba.fastjson.JSON;

import com.hd.R;
import com.hd.net.NetCallback;
import com.hd.net.socket.NetEntity;
import com.hd.utils.reflect.ClassUtils;
import com.hd.view.scrollablelayoutlib.ScrollableHelper;
import com.hd.view.scrollview.MyTipsScrollView;

/**
 * Created by liugd on 2017/4/15.
 */

public abstract class IBaseScrollViewFragment<T> extends IBaseTitleBarFragment implements NetCallback, ScrollableHelper.ScrollableContainer {

    protected MyTipsScrollView scrollView;
    protected T dataItem;
    protected Class<T> tClass;//类型

    @Override
    protected final View setBodyView(Context mContext) {
        scrollView = new MyTipsScrollView(mContext);
        return scrollView;
    }

    @Override
    public final void initTitleBarView() {
        //寻找用布局创建的scrollview
        int layoutID = getLayoutId();
        if (layoutID != 0) {
            scrollView = findViewByID(R.id.scrollView);
        }
        scrollView.setErrorDataInterface(this);
        if (getBodyId() != 0) {
            scrollView.addScorllViewBody(getBodyId());
        } else {
            scrollView.addScorllViewBody(getScrollBody(mContext));
        }

        tClass = (Class<T>) ClassUtils.getClassType(getClass());
        initScrollView();
        startGetMyData();
    }

    protected abstract void initScrollView();

    /***
     * 设置内部布局
     *
     * @return
     */
    @Override
    protected int getBodyId() {
        return 0;
    }


    protected View getScrollBody(Context mContext) {
        return null;
    }

    /**
     * 设置网络请求
     */
    @Override
    public void startGetMyData() {
        setGetData(true);
        scrollView.showLoading();
        setAPIRequest();
    }

    public MyTipsScrollView getScrollView() {
        return scrollView;
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
        dataItem = JSON.parseObject(entity.DATA, tClass);
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
            return scrollView.getContentView();
        }
        return null;
    }
}
