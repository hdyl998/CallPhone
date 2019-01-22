package com.hd.base.interfaceImpl;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 接口实现，以后要用接口只需要重写这里的方法就可以了,需要的才去重写，不需要的就不重写，减少方法数
 * 实现起来优雅一点
 * Created by liugd on 2017/1/9.
 */

public abstract class TextWatcherImpl implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
