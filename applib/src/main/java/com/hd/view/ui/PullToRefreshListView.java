package com.hd.view.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
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
import android.widget.ListView;
import android.widget.TextView;

import com.hd.R;
import com.hd.base.maininterface.IErrorRequest;
import com.hd.utils.ViewUtils;
import  com.hd.view.ui.ILoadingLayout.State;

import com.hd.view.EmptyLayout;

/**
 * 这个类实现了ListView下拉刷新，上加载更多和滑到底部自动加载
 *
 * @author Li Hong
 * @since 2013-8-15
 */
public class PullToRefreshListView extends PullToRefreshBase<ListView> implements OnScrollListener, EmptyLayout.IOnEmptyDataChangedListener {

    /**
     * ListView
     */
    private ListView mListView;
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
    public PullToRefreshListView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context mContext
     * @param attrs   attrs
     */
    public PullToRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法
     *
     * @param context  mContext
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPullLoadEnabled(false);
        isHasEmptyLayout = true;
    }

    @Override
    protected ListView createRefreshableView(Context context, AttributeSet attrs) {
        ListView listView = new ListView(context, attrs);
        listView.setId(NO_ID);
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
//        LogUitls.print("setHasMoreData" + hasMoreData);
        if (!hasMoreData) {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
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

//    /**
//     * 对listView滚动时加载图片作的优化，快速滚动时，不加载图片，慢慢滚动时还是要加载图片的
//     */
//    public void setOnScrollPauseLoadingPicture() {
//        setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true));
//    }


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
        try {
            if (SCROLL_STATE_TOUCH_SCROLL == scrollState) {//solve the HuaweiChe1-Cl10 crash.
                View currentFocus = ((Activity) getContext()).getCurrentFocus();
                if (currentFocus != null) {
                    currentFocus.clearFocus();
                }
            }
        } catch (Exception e) {//try catch when the getContext() is null.
            e.printStackTrace();
        }

        if (isScrollLoadEnabled() && hasMoreData()) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                if (isReadyForPullUp()) {
                    startLoading();
                }
            }
        }

        if (null != mScrollListener) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }


    }

    /**
     * 得到头部的数量
     *
     * @return
     */
    public int getHeaderCounts() {
        return getRefreshableView().getHeaderViewsCount();
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
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
        if ((null != mLoadMoreFooterLayout) && (mLoadMoreFooterLayout.getState() == State.NO_MORE_DATA)) {
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

        int mostTop = (mListView.getChildCount() > 0) ? mListView.getChildAt(0).getTop() : 0;
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
            final int childIndex = lastVisiblePosition - mListView.getFirstVisiblePosition();
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

    public View addHeaderView(View v) {
        getRefreshableView().addHeaderView(v);
        return v;
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

    public View addFooterView(View v) {
        getRefreshableView().addFooterView(v);
        return v;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        getRefreshableView().setOnItemLongClickListener(listener);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        getRefreshableView().setOnItemClickListener(onItemClickListener);
    }

    /**
     * 开始执行加载数据
     */
    public void doGetData() {
        if (emptyLayout != null)
            getEmptyLayout().showLoading();
        if (onRefreshListener != null)
            onRefreshListener.onRefresh();
    }

    protected OnRefreshPulldownListener onRefreshListener;

    /**
     * 刷新事件，及错误点击事件（*）
     */
    public void setOnRefreshPulldownListener(OnRefreshPulldownListener refreshListener) {
        this.onRefreshListener = refreshListener;
        setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                doGetData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        if (getEmptyLayout() != null) {
            emptyLayout.setErrorButtonClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    doGetData();
                }
            });
        }

    }

    public void setOnRefreshPulldownListener(final IErrorRequest iErrorRequest) {
        OnRefreshPulldownListener listener = new OnRefreshPulldownListener() {
            @Override
            public void onRefresh() {
                iErrorRequest.startGetMyData();
            }
        };
        setOnRefreshPulldownListener(listener);
    }


    /***
     * 不设置错误button事件
     */
    public void setErrorButtonClickListenerNull() {
        if (getEmptyLayout() != null)
            emptyLayout.setErrorButtonClickListener(null);
    }

    public interface OnRefreshPulldownListener {
        public void onRefresh();
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
        if (getEmptyLayout() != null)
            emptyLayout.showLoading();
    }

    /**
     * 显示加载完成的样子
     */
    @Override
    public void showNormal() {
        if (getEmptyLayout() != null)
            emptyLayout.setViewGone();
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


    public void addFooterLine() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.divider_common_15, getRefreshableView(), false);
        getRefreshableView().addFooterView(view);
    }


    /**
     * 设置emptylayout
     */
    public void setEmptyView(ViewGroup viewGroup) {
        if (getEmptyLayout() != null)
            emptyLayout.setEmptyView(viewGroup);
    }

    /**
     * 标志是否初化化emptylayout
     */
    boolean isSetEmptylayout = false;

    /***
     * 设置空数据样式为两行样式
     *
     * @param fistString
     * @param secondString
     */
    public void setEmptyViewOfDoubleLine(String fistString, String secondString) {
        if (isSetEmptylayout == false) {
            getEmptyLayout().setEmptyView(ViewUtils.getEmptyViewOfDoubleLine(getContext(), fistString, secondString));
        } else {
            ViewGroup viewGroup = getEmptyLayout().getEmptyView();
            try {
                TextView tView = (TextView) viewGroup.getChildAt(0);
                TextView tView2 = (TextView) viewGroup.getChildAt(1);
                tView.setText(fistString);
                tView2.setText(secondString);
            } catch (Exception e) {
            }
        }
        isSetEmptylayout = true;
    }

    /**
     * 初始化双行emptylayout,并可以动态设值
     *
     * @param residDoubleMsg
     */
    public void setEmptyViewOfDoubleLine(@StringRes int residDoubleMsg) {
        String strings[] = getContext().getResources().getString(residDoubleMsg).split("\\|");
        if (strings.length < 2) {
            setEmptyViewOfDoubleLine(strings[0], "");
        } else {
            setEmptyViewOfDoubleLine(strings[0], strings[1]);
        }
    }

    public int getHeaderViewsCount() {
        return getRefreshableView().getHeaderViewsCount();
    }

    /**
     * 设置LISTVIEW 没有分隔线
     */
    public void setNoDivider() {
        getRefreshableView().setDividerHeight(0);

    }

    /**
     * 设置listView的分割线
     *
     * @param resID
     * @param pxDividerHeight
     */
    @Deprecated
    public void setDivider(@DrawableRes int resID, @IntRange(from = 1, to = 20) int pxDividerHeight) {
        getRefreshableView().setDivider(getResources().getDrawable(resID));
        getRefreshableView().setDividerHeight(pxDividerHeight);
    }

    /**
     * 设置1px的高度的分割线
     */
    public void setDivider1pxHeight() {
        setDividerRes(R.color.divider1pxColor, R.dimen.divider1pxHeight);
    }

    /**
     * 设置大高度的分割线
     */
    public void setDividerBigHeight() {
        setDividerRes(R.color.dividerBigColor, R.dimen.dividerBigContentContent);
    }

    /**
     * 设置listView的分割线
     *
     * @param resID
     * @param dividerHeightRes
     */
    public void setDividerRes(@DrawableRes int resID, @DimenRes int dividerHeightRes) {
        getRefreshableView().setDivider(getResources().getDrawable(resID));
        getRefreshableView().setDividerHeight(getResources().getDimensionPixelSize(dividerHeightRes));
    }

    /***
     * 设置底部加载更多的文本
     *
     * @param text
     */
    public void setLoadingFooterText(String text) {
        ((FooterLoadingLayout) mLoadMoreFooterLayout).setFooterText(text);
    }
}
