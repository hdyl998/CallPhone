package com.hd.view.listview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.Space;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;

import com.hd.R;
import com.hd.utils.ViewUtils;
import com.hd.view.EmptyLayout;
import com.hd.view.ui.FooterLoadingLayout;
import com.hd.view.ui.LoadingLayout;
import com.hd.view.ui.PullToRefreshBase;

import com.hd.view.ui.ILoadingLayout.State;
import com.hd.view.ui.PullToRefreshListView;


public class RTPullPinnedHeaderListView extends
        PullToRefreshBase<PinnedHeaderListView> implements OnScrollListener,
        EmptyLayout.IOnEmptyDataChangedListener {

    /**
     * ListView
     */
    private PinnedHeaderListView mListView;
    /**
     * 用于滑到底部自动加载的Footer
     */
    private LoadingLayout mLoadMoreFooterLayout;
    /**
     * 滚动的监听器
     */
    private OnScrollListener mScrollListener;

    protected EmptyLayout emptyLayout;

    /**
     * 是否含有emptylayout
     */
    protected boolean isHasEmptyLayout = true;

    /**
     * 构造方法
     *
     * @param context mContext
     */
    public RTPullPinnedHeaderListView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context mContext
     * @param attrs   attrs
     */
    public RTPullPinnedHeaderListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context  mContext
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public RTPullPinnedHeaderListView(Context context, AttributeSet attrs,
                                      int defStyle) {
        super(context, attrs, defStyle);
        setPullLoadEnabled(false);
        isHasEmptyLayout = true;
    }

    @Override
    protected PinnedHeaderListView createRefreshableView(Context context,
                                                         AttributeSet attrs) {
        PinnedHeaderListView listView = new PinnedHeaderListView(context, attrs);
        listView.setId(0);
        mListView = listView;
        listView.setOnScrollListener(this);
        return listView;
    }

    /**
     * 设置是否有更多数据的标志
     *
     * @param hasMoreData true表示还有更多的数据，false表示没有更多数据了
     */
    public void setHasMoreData(boolean hasMoreData) {
        if (!hasMoreData) {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.setState(State.NO_MORE_DATA);
            }

            LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
            if (null != footerLoadingLayout) {
                footerLoadingLayout.setState(State.NO_MORE_DATA);
            }
        }
    }

    /**
     * 设置滑动的监听器
     *
     * @param l 监听器
     */
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    protected void startLoading() {
        super.startLoading();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.REFRESHING);
        }
    }

    @Override
    public void onPullUpRefreshComplete() {
        super.onPullUpRefreshComplete();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(State.RESET);
        }
    }

    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        super.setScrollLoadEnabled(scrollLoadEnabled);

        if (scrollLoadEnabled) {
            // 设置Footer
            if (null == mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
            }

            if (null == mLoadMoreFooterLayout.getParent()) {
                mListView.addFooterView(mLoadMoreFooterLayout, null, false);
            }
            mLoadMoreFooterLayout.show(true);
        } else {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.show(false);
            }
        }
    }

    @Override
    public LoadingLayout getFooterLoadingLayout() {
        if (isScrollLoadEnabled()) {
            return mLoadMoreFooterLayout;
        }
        return super.getFooterLoadingLayout();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isScrollLoadEnabled() && hasMoreData()) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                if (isReadyForPullUp()) {
                    startLoading();
                }
            }
        }
        try {//2016-2-03(许宏奋)parameter must be a descendant of this view
            if (SCROLL_STATE_TOUCH_SCROLL == scrollState) {//
                View currentFocus = ((Activity) getContext()).getCurrentFocus();
                if (currentFocus != null) {
                    currentFocus.clearFocus();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != mScrollListener) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        //当滚动时配置重新配置头部
        mListView.configureHeaderView(firstVisibleItem);
        if (null != mScrollListener) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    /**
     * 表示是否还有更多数据
     *
     * @return true表示还有更多数据
     */
    private boolean hasMoreData() {
        if ((null != mLoadMoreFooterLayout)
                && (mLoadMoreFooterLayout.getState() == State.NO_MORE_DATA)) {
            return false;
        }

        return true;
    }

    /**
     * 判断第一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isFirstItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        int mostTop = (mListView.getChildCount() > 0) ? mListView.getChildAt(0)
                .getTop() : 0;
        if (mostTop >= 0) {
            return true;
        }

        return false;
    }

    /**
     * 判断最后一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = mListView.getLastVisiblePosition();

        /**
         * This check should really just be: lastVisiblePosition ==
         * lastItemPosition, but ListView internally uses a FooterView which
         * messes the positions up. For me we'll just subtract one to account
         * for it and rely on the inner condition which checks getBottom().
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition
                    - mListView.getFirstVisiblePosition();
            final int childCount = mListView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mListView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mListView.getBottom();
            }
        }

        return false;
    }

    // 设置适配器
    public void setAdapter(ListAdapter listAdapter) {
        getRefreshableView().setAdapter(listAdapter);
    }

    public void addHeaderView(View v) {
        getRefreshableView().addHeaderView(v);
    }
    public View addHeaderView(@LayoutRes int layoutID) {
        View header = ViewUtils.getInflateView(getRefreshableView(), layoutID);
        getRefreshableView().addHeaderView(header);
        return header;
    }

    public View addFooterView(@LayoutRes int layoutID) {
        View footer = ViewUtils.getInflateView(getRefreshableView(), layoutID);
        getRefreshableView().addFooterView(footer);
        return footer;
    }
    public void addFooterView(View v) {
        getRefreshableView().addFooterView(v);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        getRefreshableView().setOnItemLongClickListener(listener);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        getRefreshableView().setOnItemClickListener(onItemClickListener);
    }

    public void setPinnedHeaderView(View view) {
        getRefreshableView().setPinnedHeaderView(view);
    }

    public void setPinnedHeaderView(@LayoutRes int resID) {
        getRefreshableView().setPinnedHeaderView(LayoutInflater.from(getContext()).inflate(resID, getRefreshableView(), false));
    }

    /**
     * 开始执行加载数据
     */
    public void doGetData() {
        if (emptyLayout != null)
            getEmptyLayout().showLoading();
        onRefreshListener.onRefresh();
    }

    protected PullToRefreshListView.OnRefreshPulldownListener onRefreshListener;

    /**
     * &#x5237;&#x65b0;&#x4e8b;&#x4ef6;&#xff0c;&#x53ca;&#x9519;&#x8bef;&#x70b9;&#x51fb;&#x4e8b;&#x4ef6;&#xff08;*&#xff09;
     */
    public void setOnRefreshPulldownListener(
            PullToRefreshListView.OnRefreshPulldownListener refreshListener) {
        this.onRefreshListener = refreshListener;
        setOnRefreshListener(new OnRefreshListener<PinnedHeaderListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<PinnedHeaderListView> refreshView) {
                doGetData();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<PinnedHeaderListView> refreshView) {
            }
        });
        if (getEmptyLayout() != null) {
            emptyLayout.setErrorButtonClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    doGetData();
                }
            });
        }

    }

    /***
     * 不设置错误button事件
     */
    public void setErrorButtonClickListenerNull() {
        if (getEmptyLayout() != null)
            emptyLayout.setErrorButtonClickListener(null);
    }

    @Override
    public void showError() {
        if (getEmptyLayout() != null)
            emptyLayout.showError();
    }

    @Override
    public void showEmpty() {
        if (getEmptyLayout() != null)
            emptyLayout.showEmpty();
    }

    @Override
    public void showLoading() {
        post(new Runnable() {// 避免子线程操作view
            @Override
            public void run() {
                if (getEmptyLayout() != null)
                    emptyLayout.showLoading();
            }
        });
    }

    /**
     * 显示加载完成的样子
     */
    @Override
    public void showNormal() {
    }

    @Override
    public EmptyLayout getEmptyLayout() {
        if (isHasEmptyLayout == true && emptyLayout == null)
            emptyLayout = new EmptyLayout(getContext(), getRefreshableView());
        return emptyLayout;
    }

    /**
     * 是否含有emptylayout
     *
     * @return
     */
    public boolean isHasEmptyLayout() {
        return isHasEmptyLayout;
    }

    public void setHasEmptyLayout(boolean isHasEmptyLayout) {
        /**
         * 设置
         */
        this.isHasEmptyLayout = isHasEmptyLayout;
    }

    /**
     * 添加 一个顶部的线
     *
     */
    @Override
    public void addHeaderLine() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.divider_common_15, getRefreshableView(), false);
        getRefreshableView().addHeaderView(view);
    }

    @Override
    public void addHeaderLineNoHeight() {
        getRefreshableView().addHeaderView(new Space(getContext()));
    }

    public int getHeaderViewsCount() {
        return getRefreshableView().getHeaderViewsCount();
    }

    /**
     * 设置emptylayout
     */
    public void setEmptyView(ViewGroup viewGroup) {
        if (getEmptyLayout() != null)
            emptyLayout.setEmptyView(viewGroup);
    }

    //去掉做分隔线
    public void setNoDivider() {
        getRefreshableView().setDividerHeight(0);
    }

    public void setDivider(@DrawableRes int resID, @IntRange(from = 1, to = 20) int pxDividerHeight) {
        getRefreshableView().setDivider(getResources().getDrawable(resID));
        getRefreshableView().setDividerHeight(pxDividerHeight);
    }

}
