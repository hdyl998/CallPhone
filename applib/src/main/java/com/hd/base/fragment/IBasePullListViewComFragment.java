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
import com.hd.utils.Utils;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.reflect.ClassUtils;
import com.hd.view.ui.PullToRefreshBase;
import com.hd.view.ui.PullToRefreshListView;


import java.util.ArrayList;
import java.util.List;

/**
 * 有一个titleBar和一个listView的界面
 * Created by liugd on 2017/3/30.
 */

public abstract class IBasePullListViewComFragment<T, V extends PullToRefreshBase> extends IBaseTitleBarFragment implements NetCallback, AdapterView.OnItemClickListener, PullToRefreshListView.OnRefreshPulldownListener {
    protected V listView;

    protected BaseAdapter adapter;//适配器

    protected List<T> listData;//list数据

    protected Class<T> tClass;//类型


//    @Override
//    public int getLayoutId() {
//        return R.layout.layout_base_pull_listview;
//    }

//    /***
//     * 这里可以定制重写定义不同样式的下拉组件可以从layout里面解析，也可以NEW生成
//     *
//     * @param mContext
//     * @return
//     */
//    @Override
//    protected View setBodyView(Context mContext) {
//        try {
//            listView = createInstance(mContext);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        listView.setNoDivider();
//        listView.addHeaderLine(10);
//        return listView;
//    }

//    /***
//     * 创建一个实例
//     *
//     * @param mContext
//     * @return
//     * @throws Exception
//     */
//    private V createInstance(Context mContext) throws Exception {
//        Class<V> clazz = ClassUtils.getClassType(IBasePullListViewComFragment.class, 1);
//        Constructor<V> constructor = clazz.getConstructor(Context.class);
//        V instance = constructor.newInstance(mContext);
//        return instance;
//    }


    @Override
    public final void initTitleBarView() {
        //寻找用布局创建的listView
        int layoutID = getLayoutId();
        if (layoutID != 0) {
            listView = findViewByID(R.id.listView);
        }
        tClass = (Class<T>) ClassUtils.getClassType(this.getClass());//获取泛型的类型
        LogUitls.print("范型的类型", tClass + "" + isInited());

        listData = new ArrayList();//list数据
        initTitleBarListView();
        //设置适配器
        adapter = setBaseAdapter(mContext, listData);
        listView.setOnItemClickListener(this);
        listView.initAllDatas(listData, false, adapter, this);

        listView.setAdapter(adapter);//放最后，不能在设置头部之前设置适配器
        //开始请求数据
        onRefresh();
    }


    /***
     * 设置适配器，外部可以重写此方法自定义适配器，这里写成活的
     *
     * @param mContext
     * @param listData
     * @return
     */
    protected BaseAdapter setBaseAdapter(Context mContext, List<T> listData) {
        return new SuperAdapter<T>(mContext, listData, setAdapterLayoutID()) {
            @Override
            protected void onBind(BaseViewHolder holder, T item, int position) {
                onAdapterBind(holder, item, position);
            }
        };
    }


    @Override
    public void startGetMyData() {
        onRefresh();
    }

    /***
     * 刷新时调用
     */
    @Override
    public final void onRefresh() {
        setGetData(true);
        if (listView != null) {
            listView.showLoading();
        }
        setAPIRequest();
    }


    @Override
    public void onSuccess(NetEntity entity) throws Exception {
        listView.setDataFromNetWork((List) entity.getDataBean());//设置数据来自网络
        listView.setPullRefreshEnabled(!listData.isEmpty());
    }


    @Override
    public void onError(NetEntity entity) throws Exception {
        isGetData = false;
        listView.showError();
    }


    @Override
    public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position -= listView.getHeaderViewsCount();//去掉头部的影响
        T item = Utils.getListItemSafely(listData, position);
        if (item != null) {
            onItemClickSafely(item, position);
        }
    }

    /**
     * 设置不可以滚动动底加载更多
     */
    public void setScrollLoadEnabledFalse() {
        listView.setScrollLoadEnabled(false);
    }

    /***
     * 设置不可以下拉刷新
     */
    public void setPullRefreshEnabledFalse() {
        listView.setPullRefreshEnabled(false);
    }


    public List<T> getListData() {
        return listData;
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

    public V getListView() {
        return listView;
    }


    /***
     * 初始化界面需要做的
     */
    protected abstract void initTitleBarListView();

    /***
     * 设置请求接口，这里与通用命名保持一致
     */
    protected abstract void setAPIRequest();

    //点击回调，这里作了去掉头部容错处理
    protected abstract void onItemClickSafely(T item, int position);


    public int setAdapterLayoutID() {
        return 0;
    }

    /***
     * 适配器的绑定方法
     *
     * @param holder
     * @param item
     * @param position
     */
    protected void onAdapterBind(BaseViewHolder holder, T item, int position) {

    }


    //默认自定义解析方式,优先解析成数组
    @Override
    public Object onParseData(String data) {
        return JSON.parseArray(data, tClass);
    }

}
