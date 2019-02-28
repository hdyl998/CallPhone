package com.callphone.client.base.data;

import com.hd.utils.log.impl.LogUitls;

/**
 * Note：APP全局的一些变量
 * Created by Liuguodong on 2018/12/24 17:07
 * E-Mail Address：986850427@qq.com
 */
public class AppConst {


    public long differTime = 0l;//时间之差,单位毫秒
    /***
     * 获得当前服务器时间，已校验，单位秒
     *
     * @return
     */
    public long getServerTimeMillis() {
        return System.currentTimeMillis() + differTime;
    }



    public void setDifferTime(long serverTime) {
        differTime = serverTime - System.currentTimeMillis();
        LogUitls.print("服务器-系统的时间差",differTime);
    }

   final static AppConst instace=new AppConst();

    public static AppConst getInstace() {
        return instace;
    }

    private AppConst(){

    }

    /***
     * 获得当前服务器时间，已校验，单位ms
     *
     * @return
     */
    public long getServerTimeSeconds() {
        return getServerTimeMillis() / 1000;
    }

    /***
     * 是否需要闪进app,(条件=热启动&&上次进入小于指定时间)
     * @return
     */
    public boolean isFlashIn() {
        LogUitls.print("ttt闪进", getServerTimeSeconds() - getStartTime());
        if (isStartedApp() && getServerTimeSeconds() - getStartTime() < 5 * 60) {
            return true;
        }
        return false;
    }

    public boolean isStartedApp = false;//是否启动过App，用于判定是否是热启动




    public boolean isStartedApp() {
        return isStartedApp;
    }


    /***
     * 设置启动成功并且记录启动时间
     */
    public void setStartedApp() {
        isStartedApp = true;
        this.startTime = getServerTimeSeconds();
    }

    /***
     * 启动的时间
     */
    public long startTime;


    public long getStartTime() {
        return startTime;
    }

    public boolean isrev;//用于记录30005 的isrev 字段 控制相关功能的隐藏


    public void setRev(boolean isrev) {
        this.isrev = isrev;
    }

    public void toggleRev() {
        isrev = !isrev;
    }

    public boolean isRev() {
        return isrev;
    }




    public boolean isMainActivityRunning = false;//MainActivity是否正在运行
    public boolean isOnBackGround = false;//是否在后台
    public boolean isInActivity = true;//在整个activity内的标志


    public void setMainActivityRunning(boolean mainActivityRunning) {
        isMainActivityRunning = mainActivityRunning;
    }


    public boolean isMainActivityRunning() {
        return isMainActivityRunning;
    }


    public void setOnBackGround(boolean onBackGround) {
        isOnBackGround = onBackGround;
    }


    public boolean isOnBackGround() {
        return isOnBackGround;
    }


    public void setInActivity(boolean inActivity) {
        isInActivity = inActivity;
    }


    public boolean isInActivity() {
        return isInActivity;
    }

}
