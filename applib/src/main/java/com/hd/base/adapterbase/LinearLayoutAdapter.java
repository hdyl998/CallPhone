package com.hd.base.adapterbase;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.hd.R;

import java.util.List;

/**
 * 线性布局的适配器,一次性初始化，支持多种适配器
 * Created by liugd on 2017/4/21.
 */

public abstract class LinearLayoutAdapter<T> {
    protected int mItemLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected LinearLayout mLayout;
    protected IMultiItemViewType<T> mMultiItemViewType;
    protected Context mContext;


    private LinearLayoutAdapter(LinearLayout mLayout, List<T> data, int layoutResId, IMultiItemViewType<T> multiItemViewType) {
        this.mDatas = data;
        this.mMultiItemViewType = multiItemViewType;
        this.mItemLayoutId = layoutResId;
        this.mInflater = LayoutInflater.from(mLayout.getContext());
        this.mLayout = mLayout;
        this.mContext = mLayout.getContext();
        notifyDataSetChanged();
    }

    public LinearLayoutAdapter(@Nullable LinearLayout mLayout, int mItemLayoutId) {
        this(mLayout, null, mItemLayoutId);
    }

    public LinearLayoutAdapter(@Nullable LinearLayout mLayout, List<T> mDatas, int mItemLayoutId) {
        this(mLayout, mDatas, mItemLayoutId, null);
    }

    public LinearLayoutAdapter(@Nullable LinearLayout mLayout, List<T> mDatas, IMultiItemViewType<T> multiItemViewType) {
        this(mLayout, mDatas, 0, multiItemViewType);
    }

    public int getItemLayoutId() {
        return mItemLayoutId;
    }

    public void setItemLayoutId(int mItemLayoutId) {
        this.mItemLayoutId = mItemLayoutId;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public int setDividerHeight() {
        return 1;
    }

    /***
     * 是否有分割线
     *
     * @return
     */
    protected boolean isDivider() {
        return false;
    }

    public LinearLayout getLinearLayout() {
        return mLayout;
    }

    public void notifyDataSetChanged() {
        mLayout.removeAllViews();
        if (null != mDatas && !mDatas.isEmpty()) {
            if (mMultiItemViewType != null) {
                int ids[] = mMultiItemViewType.getLayoutIDs();
                for (int i = 0; i < mDatas.size(); i++) {
                    T item = mDatas.get(i);
                    int index = mMultiItemViewType.getLayoutIDsIndex(item, i);
                    BaseViewHolder holder = BaseViewHolder.get(mInflater, null, mLayout, ids[index]);
                    onBind(holder, item, i);
                    if (isDivider() && i != 0) {
                        View viewLine = LayoutInflater.from(mContext).inflate(R.layout.view_line_1dimpx, mLayout, false);
                        mLayout.addView(viewLine);
                        viewLine.getLayoutParams().height = setDividerHeight();
                    }
                    mLayout.addView(holder.getItemView());
                    onAddedView(holder, item, i);
                }
            } else {
                for (int i = 0; i < mDatas.size(); i++) {
                    BaseViewHolder holder = BaseViewHolder.get(mInflater, null, mLayout, getItemLayoutId());
                    T item = mDatas.get(i);
                    onBind(holder, item, i);
                    if (isDivider() && i != 0) {
                        View viewLine = LayoutInflater.from(mContext).inflate(R.layout.view_line_1dimpx, mLayout, false);
                        mLayout.addView(viewLine);
                        viewLine.getLayoutParams().height = setDividerHeight();
                    }
                    onBeforeAddedView(holder, item, i);
                    mLayout.addView(holder.getItemView());
                    onAddedView(holder, item, i);
                }
            }
        }
    }

    public abstract void onBind(BaseViewHolder holder, T item, int position);


    public void onAddedView(BaseViewHolder holder, T item, int position) {

    }

    public void onBeforeAddedView(BaseViewHolder holder, T item, int position) {

    }

}
