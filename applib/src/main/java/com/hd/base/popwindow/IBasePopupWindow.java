package com.hd.base.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.hd.R;
import com.hd.base.interfaceImpl.AnimationListenerImpl;
import com.hd.base.maininterface.IBaseMethod;
import com.hd.utils.ViewUtils;

/**
 * Created by liugd on 2017/3/20.
 */

public abstract class IBasePopupWindow extends PopupWindow implements IBaseMethod, View.OnClickListener {

    protected Activity mContext;

    public IBasePopupWindow(Context mContext) {
        super(mContext);
        this.mContext = (Activity) mContext;
        if (getLayoutId() != 0) {
            this.setContentView(ViewUtils.getInflateView(mContext, getLayoutId()));
        }
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        initClickListener();
    }

    View darkBgView;

    private void addBgToActivity() {
        //查找是否添加过这个背景VIEW了,只添加一次
        View view = mContext.findViewById(R.id.tagFirst);
        if (view instanceof FrameLayout) {
            view.setVisibility(View.VISIBLE);
            darkBgView = view;
            return;
        }
        FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        FrameLayout layout = new FrameLayout(mContext);
        layout.setId(R.id.tagFirst);
        layout.setBackgroundColor(0x77000000);
        darkBgView = layout;
        mContext.addContentView(layout, ll);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        alphaIn();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        alphaIn();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        alphaIn();
    }

    private void alphaIn() {
        if (darkBgView == null) {
            addBgToActivity();
        }
        darkBgView.setVisibility(View.VISIBLE);
        AlphaAnimation ain = new AlphaAnimation(0f, 1f);
        ain.setDuration(300);
        darkBgView.startAnimation(ain);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        darkBgView.clearAnimation();
        AlphaAnimation ain = new AlphaAnimation(1f, 0f);
        ain.setDuration(300);
        darkBgView.startAnimation(ain);
        ain.setAnimationListener(new AnimationListenerImpl() {
            @Override
            public void onAnimationEnd(Animation animation) {
                darkBgView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int[] setClickIDs() {
        return null;
    }

    @Override
    public void initClickListener() {
        int ids[] = setClickIDs();// 设置点击ID
        if (ids != null) {
            for (int id : ids) {
                getContentView().findViewById(id).setOnClickListener(this);
            }
        }
    }

    @Override
    public <T extends View> T findViewByID(@IdRes int id) {
        return (T) getContentView().findViewById(id);
    }
}
