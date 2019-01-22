package com.hd.view.scrollview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.hd.view.BaseTipsView;


public class MyTipsScrollView extends BaseTipsView<ScrollView> {

    public MyTipsScrollView(Context context) {
        this(context, null);
    }

    public MyTipsScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected ScrollView createTipsView(Context context, AttributeSet attrs) {
        return new ScrollView(context,attrs);
    }

    /**
     * 显示正常样子
     */
    @Override
    public void showNormal() {
        super.showNormal();
    }

    /**
     * 添加scrollView Body
     */
    public void addScorllViewBody(View view) {
        contentViewT.addView(view);
    }

    /**
     * 添加scrollView Body
     */
    public void addScorllViewBody(@LayoutRes int res) {
        contentViewT.addView(View.inflate(getContext(), res, null));
    }

    @Override
    public void showError() {
        contentViewT.setVisibility(View.GONE);
        super.showError();
    }

    @Override
    public void showEmpty() {
        contentViewT.setVisibility(View.GONE);
        super.showEmpty();
    }

    @Override
    public void showLoading() {
        contentViewT.setVisibility(View.GONE);
        super.showLoading();
    }
}
