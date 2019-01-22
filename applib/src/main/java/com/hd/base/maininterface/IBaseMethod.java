package com.hd.base.maininterface;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * 抽象出来，让所有的基类都实现它，可体操作体验保持一致
 * Created by liugd on 2017/3/21.
 */

public interface IBaseMethod {

    @LayoutRes
    int getLayoutId();


    int[] setClickIDs();

    void initClickListener();

    <T extends View> T findViewByID(@IdRes int id);
}
