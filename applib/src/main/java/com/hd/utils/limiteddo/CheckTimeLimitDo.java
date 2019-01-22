package com.hd.utils.limiteddo;

import android.os.Handler;

import com.hd.utils.log.impl.LogUitls;

/**
 * 短时间内限制执行工具，用于十分频繁的操作，如adapter.notifydataSetChanged()
 * 当设置成5秒钟，5秒钟只能操作一次，
 * Created by liugd on 2017/8/18.
 */

public class CheckTimeLimitDo<T> extends CheckTimeDo<T> {


    protected Handler mHandler = new Handler();

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public Handler getHandler() {
        return mHandler;
    }

    /***
     * 生命周期 onDestory
     */
    public void onDestory() {
        getHandler().removeCallbacksAndMessages(null);//移除回调
    }

//    /***
//     * 生命周期 onPause
//     */
//    public void onPause() {
//        getHandler().removeCallbacksAndMessages(null);//移除回调
//    }
//
//    /**
//     * 生命周期 onResume
//     */
//    public void onResume() {
//        checkDo();
//    }

    @Override
    public boolean checkDo() {
        getHandler().removeCallbacksAndMessages(null);//移除回调
        boolean isDo = super.checkDo();
        //没有执行，超时后自动执行
        if (!isDo) {
            getHandler().postDelayed(runnable, milliSeconds);
        }
        return isDo;
    }

    @Override
    public void checkDo(T t) {
        this.t = t;
        checkDo();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (comCallBacks != null) {
                comCallBacks.call(t);
                LogUitls.print(TAG, "最后一条消息超时后执行");
                recordCurTime();//记录最后执行的时间
            }
        }
    };

    private static final String TAG = "CheckTimeLimitDo";

}
