package com.hd.base.dialog;

import android.content.Context;

import com.hd.R;

/**
 * 底部弹出的dialog
 * Created by liugd on 2017/3/21.
 */

public abstract class IBaseBottomDialog extends IBaseDialog {
    public IBaseBottomDialog(Context context) {
        super(context);
        setShowAtBottom();
    }

    public IBaseBottomDialog(Context context, int layoutId) {
        super(context, layoutId);
        setShowAtBottom();
    }

    @Override
    protected int setWindowAnimation() {
        return R.style.dialogAnimBottominBottomout;
    }
}
