package com.hd.utils.other;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class MyRunTimeException extends RuntimeException {

    public MyRunTimeException(String message) {
        super(message);
    }

    /*
     * 重写fillInStackTrace方法会使得这个自定义的异常不会收集线程的整个异常栈信息
     * 减少异常开销。
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
