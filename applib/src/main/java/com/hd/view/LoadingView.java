package com.hd.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.hd.R;
import com.hd.base.IBaseFragment;

/**
 * Created by liugd on 2017/6/23.
 */
//加载首屏时显示的view
public class LoadingView extends RelativeLayout {

    EmptyLayout emptyLayout;
    Activity mContext;

    boolean isLoadingSuccess;

    TitleBar titleBar;

    public LoadingView(Context mContext, IBaseFragment iBaseFragment) {
        super(mContext);
        this.mContext = (Activity) mContext;
        initViews(mContext, iBaseFragment.isStatusBarLightStyle());
        initTitleBarMarginTop(iBaseFragment);
        titleBar.initFragmentSetting(iBaseFragment);
    }

    //
    private void initViews(Context mContext, boolean isLightStyle) {
        ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        addView(titleBar = new TitleBar(mContext), ll);
        if (!isLightStyle) {
            titleBar.setDarkStyle();
        }

        emptyLayout = new EmptyLayout(mContext, this);
        this.setBackgroundResource(R.color.defaultBgColor);
        addToActivity();
        emptyLayout.showLoading();
    }

    /***
     * 针对状态栏无颜色的部分要设置距离顶部一定的距离
     *
     * @param iBaseFragment
     */
    private void initTitleBarMarginTop(IBaseFragment iBaseFragment) {
        if (iBaseFragment.isStatusBarColor() == false) {
            getTitleBar().addColorStatusBar();
        }
    }


    public void setErrorButtonClickListener(final View.OnClickListener listener) {
        emptyLayout.setErrorButtonClickListener(listener);
    }


    public void showError() {
        if (isLoadingSuccess == false)
            emptyLayout.showError();
    }

    public void showLoading() {
        if (isLoadingSuccess == false)
            emptyLayout.showLoading();
    }


    public void showEmpty() {
        if (isLoadingSuccess == false)
            emptyLayout.showEmpty();
    }

    private void addToActivity() {
        FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mContext.addContentView(this, ll);
    }

    private void removeFromActivity() {
        this.setVisibility(GONE);
    }


    public void showNormal() {
        if (isLoadingSuccess == false) {
            isLoadingSuccess = true;
            emptyLayout.setViewGone();
            removeFromActivity();
        }
    }


    public TitleBar getTitleBar() {
        return titleBar;
    }

    public EmptyLayout getEmptyLayout() {
        return emptyLayout;
    }
}
