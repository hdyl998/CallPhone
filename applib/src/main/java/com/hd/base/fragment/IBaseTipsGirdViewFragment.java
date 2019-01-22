package com.hd.base.fragment;

import android.content.Context;
import android.view.View;
import android.widget.GridView;

import com.hd.view.gridview.MyTipsGridView;


/**
 * Created by liugd on 2017/5/15.
 */

public abstract class IBaseTipsGirdViewFragment<T> extends IBaseTipsViewFragment<T, MyTipsGridView> {
    @Override
    protected View setBodyView(Context mContext) {
        tipsView = new MyTipsGridView(mContext);
        return tipsView;
    }

    public GridView getContentView() {
        return tipsView.getContentView();
    }
}
