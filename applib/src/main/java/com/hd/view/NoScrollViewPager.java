package com.hd.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @类名:NoScrollViewPager.java <br>
 * @功能描述: 禁止ViewPager滑动<br>
 * @作者:XuanKe'Huang <br>
 * @时间:2015-6-29 上午11:08:53 <br>
 * @Copyright @2015 <br>
 */
public class NoScrollViewPager extends ViewPager {
    private boolean isScrollAble = true;


    public void setScrollAble(boolean scrollAble) {
        isScrollAble = scrollAble;
    }

    public void setScrollAbleFalse() {
        setScrollAble(false);
        this.setPageTransformer(true, null);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isScrollAble)
            return super.onTouchEvent(arg0);
        return false;
    }


    @Override
    public void setCurrentItem(int item) {
        if (isScrollAble) {
            super.setCurrentItem(item);
        } else {
            super.setCurrentItem(item, false);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isScrollAble)
            return super.onInterceptTouchEvent(arg0);
        return false;

    }


}