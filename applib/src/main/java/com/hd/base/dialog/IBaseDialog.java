package com.hd.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.hd.R;
import com.hd.base.maininterface.IBaseMethod;

/**
 * 新版的基类对话框，重构
 * Created by liugd on 2017/3/20.
 */

public abstract class IBaseDialog extends Dialog implements View.OnClickListener, IBaseMethod {

    protected Context mContext;

    public IBaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        if (setWindowAnimation() != 0) {
            getWindow().setWindowAnimations(setWindowAnimation());
        }
        setContentView(getLayoutId());
        initClickListener();
    }

    public IBaseDialog(Context context) {
        this(context, R.style.dialogThemeBase);
    }


    //确保dissmiss在主线程执行防止华为手机异常发生
    private final Handler mHandler = new Handler();
    private final Runnable mDismissAction = this::dismissDialogSuper;

    @Override
    public void dismiss() {
        if (Looper.myLooper() == mHandler.getLooper()) {
            dismissDialogSuper();
        } else {
            mHandler.post(mDismissAction);
        }
    }

    private void dismissDialogSuper() {
        super.dismiss();
    }


    @Override
    public void onClick(View v) {

    }


    /***
     * 设置VIEW
     *
     * @return
     */
    public abstract int getLayoutId();

    /***
     * 设置动画
     * public static final int dialogAnimBigIn=0x7f090164;
     * public static final int dialogAnimBottominBottomout=0x7f090165;
     * public static final int dialogAnimFadeinFadeout=0x7f090166;
     * public static final int dialogAnimLeftInRightOut=0x7f090167;
     * public static final int dialogAnimUpInBottomOut=0x7f090168;
     * @return
     */
    protected int setWindowAnimation() {
        return 0;
    }


    public int[] setClickIDs() {
        return null;
    }

    public final <T extends View> T findViewByID(@IdRes int id) {
        return (T) super.findViewById(id);
    }

    /**
     * 初始化点击事件
     */
    public final void initClickListener() {
        int ids[] = setClickIDs();// 设置点击ID
        if (ids != null && ids.length > 0) {
            for (int id : ids) {
                findViewById(id).setOnClickListener(this);
            }
        }
    }

    //对话框全屏
    protected void setFullScreen() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
    }

    /***
     * 显示在底部
     */
    protected void setShowAtBottom() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
        getWindow().setGravity(Gravity.BOTTOM);//底部展示
    }
}
