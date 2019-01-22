package com.hd.view.listview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;


import com.hd.R;
import com.hd.view.ui.PullToRefreshBase;
import com.hd.view.ui.PullToRefreshListView;

import java.util.List;

public class PulltoRefreshAAMListView extends PullToRefreshListView {

    private int pager;
    private int mLoadDataCount = 20;
    private static final int mDefaultPage = 1;

    private List<?> listItems;
    private BaseAdapter adapter;//适配器

    public PulltoRefreshAAMListView(Context context) {
        this(context, null);
    }

    public PulltoRefreshAAMListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PulltoRefreshAAMListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.PulltoRefreshAAMListView);
        boolean addHeaderLine = mTypedArray.getBoolean(R.styleable.PulltoRefreshAAMListView_addHeaderLine, false);
        mTypedArray.recycle();
        if (addHeaderLine) {
            addHeaderLine();
        }
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
        if (isFirstRequest()) {
            // 重置页码
            pager = mDefaultPage;
            listItems.clear();
        }
    }

    public boolean isFirstRequest() {
        if (isPullRefreshing() || pager == 1) {
            return true;
        }
        return false;
    }


    /***
     * 是否是第一页
     *
     * @return
     */
    public boolean isFirstPager() {
        return pager == 1;
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
    public void initAllDatas(List<?> list, boolean isLoadingData, OnRefreshPulldownListener refreshListener) {
        setDataLists(list);
        setOnPullUpDownListener(refreshListener);
        // 加载第一页数据
        if (isLoadingData) {
            doGetData();
        }
    }

    /**
     * 设置数据
     *
     * @param list
     * @param isLoadingData
     * @param adapter
     * @param refreshListener
     */
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
    private void setOnPullUpDownListener(OnRefreshPulldownListener refreshListener) {
        super.onRefreshListener = refreshListener;
        setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 下拉刷新时页码重置
                doGetData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
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
     * 设置来自网络的数据
     */
    public void setDataFromNetWork(List list) {
        setDataFromNetWork(list, true, false);
    }

    /**
     * 设置来自网络的数据
     */
    public void setDataFromNetWork(List list, boolean isNotifidata) {
        setDataFromNetWork(list, isNotifidata, false);
    }

    public void setDataFromNetWork(List list, boolean isNotifidata, boolean isAddFirst) {
        clearDatasIfRefresh();
        if (list != null && list.size() != 0)
            if (isAddFirst) {
                this.listItems.addAll(0, list);
            } else {
                this.listItems.addAll(list);
            }
        if (isNotifidata) {
            if (this.adapter == null) {
                throw new RuntimeException("适配器不能为空，请初始化适配器");
            }
            this.adapter.notifyDataSetChanged();
        }
        super.onPullDownRefreshComplete();
        super.onPullUpRefreshComplete();
        boolean isSizeEmpty = list == null || list.size() == 0;//新加的size,size==0则显示无更多
        setHasMoreData(!isSizeEmpty);// false 显示 无更多,true 显示还有数据

        this.showEmpty();
    }

    /**
     * 结束上拉或者下拉
     */
    @Deprecated
    public void onPullUpDownComplete() {
        super.onPullDownRefreshComplete();
        super.onPullUpRefreshComplete();
        // 设置还有更多
        int len = listItems.size();

        boolean isMore = len == 0 || len >= pager * mLoadDataCount;
        setHasMoreData(isMore);// false 显示 无更多, len 为0时不显示无更多 true
    }


    /***
     * 当网络错误时调用
     */
    @Override
    public void showError() {
        super.onPullDownRefreshComplete();
        super.onPullUpRefreshComplete();
        super.showError();
    }

    //    public void onPullUpDownComplete2() {
//        super.onPullDownRefreshComplete();
//        super.onPullUpRefreshComplete();
//        // 设置还有更多
////        int len = listItems.size();
////        boolean isMore = len == 0|| len == pager * mLoadDataCount;
//        setHasMoreData(true);// false 显示 无更多, len 为0时不显示无更多 true
//    }
    @Deprecated
    @Override
    public void onPullDownRefreshComplete() {
        onPullUpDownComplete();
    }

    @Deprecated
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


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (listener != null)
                        listener.onTap();
                    break;
            }
            return super.dispatchTouchEvent(event);
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 设置触摸时的回调接口
     *
     * @param listener
     */
    public void setOnTapListener(OnTapListener listener) {
        this.listener = listener;
    }

    private OnTapListener listener;

    public interface OnTapListener {
        void onTap();
    }
}
