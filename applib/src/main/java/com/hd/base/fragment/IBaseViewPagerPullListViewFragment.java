package com.hd.base.fragment;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.alibaba.fastjson.JSON;
import com.hd.R;
import com.hd.base.adapterbase.BaseViewHolder;
import com.hd.base.adapterbase.SuperAdapter;
import com.hd.net.NetCallback;
import com.hd.net.socket.NetEntity;
import com.hd.utils.ViewUtils;
import com.hd.utils.reflect.ClassUtils;
import com.hd.view.listview.PulltoRefreshAAMListView;
import com.hd.view.ui.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 带有titleBar pagerSlider viewPager 多个 Pull2RefreshListView的组合模板基类
 * Created by liugd on 2017/3/28.
 */

public abstract class IBaseViewPagerPullListViewFragment<T> extends IBaseViewPagerFragment implements NetCallback, AdapterView.OnItemClickListener, PullToRefreshListView.OnRefreshPulldownListener {

    protected PulltoRefreshAAMListView[] listViews;

    protected BaseAdapter[] adapters;//适配器

    protected List<T>[] listDatas;//list数据

    protected Class<T> tClass;//类型

    protected boolean[] ifGetDatas;//是否获得数据


    @Override
    protected final void initViewPagerView() {
        tClass = (Class<T>) ClassUtils.getClassType(this.getClass());//获取泛型的类型
        listDatas = new ArrayList[listViews.length];//list数据
        ifGetDatas = new boolean[listViews.length];//是否获得数据
        adapters = new BaseAdapter[listViews.length];//适配器的数组
        //放在初始化适配器之前，有可能listView会加头部，如果设置适配器后加头部会出问题
        initViewPagerListView();

        for (int i = 0; i < listViews.length; i++) {
            PulltoRefreshAAMListView listView = listViews[i];
            listView.setOnItemClickListener(this);
//            initListView(listView, i);
            List<T> list = listDatas[i] = new ArrayList<>();
            BaseAdapter adapter = adapters[i] = setBaseAdapter(mContext, list, i);
            listView.setAdapter(adapter);
            listView.initAllDatas(list, false, adapter, IBaseViewPagerPullListViewFragment.this);
        }
        //第一次请求数据
        onPagerSelected(mType);
    }

//    /***
//     * 初始化listView
//     *
//     * @param listView
//     */
//    protected void initListView(PulltoRefreshAAMListView listView, int mType) {
//    }

    /***
     * 外部重写此方法，可根据mType的不同传入不同的适配器，增强扩展能力
     *
     * @param mType
     * @return
     */
    protected BaseAdapter setBaseAdapter(Context mContext, List<T> listData, int mType) {
        return new MySuperAdapter(mContext, listData, mType);
    }


    /***
     * 刷新时调用
     */
    @Override
    public final void onRefresh() {
        listViews[mType].showLoading();
        setAPIRequest(mType);
    }


    /***
     * @param position
     */
    @Override
    protected final void onPagerSelected(int position) {
        //选择tab控制数据有无请求
        if (ifGetDatas[position] == false) {
            ifGetDatas[position] = true;
            listViews[mType].postDelayed(new Runnable() {
                @Override
                public void run() {
                    onRefresh();
                }
            }, 100);//可以防止卡顿问题
        }
        onPagerSelectedCallback(position);
    }


    /***
     * 页码回调
     * @param position
     */
    protected void onPagerSelectedCallback(int position) {
    }


    /**
     * 设置当前页是否获得数据
     *
     * @param pager
     * @param ifGetData
     */
    public void setPagerIfGetData(int pager, boolean ifGetData) {
        this.ifGetDatas[pager] = ifGetData;
    }

    /***
     * 这里弃用它
     */
    @Deprecated
    @Override
    public final void startGetMyData() {
    }

    @Override
    protected final View[] getViewLists() {
        int size = getTitleString().split("\\|").length;
        listViews = new PulltoRefreshAAMListView[size];
        View views[] = new View[size];
        for (int i = 0; i < size; i++) {
            View rootView = ViewUtils.getInflateView(mContext, setItemLayoutId(i));
            PulltoRefreshAAMListView listView = rootView.findViewById(R.id.listView);
            views[i] = rootView;
            listViews[i] = listView;
            onConfigView(i, rootView, listView);
        }
        return views;
    }

    /***
     * 配置listView
     * @param mType
     * @param rootView
     * @param listView
     */
    protected void onConfigView(int mType, View rootView, PulltoRefreshAAMListView listView) {
    }

    /***
     *  配置每页的布局
     * @return
     */
    protected int setItemLayoutId(int mType) {
        return R.layout.layout_base_viewpager_pulllistview;
    }


    @Override
    public void onSuccess(NetEntity entity) throws Exception {
        List<T> list = JSON.parseArray(entity.data, tClass);
        listViews[(int) entity.flag].setDataFromNetWork(list);//设置数据来自网络
    }

    @Override
    public void onError(NetEntity entity) throws Exception {
        ifGetDatas[(int) entity.flag] = false;
        listViews[(int) entity.flag].showError();
    }


    @Override
    public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position -= listViews[mType].getRefreshableView().getHeaderViewsCount();
        if (position < 0) {
            return;
        }
        List<T> list = listDatas[mType];
        if (list != null && list.size() > 0) {
            onItemClickSafely(list.get(position), position);
        }
    }

    /**
     * 设置不可以滚动动底加载更多
     */
    public void setScrollLoadEnabledFalse() {
        for (int i = 0; i < listViews.length; i++) {
            listViews[i].setScrollLoadEnabled(false);
        }
    }

    /***
     * 设置不可以下拉刷新
     */
    public void setPullRefreshEnabledFalse() {
        for (int i = 0; i < listViews.length; i++) {
            listViews[i].setPullRefreshEnabled(false);
        }
    }

    public PulltoRefreshAAMListView getCurListView() {
        return listViews[mType];
    }

    public List<T> getCurListData() {
        return listDatas[mType];
    }

    public BaseAdapter getCurAdapter() {
        return adapters[mType];
    }

    /***
     * 获得当前listdata的最后一个条目
     *
     * @return
     */
    public T getCurTailItem() {
        List<T> list = getCurListData();
        if (list.isEmpty() || getCurListView().isFirstPager()) {
            return null;
        } else {
            return list.get(list.size() - 1);
        }
    }


    public List<T>[] getListDatas() {
        return listDatas;
    }

    public BaseAdapter[] getAdapters() {
        return adapters;
    }

    public PulltoRefreshAAMListView[] getListViews() {
        return listViews;
    }


    /***
     * 初始化页面
     */
    protected abstract void initViewPagerListView();

    /***
     * 设置请求接口
     *
     * @param mType
     */
    protected abstract void setAPIRequest(int mType);


    //点击回调，这里作了去掉头部容错处理
    protected abstract void onItemClickSafely(T item, int position);

    /***
     * 设置适配器的layoutID
     * 可根据mType制定不同的布局
     *
     * @return
     */
    public int setAdapterLayoutID(int mType) {
        return 0;
    }

    /***
     * 适配器的绑定方法
     *
     * @param holder
     * @param item
     * @param position
     */
    protected void onAdapterBind(BaseViewHolder holder, T item, int position, int mType) {

    }


    private class MySuperAdapter extends SuperAdapter<T> {

        private int pageType;

        public MySuperAdapter(Context context, List<T> data, int pageType) {
            super(context, data, setAdapterLayoutID(pageType));
            this.pageType = pageType;
        }

        @Override
        protected void onBind(BaseViewHolder holder, T item, int position) {
            onAdapterBind(holder, item, position, pageType);
        }
    }

}
