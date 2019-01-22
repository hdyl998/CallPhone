package com.hd.base.adapterbase;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.hd.R;
import com.hd.base.maininterface.ISComCallBacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 帮助查找viewID的一个适配器工具类,具体用法看，我的 页面
 * Created by liugd on 2017/4/26.
 */

public abstract class FindViewAdapter<T> {
    private ViewGroup mLayout;
    private int[] mIDs;
    private List<T> mDatas;
    private SparseArray<BaseViewHolder> mVHCahces;//缓存池子,优化处理的

    //重载
    public FindViewAdapter(ViewGroup mLayout, List<T> array, int ids[]) {
        this.mLayout = mLayout;
        this.mIDs = ids;
        this.mDatas = array;
        mVHCahces = new SparseArray<>();
        mLayout.post(()->notifyDataSetChanged());
    }

    //重载
    public FindViewAdapter(ViewGroup mLayout, T[] array, int ids[]) {
        this(mLayout, Arrays.asList(array), ids);
    }

    //重载
    public FindViewAdapter(ViewGroup mLayout, int ids[]) {
        this(mLayout, new ArrayList<T>(), ids);
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public void setDatas(T[] mDatas) {
        setDatas(Arrays.asList(mDatas));
    }

    private ISComCallBacks<T, Integer> onItemClickListener;


    public void setOnItemClickListener(ISComCallBacks<T, Integer> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private View.OnClickListener onClickListener;


    /***
     * 刷新数据
     */
    public void notifyDataSetChanged() {
        if (mDatas != null && mDatas.size() != 0) {
            int min = Math.min(mIDs.length, mDatas.size());
            for (int i = 0; i < min; i++) {
                BaseViewHolder holder = mVHCahces.get(mIDs[i]);
                if (holder == null) {
                    holder = BaseViewHolder.get(mLayout, mIDs[i]);
                    mVHCahces.put(mIDs[i], holder);
                }
                onBind(holder, mDatas.get(i), i);

                if (onItemClickListener != null) {
                    if (onClickListener == null) {
                        onClickListener = (v) -> {
                            int position = (int) v.getTag(R.id.tagDefault);
                            onItemClickListener.call(mDatas.get(position), position);
                        };
                    }
                    holder.getItemView().setTag(R.id.tagDefault, i);
                    holder.getItemView().setOnClickListener(onClickListener);
                }
            }
        }
    }

    public abstract void onBind(BaseViewHolder holder, T item, int position);


}
