package com.hd.utils.other;

import com.hd.utils.log.impl.LogUitls;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>Created by Administrator on 2018/12/10.<p>
 * <p>佛祖保佑，永无BUG<p>
 */
public class ThreadPoolManager {
    //最佳方案是processors+1  这里取一半是因为想让其不过份占用cpu,使其画面预览流畅
    private final int CORE_PROCESS=(Runtime.getRuntime().availableProcessors() + 1) / 2;

    //最小取4,最大值和cpu核心值有关
    private final int CORE_POOL_SIZE =Math.max(CORE_PROCESS,4);//核心线程数
    private final int MAX_POOL_SIZE = CORE_POOL_SIZE*2;//最大线程数
    private final long KEEP_ALIVE_TIME = 3;//空闲线程超时时间


    private ThreadPoolExecutor poolExecutor;

    private LinkedBlockingQueue<Runnable> runnables = new LinkedBlockingQueue<>();

    private static final String TAG = "ThreadPoolManager";

    public ThreadPoolManager() {
        LogUitls.print(TAG, CORE_POOL_SIZE + " tag");
        poolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, runnables);
    }


    //判断正在执行的线程
    public boolean isPoolFull() {
        return poolExecutor.getActiveCount() >= poolExecutor.getCorePoolSize();
    }


    public void execute(Runnable runnable) {
        try {
            poolExecutor.execute(runnable);
        } catch (Exception e) {
            e.printStackTrace();
            LogUitls.print(TAG, e + "");
        }
    }


    /***
     * 销毁时调用
     */
    public void onDestory() {
        //阻止新来的任务提交，同时会中断当前正在运行的线程，即workers中的线程。另外它还将workQueue中的任务给移除，并将这些任务添加到列表中进行返回。
        poolExecutor.shutdownNow();
    }

}
