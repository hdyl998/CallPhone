package com.hd.base.fragment;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.hd.R;
import com.hd.base.adapterbase.BaseViewHolder;
import com.hd.base.adapterbase.SuperAdapter;
import com.hd.view.BaseTipsView;

import java.util.ArrayList;
import java.util.List;

/**
 * 可显示空布局的listView GridView的基类，不含网络操作，其子类为IBaseTipsGridViewFragment,和IBaseTipsListViewFragment
 * Created by liugd on 2017/5/12.
 */

public abstract class IBaseTipsViewFragment<T, V extends BaseTipsView> extends IBaseTitleBarFragment implements AdapterView.OnItemClickListener {

    protected V tipsView;
    protected BaseAdapter adapter;//适配器
    public List<T> listData;//list数据

//    @Override
//    protected View setBodyView(Context mContext) {
//        Class<V> clazz = ClassUtils.getClassType(getClass().getSuperclass(),1);
//        return tipsView = ClassUtils.getClassNewInstance(clazz, mContext);
//    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void initTitleBarView() {
        //寻找用布局创建的tipsView
        int layoutID = getLayoutId();
        if (layoutID != 0) {
            tipsView = findViewByID(R.id.listView);//ID 这里使用listView
        }
        tipsView.setOnItemClickListener(this);
        tipsView.showLoading();
        listData = new ArrayList();//list数据
        adapter = setBaseAdapter(mContext, listData);
        initTitleBarListView();

        tipsView.setAdapter(adapter);//放最后，不能在设置头部之前设置适配器
    }

    /***
     * 初始化界面需要做的
     */
    protected abstract void initTitleBarListView();

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

    /***
     * 设置数据并且更新空布局
     *
     * @param listData
     */
    public void setListDataAndNotifyData(List<T> listData) {
        if (listData != null) {
            this.listData.clear();
            this.listData.addAll(listData);
            tipsView.showEmpty();
            adapter.notifyDataSetChanged();
        } else {
            tipsView.showEmpty();
            adapter.notifyDataSetChanged();
        }
    }


    public void setListData(List<T> listData) {
        this.listData = listData;
    }

    public List<T> getListData() {
        return listData;
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

    public V getTipsView() {
        return tipsView;
    }


    public View getContentView() {
        return tipsView.getContentView();
    }
}
