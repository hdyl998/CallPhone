package com.hd.net.download;

/**
 * <p>Created by Administrator on 2018/10/10.<p>
 * <p>佛祖保佑，永无BUG<p>
 */
public interface DownListener {
    /**
     * 开始下载
     */
    void downStart();

    /***
     * 下载进度，和速度
     * @param progress
     * @param speed
     */
    void downProgress(float progress, long speed);

    /***
     * 下载完成
     * @param downPath
     */
    void downSuccess(String downPath);

    /***
     * 下载失败
     * @param failedDesc
     */
    void downFailed(String failedDesc);

}
