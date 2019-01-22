package com.hd.view.listview;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hd.R;
import com.hd.utils.DensityUtils;
import com.hd.utils.ViewUtils;
import com.hd.view.BaseTipsView;

public class MyTipsListView extends BaseTipsView<ListView> {
    public MyTipsListView(Context context) {
        this(context, null);
    }

    public MyTipsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected ListView createTipsView(Context context, AttributeSet attrs) {
        return new ListView(context, attrs);
    }

    public void addHeaderView(View view) {
        getContentView().addHeaderView(view);
    }

    public View addHeaderView(@LayoutRes int layoutID) {
        View header = ViewUtils.getInflateView(getContentView(), layoutID);
        getContentView().addHeaderView(header);
        return header;
    }

    public View addFooterView(@LayoutRes int layoutID) {
        View footer = ViewUtils.getInflateView(getContentView(), layoutID);
        getContentView().addFooterView(footer);
        return footer;
    }

    public void setAdapter(ListAdapter listAdapter) {
        getContentView().setAdapter(listAdapter);
    }

    public void addFooterView(View view) {
        getContentView().addFooterView(view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        getContentView().setOnItemClickListener(onItemClickListener);
    }


    /**
     * 去掉做分割线
     */
    public void setNoDivider() {
        getContentView().setDividerHeight(0);
    }


    /**
     * 添加 一个顶部的线
     */
    public void addHeaderLine() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.divider_common_15, getContentView(), false);
        getContentView().addHeaderView(view);
    }

    public void addFooterLine() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.divider_common_15, getContentView(), false);
        getContentView().addFooterView(view);
    }

    @Deprecated
    public void setDividerHeight(int height) {
        getContentView().setDividerHeight(DensityUtils.getDimenPx( height));
    }

    public void setSelectorNormal() {
        getContentView().setSelector(R.drawable.list_item_selectedbg);
    }

//
//    /**
//     * 设置listView的分割线
//     *
//     * @param resID
//     * @param dividerHeight
//     */
//    public void setDivider(@DrawableRes int resID, int dividerHeight) {
//        getContentView().setDivider(getResources().getDrawable(resID));
//        getContentView().setDividerHeight(dividerHeight);
//    }

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
        getContentView().setDivider(getResources().getDrawable(resID));
        getContentView().setDividerHeight(getResources().getDimensionPixelSize(dividerHeightRes));
    }

    /**
     * 分发事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchLisener != null) {
            touchLisener.OnTouch();
        }
        return super.dispatchTouchEvent(ev);
    }

    OnTouchLisener touchLisener;

    public void setTouchLisener(OnTouchLisener touchLisener) {
        this.touchLisener = touchLisener;
    }

    public interface OnTouchLisener {
        void OnTouch();
    }
}
