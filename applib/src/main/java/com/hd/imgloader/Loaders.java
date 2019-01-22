//package com.hd.imgloader;
//
//import com.czdx.ddcp.appconfig.IAppConfigFactory;
//import com.czdx.ddcp.common.Constants;
//import com.czdx.ddcp.lib.base.IComCallBacks;
//import com.czdx.ddcp.util.log.LogUitls;
//import com.czdx.ddcp.util.ohter.StringUtil;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.URL;
//
//import pl.droidsonroids.gif.GifImageView;
//
///**
// * Created by liugd on 2017/6/12.
// */
//
//public class Loaders {
//    /***
//     * 根据比例得到高度
//     *
//     * @param width
//     * @param recdWidth
//     * @param recdHeight
//     * @return
//     */
//
//    public static int getRealHeight(int width, int recdWidth, int recdHeight) {
//        return width * recdHeight / recdWidth;
//    }
//
//    /***
//     * 获得分享的图片的链接
//     *
//     * @param mFirstImagUrl
//     * @return
//     */
//    public static String getImageUrl(String mFirstImagUrl) {
//        if (!mFirstImagUrl.startsWith("http")) {// 如果图片没有开头
//            mFirstImagUrl = IAppConfigFactory.getConfig().getImgUrl() + mFirstImagUrl;
//        }
//        if (!mFirstImagUrl.endsWith(".png") && !mFirstImagUrl.endsWith(".jpg") &&
//                !mFirstImagUrl.endsWith(".jpeg") && !mFirstImagUrl.endsWith(".gif") &&
//                !mFirstImagUrl.endsWith(".bmp")) {// 如果图片没有后缀
//            mFirstImagUrl += Constants.ImgURL_580;
//        }
//        return mFirstImagUrl;
//    }
//
//
//    public static void loadGifImagView(final GifImageView gifImageView, final String url, final IComCallBacks comCallBacks) {
////        new Thread() {
////            @Override
////            public void run() {
////                try {
////                    File file = downImg(getContext().getExternalCacheDir() + File.separator + "gif", StringUtil.stringMD5(url, null) + ".gif", url);
////                    final GifDrawable drawable = new GifDrawable(file);
////                    gifImageView.post(new Runnable() {
////                        @Override
////                        public void run() {
////                            gifImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);// 设置缩放模式
////                            gifImageView.setImageDrawable(drawable);// 把图片设置到Gif View中
////                            if (comCallBacks != null)
////                                comCallBacks.call(null);
////                        }
////                    });
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////        }.start();
//    }
//
//
//    /***
//     * 使用URL生成文件名
//     * @param url
//     * @return
//     */
//    public static String getFileNameByUrl(String url) {
//        return StringUtil.stringMD5(url);
//    }
//
//    /**
//     * 下载图片方法
//     *
//     * @param filePath 路径
//     * @param fileName 文件名
//     * @param url      地址
//     * @return 下载后的文件
//     */
//    public static File downImg(String filePath, String url, String fileName) {
//        if (url != null) url = getImageUrl(url);
//        File file = null;
//        try {
//            File fileDir = new File(filePath);
//            if (!fileDir.exists()) {
//                fileDir.mkdirs();
//            }
//            file = new File(fileDir.getPath() + File.separator + fileName);
////            String adStr = MySharepreferences.getString(getContext(), "cachefile", fileName, null);
//            if (!file.exists()) {//文件不存在下载
//                LogUitls.print("siyehua", "存在" + file.getPath() + file.getName());
//                InputStream inputStream = new URL(url).openConnection().getInputStream();
//                OutputStream outputStream = new FileOutputStream(file);
//                int i;
//                byte[] buffer = new byte[1024 * 10];
//                while ((i = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, i);
//                }
//                inputStream.close();
//                outputStream.flush();
//                outputStream.close();
////                MySharepreferences.putString(getContext(), "cachefile", fileName, StringUtil.stringMD5(url, null));
//            } else {
//                LogUitls.print("siyehua", "存在");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return file;
//    }
//
//}
