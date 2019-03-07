package com.hd.net.download;

/**
 * <p>Created by Administrator on 2018/10/10.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

import android.os.Handler;


import com.hd.utils.log.impl.LogUitls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/***
 * 下载工具
 */

public class DownUtil implements Callback {


    Handler handler = new Handler();

    OkHttpClient mOkHttpClient;
    DownListener downListener;
    String downPath;
    String downName;
    String downUrl;


    public DownUtil setDownUrl(String downUrl) {
        this.downUrl = downUrl;
        return this;
    }

    public DownUtil setDownPath(String downPath) {
        this.downPath = downPath;
        return this;
    }

    public DownUtil setDownName(String downName) {
        this.downName = downName;
        return this;
    }

    public DownUtil() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }


    public void downFile(DownListener downListener) {
        this.downListener = downListener;
        try {
            if (downListener != null)
                downListener.downStart();
            LogUitls.print("down", "下载地址==" + downPath + "   下载的名字==" + downName);
            File destDir = new File(downPath);

            LogUitls.print("isdirectory", destDir.isDirectory());
            if (!destDir.exists()) {
                destDir.mkdirs();
                LogUitls.print("isdirectory", "mkdirs");
            }
            Request request = new Request.Builder().url(downUrl).build();
            mOkHttpClient.newCall(request).enqueue(this);
        } catch (Exception e) {
            if (downListener != null) {
                downListener.downFailed(e.toString());
            }
            LogUitls.print("down", "=================error==" + e.toString());
        }
    }

    public void cacleDown() {
        Dispatcher dispatcher = mOkHttpClient.dispatcher();
//        for (Call call : dispatcher.queuedCalls()) {
//            call.cancel();
//        }
        for (Call call : dispatcher.runningCalls()) {
            call.cancel();
        }
        handler.removeCallbacksAndMessages(null);
        downListener = null;
    }


    @Override
    public void onFailure(Call call, IOException e) {
        onDownFailed(e.toString());
        LogUitls.print("down", "onFailure");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        lastProgress = 0f;
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            long total = response.body().contentLength();
            File file = new File(downPath, downName);
            if (file.exists()) {
                LogUitls.print("down", "文件存在，删除文件==");
                file.delete();
            }
            if (!file.exists() && file.isFile()) {
                LogUitls.print("down", "下载文件不存在创建文件==");
                boolean isCreat = file.createNewFile();
                LogUitls.print("down", "创建文件==" + isCreat);
            }
            fos = new FileOutputStream(file);
            long sum = 0;
            long systemCurrentMi = System.currentTimeMillis();
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                if (System.currentTimeMillis() - systemCurrentMi >= 200) {
                    systemCurrentMi = System.currentTimeMillis();
                    final float progress = (sum * 1.0f / total * 100);
                    updateProgress(progress, sum, total);
                }
            }
            updateProgress(100, sum, total);
            fos.flush();
            if (downListener != null)
                handler.post(() -> downListener.downSuccess(file.getPath()));
            LogUitls.print("down", "=================success==");
        } catch (Exception e) {
            onDownFailed(e.toString());
            LogUitls.print("down", "=================error==" + e.toString());
        } finally {
            if (is != null)
                is.close();
            if (fos != null)
                fos.close();
        }
    }

    private void onDownFailed(String msg) {
        if (downListener != null)
            handler.post(() -> downListener.downFailed(msg));
    }

    float lastProgress = 0;

    private void updateProgress(float progress, long sum, long total) {
        if (progress > lastProgress) {
            if (downListener != null) {
                handler.post(() -> downListener.downProgress(progress, sum, total));
            }
        }
        lastProgress = progress;
    }


}