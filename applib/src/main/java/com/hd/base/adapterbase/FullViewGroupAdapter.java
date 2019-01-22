package com.hd.base.adapterbase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hd.R;
import com.hd.base.maininterface.ISComCallBacks;

import java.util.ArrayList;
import java.util.List;


/***
 * 线性布局适配器，可以复用ITEM ,只支持一种LAYOUT
 *
 * @param <T>
 * @author Administrator
 */
public abstract class FullViewGroupAdapter<T> {
    private int mItemLayoutId;
    private List<T> mDatas;

    private List<BaseViewHolder> mVHCahces;
    private LayoutInflater mInflater;

    private ViewGroup viewGroup;//支持Linearlayout  FlowViewGroup

    protected Context mContext;


    public FullViewGroupAdapter(ViewGroup viewGroup, int mItemLayoutId) {
        this(viewGroup, null, mItemLayoutId);
    }

    public FullViewGroupAdapter(ViewGroup viewGroup, List<T> mDatas, int mItemLayoutId) {
        this.mItemLayoutId = mItemLayoutId;
        this.mDatas = mDatas;
        this.mContext = viewGroup.getContext();
        this.mInflater = LayoutInflater.from(mContext);
        this.mVHCahces = new ArrayList<>();
        this.viewGroup = viewGroup;

        viewGroup.post(() -> notifyDataSetChanged());
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


    public ViewGroup getViewGroup() {
        return viewGroup;
    }


    public void notifyDataSetChanged() {
        if (null != viewGroup) {
            if (null != mDatas && !mDatas.isEmpty()) {
                int childCount = viewGroup.getChildCount();
                int dataSize = mDatas.size();
                if (dataSize > childCount) {

                } else if (dataSize < childCount) {
                    viewGroup.removeViews(dataSize, childCount - dataSize);
                    while (mVHCahces.size() > dataSize) {
                        mVHCahces.remove(mVHCahces.size() - 1);
                    }
                }
                for (int i = 0; i < dataSize; i++) {
                    BaseViewHolder holder;
                    if (mVHCahces.size() - 1 >= i) {
                        holder = mVHCahces.get(i);
                    } else {
                        holder = BaseViewHolder.get(mInflater, null, viewGroup, getItemLayoutId());
                        mVHCahces.add(holder);
                    }
                    onBind(holder, mDatas.get(i), i);
                    if (null == holder.getItemView().getParent()) {
                        viewGroup.addView(holder.getItemView());
                    }
                    if (onItemClickListener != null) {
                        holder.getItemView().setTag(R.id.tagDefault, i);
                        holder.getItemView().setOnClickListener(getOnClickListener());
                    }
                }
            } else {
                viewGroup.removeAllViews();
            }
        } else {
            viewGroup.removeAllViews();
        }
    }


    ISComCallBacks<T, Integer> onItemClickListener;


    public void setOnItemClickListener(ISComCallBacks<T, Integer> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private View.OnClickListener onClickListener;

    private View.OnClickListener getOnClickListener() {
        if(onClickListener==null)
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener==null){
                        return;
                    }
                    int position = (int) v.getTag(R.id.tagDefault);
                    onItemClickListener.call(mDatas.get(position), position);
                }
            };
        return onClickListener;
    }

    public abstract void onBind(BaseViewHolder holder, T item, int position);

}