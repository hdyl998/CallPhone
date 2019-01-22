package com.hd.utils.loopdo;

import android.os.Handler;
import android.os.Message;

import com.hd.base.maininterface.IComCallBacks;

/**
 * 一个用handler的循环的工具，一般用于计时
 * Created by Administrator on 2018/3/20.
 */

public class HanderLoopHelper {

    private int loopTimeMillis = 1000;//循环时间1秒

    private boolean isStarted = false;//是否启动过

    public void setLoopTimeMillis(int loopTimeMillis) {
        this.loopTimeMillis = loopTimeMillis;
    }

    boolean isRunning=false;

    MyThread myThread;

    class MyThread extends Thread{

        boolean isRun=true;

        public void setRun(boolean run) {
            isRun = run;
        }

        public void stopWork(){
            setRun(false);
            this.interrupt();;
        }

        @Override
        public void run() {
            while (isRun){
                try {
                    Thread.sleep(loopTimeMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                try {
                    if (loopCallBacks != null) {
                        loopCallBacks.call(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }




    public IComCallBacks loopCallBacks;

    public void setLoopCallBacks(IComCallBacks comCallBacks) {
        this.loopCallBacks = comCallBacks;
    }

    /***
     * 开始循环
     */
    public void startLoop() {
        isStarted = true;
        stopLoop();
        myThread=new MyThread();
        myThread.start();
    }


    /***
     * 停止循环
     */
    public void stopLoop() {
        if(myThread!=null){
            myThread.stopWork();
        }
    }

    /**
     * 生命周期onPause
     */
    public void onPause() {
        //只有手动调用过才停止
        if (isStarted) {
            stopLoop();
        }
    }




    public void setStarted(boolean started) {
        isStarted = started;
    }

    /***
     * 生命周期onDestory
     */
    public void onDestory() {
        setLoopCallBacks(null);
    }
}
