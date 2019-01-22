package com.hd.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;


import com.hd.view.ui.PullToRefreshBase;
import com.hd.view.ui.PullToRefreshListView;

import java.util.List;

public class RTPullPinnedHeaderAAMListView extends RTPullPinnedHeaderListView {

    private int pager;
    private int mLoadDataCount = 20;
    private static final int mDefaultPage = 1;
    private BaseAdapter adapter;//适配器
    private List<?> listItems;

    public RTPullPinnedHeaderAAMListView(Context context) {
        this(context, null);
    }

    public RTPullPinnedHeaderAAMListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RTPullPinnedHeaderAAMListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        pager = mDefaultPage;
        // 非下拉样式
        setPullLoadEnabled(false);
        // 滚动到底部自动获取
        setScrollLoadEnabled(true);
    }

    // @Override
    // protected void startLoading() {
    // pager++;
    // super.startLoading();
    // }
    //
    // @Override
    // protected void startRefreshing() {
    // pager = mDefaultPage;
    // super.startRefreshing();
    //
    // }

    // // 数据是否来自上拉
    // public boolean isFromStartLoading() {
    // return isPullLoading();
    // }

    /**
     * 清除掉数据如果来自刷新
     */
    public void clearDatasIfRefresh() {
        if (isPullRefreshing() || pager == 1) {
            // 重置页码
            pager = mDefaultPage;
            listItems.clear();
        }
    }

    /**
     * 设置数据来自网络（）
     *
     * @param list 解析的数据
     */
    public void setDataFromNetWork(List list) {
        setDataFromNetWork(list, true);
    }


    public void setDataFromNetWork(List list, boolean isNotifidata) {
        clearDatasIfRefresh();
        if (list != null && list.size() != 0)
            this.listItems.addAll(list);
        if (isNotifidata) {
            this.adapter.notifyDataSetChanged();
        }
        super.onPullDownRefreshComplete();
        super.onPullUpRefreshComplete();
        boolean isSizeEmpty = list == null || list.size() == 0;//新加的size,size==0则显示无更多
        setHasMoreData(!isSizeEmpty);// false 显示 无更多,true 显示还有数据
        this.showEmpty();
    }


    /**
     * 设置列表数据
     *
     * @param list
     */
    private void setDataLists(List<?> list) {
        this.listItems = list;
        if (listItems == null)
            throw new RuntimeException("list 不能为空");
    }

    /***
     * 初始化所有需要的数据
     *
     * @param list            list的引用，不能为空且地址不能变
     * @param isLoadingData   是否加载数据
     * @param refreshListener 刷新时及上拉加载时,请求数据事件
     */
    @Deprecated
    public void initAllDatas(List<?> list, boolean isLoadingData, PullToRefreshListView.OnRefreshPulldownListener refreshListener) {
        setDataLists(list);
        setOnPullUpDownListener(refreshListener);
        // 加载第一页数据
        if (isLoadingData) {
            doGetData();
        }
    }

    public void initAllDatas(List<?> list, boolean isLoadingData, BaseAdapter adapter, PullToRefreshListView.OnRefreshPulldownListener refreshListener) {
        this.adapter = adapter;
        initAllDatas(list, isLoadingData, refreshListener);
    }

    @Override
    public void doGetData() {
        pager = mDefaultPage;
        super.doGetData();
    }

    /***
     * 刷新时及上拉加载时,请求数据,如果emptylayout不为空已设置网络错误 时请求相同的接口
     *
     * @param refreshListener 刷新事件
     */
    private void setOnPullUpDownListener(PullToRefreshListView.OnRefreshPulldownListener refreshListener) {
        super.onRefreshListener = refreshListener;
        setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<PinnedHeaderListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<PinnedHeaderListView> refreshView) {
                // 下拉刷新时页码重置
                doGetData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<PinnedHeaderListView> refreshView) {
                pager++;
                // 下拉加载时页码增加
                onRefreshListener.onRefresh();
            }
        });
        // 网络错误只有在空列表时才出现
        if (getEmptyLayout() != null) {
            emptyLayout.setErrorButtonClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    doGetData();
                }
            });
        }
    }

    /**
     * 结束上拉或者下拉
     */
    @Deprecated
    public void onPullUpDownComplete() {
        super.onPullDownRefreshComplete();
        super.onPullUpRefreshComplete();
        // 设置还有更多
        int len = listItems == null ? 0 : listItems.size();

        boolean isMore = len == 0 || len == pager * mLoadDataCount;
        setHasMoreData(isMore);// true 显示 无更多, len 为0时不显示无更多 true
    }

    @Override
    public void onPullDownRefreshComplete() {
        onPullUpDownComplete();
    }

    @Override
    public void onPullUpRefreshComplete() {
        onPullUpDownComplete();
    }

    public int getPager() {
        return pager;
    }

    public void setPager(int pager) {
        this.pager = pager;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    /***
     * 得到每页加载的数量
     */
    public int getPagerCount() {
        return mLoadDataCount;
    }

    /***
     * 得到每页加载的数量
     */
    public void setPagerCount(int count) {
        mLoadDataCount = count;
    }

    /**
     * 得到头部的数量
     *
     * @return
     */
    public int getHeaderCounts() {
        return getRefreshableView().getHeaderViewsCount();
    }

    /***
     * 当网络错误时调用
     */
    @Override
    public void showError() {
        super.showError();
        onPullDownRefreshComplete();
    }
}
