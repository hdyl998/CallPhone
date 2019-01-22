package com.hd.utils.limiteddo;

import com.hd.base.maininterface.IComCallBacks;
import com.hd.utils.log.impl.LogUitls;

/**
 * 短时间内限制执行工具，用于请求十分频繁的接口，如个人信息，小红点等，
 * 当设置成1分钟，1分钟内只能请求1次，超过的次数将被忽略
 * Created by liugd on 2017/8/18.
 */

public class CheckTimeDo<T> {
    protected long lastDoTime;

    protected String logTag = null;
    protected T t;
    protected IComCallBacks<T> comCallBacks;

    protected boolean isForceDo = false;

    protected boolean isEnable = true;

    protected long milliSeconds = 5 * 60 * 1000;

    public CheckTimeDo() {
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public CheckTimeDo setLogTag(String logTag) {
        this.logTag = logTag;
        return this;
    }

    public CheckTimeDo(int waitTimeMinute) {
        setWaitTimeMinute(waitTimeMinute);
    }


    public CheckTimeDo setWaitTimeMinute(int waitTimeMinute) {
        this.milliSeconds = waitTimeMinute * 60 * 1000;
        return this;
    }

    /***
     * 记录当前时间
     */
    public void recordCurTime() {
        lastDoTime = System.currentTimeMillis();
    }


    public CheckTimeDo setWaitMilliSeconds(long waintMillions) {
        this.milliSeconds = waintMillions;
        return this;
    }

    public CheckTimeDo setComCallBacks(IComCallBacks<T> comCallBacks) {
        this.comCallBacks = comCallBacks;
        return this;
    }

    /***
     * 强制请求的标志
     *
     * @param forceDo
     */
    public void setForceDo(boolean forceDo) {
        isForceDo = forceDo;
    }


    private static final String TAG = "CheckTimeDo";
    /***
     * 检查时间并做处理
     *
     * @return 是否刷新
     */
    public boolean checkDo() {
        if (!isEnable) {
            return false;
        }
        long sysTime = System.currentTimeMillis();
        if (logTag != null) {
            LogUitls.print(TAG + logTag, "强制刷新？" + isForceDo);
            LogUitls.print(TAG + logTag, sysTime - lastDoTime > milliSeconds ? "超时了--刷新" : "没超时");
        }
        if (isForceDo || sysTime - lastDoTime > milliSeconds) {
            lastDoTime = sysTime;
            isForceDo = false;
            if (comCallBacks != null) {
                comCallBacks.call(t);
                return true;
            }
        }
        return false;
    }


    public void checkDo(T t) {
        this.t = t;
        checkDo();
    }

    public void setT(T t) {
        this.t = t;
    }

    /***
     * 是否超时
     *
     * @return
     */
    public boolean isTimerOver() {
        long sysTime = System.currentTimeMillis();
        return sysTime - lastDoTime > milliSeconds;
    }

    /***
     * 强制刷新
     *
     * @return
     */
    public boolean foceDo() {
        setForceDo(true);
        return checkDo();
    }

}
