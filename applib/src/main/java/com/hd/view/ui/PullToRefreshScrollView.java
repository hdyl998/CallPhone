package com.hd.view.ui;//package com.caiyu.qqsd.view.ui;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.View;


import com.hd.view.EmptyLayout;
import com.hd.view.scrollview.ObservableScrollView;

/**
 * 封装了ScrollView的下拉刷新
 *
 * @author Li Hong
 * @since 2013-8-22
 */
public class PullToRefreshScrollView extends PullToRefreshBase<ObservableScrollView> implements EmptyLayout.IOnEmptyDataChangedListener {

    protected EmptyLayout emptyLayout;

    /**
     * 构造方法
     *
     * @param mContext mContext
     */
    public PullToRefreshScrollView(Context mContext) {
        this(mContext, null);
    }

    /**
     * 构造方法
     *
     * @param mContext mContext
     * @param attrs    attrs
     */
    public PullToRefreshScrollView(Context mContext, AttributeSet attrs) {
        this(mContext, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param mContext mContext
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullToRefreshScrollView(Context mContext, AttributeSet attrs, int defStyle) {
        super(mContext, attrs, defStyle);
        initEmptyLayout(mContext);
    }

    private void initEmptyLayout(Context mContext) {
        emptyLayout = new EmptyLayout(mContext, getRefreshableViewWrapper());
        emptyLayout.setErrorButtonClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doPullGetData();
            }
        });
    }


    @Override
    protected ObservableScrollView createRefreshableView(Context mContext, AttributeSet attrs) {
        ObservableScrollView scrollView = new ObservableScrollView(mContext);
        scrollView.setId(NO_ID);
        return scrollView;
    }


    @Override
    protected boolean isReadyForPullDown() {
        return mRefreshableView.getScrollY() == 0;
    }


    @Override
    protected boolean isReadyForPullUp() {
        View scrollViewChild = mRefreshableView.getChildAt(0);
        if (null != scrollViewChild) {
            return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
        }
        return false;
    }

    /**
     * 添加scrollView Body
     */
    public void addScorllViewBody(@LayoutRes int res) {
        mRefreshableView.addView(View.inflate(getContext(), res, null));
    }

    public void addScorllViewBody(View view) {
        mRefreshableView.addView(view);
    }

    @Override
    public void showError() {
        this.onPullDownRefreshComplete();
        if (!isContentViewVisiable()) {//仅当看不见时，才显示错误
            emptyLayout.showError();
        }
    }

    @Override
    public void showEmpty() {
        this.onPullDownRefreshComplete();
        emptyLayout.showEmpty();
    }


    @Override
    public void showLoading() {
        emptyLayout.showLoading();
        getRefreshableView().setVisibility(View.GONE);
    }

    @Override
    public void showNormal() {
        this.onPullDownRefreshComplete();
        if (!isContentViewVisiable()) {
            emptyLayout.setViewGone();//隐藏
            getRefreshableView().setVisibility(View.VISIBLE);
        }
    }


    private boolean isContentViewVisiable() {
        return getRefreshableView().getVisibility() == View.VISIBLE;
    }

    @Override
    public EmptyLayout getEmptyLayout() {
        return emptyLayout;
    }
}
