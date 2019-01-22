package com.hd.utils;


import com.hd.utils.other.CalcTime;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class RxJavaUtils {

    /***
     * 执行任务(任务线程默认在IO线程,观察线程默认在主线程)
     * @param task
     * @return
     */
    public static Subscriber doTask(RxJavaClass task) {
        return doTask(task, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    /***
     * 执行任务(任务线程默认在IO线程,观察线程默认在主线程)
     * @param task
     * @return
     */
    public static Subscriber doCPUTask(RxJavaClass task) {
        return doTask(task, Schedulers.computation(), AndroidSchedulers.mainThread());
    }

    /***
     *执行任务
     * @param task
     * @param subscribeThread 任务线程
     * @param observeThread 观察者线程
     * @return
     */
    private static Subscriber doTask(RxJavaClass task, Scheduler subscribeThread, Scheduler observeThread) {
        Observable.create(task).subscribeOn(subscribeThread)
                .observeOn(observeThread)
                .subscribe((Subscriber) task);
        return task;
    }

    public abstract static class RxJavaClass<T> extends Subscriber<T> implements Observable.OnSubscribe<T> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(T t) {
            onTaskCompleted(t);
        }

        public abstract T onDoTask() throws Exception;

        public abstract void onTaskCompleted(T t);

        @Override
        public final void call(Subscriber<? super T> subscriber) {
            T t;
            try {
                CalcTime calcTime = new CalcTime();
                t = onDoTask();
                calcTime.printResult("rxjava");
            } catch (Exception e) {
                e.printStackTrace();
                RuntimeException runtimeException = new RuntimeException("出现问题");
                runtimeException.initCause(e);
                throw runtimeException;
            }
            subscriber.onNext(t);
            subscriber.onCompleted();
            subscriber.unsubscribe();
        }
    }

    public abstract static class RxJavaClassImpl extends RxJavaClass<Void> {

    }

}
