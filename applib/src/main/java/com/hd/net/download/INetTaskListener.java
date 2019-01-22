package com.hd.net.download;

/**
 * Created by liugd on 2019/1/7.
 */

public interface INetTaskListener {

    /**
     * 开始下载
     */
    void onStart();

    void onProgress(int progress, long speed);

    /***
     * 下载完成
     * @param downPath
     */
    void onSuccess(String downPath);

    /***
     * 下载失败
     * @param failedDesc
     */
    void downFailed(String failedDesc);
}
