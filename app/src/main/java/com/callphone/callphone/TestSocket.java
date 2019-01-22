package com.callphone.callphone;

import android.os.Handler;

import com.hd.base.maininterface.IComCallBacks;
import com.hd.utils.log.impl.LogUitls;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by liugd on 2019/1/20.
 */

public class TestSocket extends Thread {

    Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public TestSocket() {
    }

    Handler handler= new Handler();
    boolean isRun = true;

    private static final String TAG = "TestSocket";

    @Override
    public void run() {

        try {
            // 要连接的服务端IP地址和端口
            String host = "192.168.1.5";
            int port = 8877;
            // 与服务端建立连接
            socket = new Socket(host, port);
            // 建立连接后获得输出流
            outputStream = socket.getOutputStream();
//            String message = "你好  yiwangzhibujian";
//            socket.getOutputStream().write(message.getBytes("UTF-8"));
            //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
//            socket.shutdownOutput();

            inputStream = socket.getInputStream();

            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                String str = new String(bytes, 0, len, "UTF-8");
                handler.post(()->{
                    if(comCallBacks!=null){
                        comCallBacks.call(str);
                    }
                });
                LogUitls.print(TAG, str);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    IComCallBacks<String>comCallBacks;

    public void setComCallBacks(IComCallBacks<String> comCallBacks) {
        this.comCallBacks = comCallBacks;
    }

    public void close() {
        try {
            if(inputStream!=null)
            inputStream.close();
            if(outputStream!=null)
            outputStream.close();
            if(socket!=null)
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
