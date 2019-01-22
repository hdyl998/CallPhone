package com.hd.base.dialog;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import com.hd.utils.other.KeyboardUtils;

/**
 * 话框弹出时显示键盘，消失时隐藏键盘
 * Created by liugd on 2017/3/21.
 */

public abstract class IBaseEditDialog extends IBaseDialog {

    public IBaseEditDialog(Context context) {
        super(context);
    }

    public IBaseEditDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
    /////////////////////以下逻辑块为设置对话框弹出时显示键盘，消失时隐藏键盘/////////////////////


    private EditText focusEditText;//focus想要获得焦点的对话框edit引用


    protected void setFocusEditText(EditText editText) {
        this.focusEditText = editText;
    }

    public void hideSoftKeyboard() {
        KeyboardUtils.hideSoftKeyboard((Activity) mContext, focusEditText);
    }

    public void showSoftKeyboard() {
        KeyboardUtils.showSoftKeyboard((Activity) mContext, focusEditText);
    }


    @Override
    public void show() {
        super.show();
        if (focusEditText != null && isShowWithSoftKeyboard()) {
            focusEditText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSoftKeyboard();
                }
            }, 320);
        }
    }

    /**
     * 显示即显示键盘
     *
     * @return
     */
    protected boolean isShowWithSoftKeyboard() {
        return true;
    }

    @Override
    public void dismiss() {
        if (focusEditText != null) {
            this.hideSoftKeyboard();//先隐藏后消失不会很突五
            focusEditText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    IBaseEditDialog.super.dismiss();
                }
            }, 100);
        } else {
            super.dismiss();
        }
    }
}
