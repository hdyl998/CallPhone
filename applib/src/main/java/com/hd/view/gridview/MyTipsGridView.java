package com.hd.view.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.hd.R;
import com.hd.view.BaseTipsView;

/**
 * Created by liugd on 2017/5/15.
 */

public class MyTipsGridView extends BaseTipsView<GridViewWithHeaderAndFooter> {
    public MyTipsGridView(Context context) {
        this(context, null);
    }

    public MyTipsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected GridViewWithHeaderAndFooter createTipsView(Context context, AttributeSet attrs) {
        return new GridViewWithHeaderAndFooter(context, attrs);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        getContentView().setOnItemClickListener(onItemClickListener);
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

    public void setAdapter(ListAdapter listAdapter) {
        getContentView().setAdapter(listAdapter);
    }
}
