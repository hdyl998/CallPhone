package com.hd.utils;

import android.os.Environment;

import com.hd.permission.PermissionConstants;
import com.hd.permission.PermissionHelper;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * 文件工具类
 * <p>Created by Administrator on 2018/10/11.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class FileUtils {
    /**
     * 方法名: getSDCardPath
     * <p/>
     * 功能描述:得到SD卡路径
     *
     * @return 类型String:SD卡路径,null:不存在SD卡
     * <p/>
     * </br>throws
     */
    public static String getSDCardPath() {
        if (isExistSDCard()) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return null;
    }

    /***
     * 在指定路径创建文件夹
     * @param filePath
     * @return
     */
    public static File createDirectoryFile(String filePath) {
        File destDir = new File(filePath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir;
    }

    /****
     * 在指定目录上创建指定文件,如果文件存在,则删除该文件
     * @param filePath
     * @param fileName
     * @return
     */
    public static File createNewFile(String filePath, String fileName) {
        File file = new File(filePath, fileName);
        if (!file.exists()) {// 文件不存在，则建立文件夹
            File dirFile = file.getParentFile();
            if (dirFile != null)
                dirFile.mkdirs();
        }
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    /**
     * 写SD卡文件
     *
     * @param fileName
     * @param content
     */
    public static void writeFile(String filePath, String fileName, String content) {
        if (!PermissionHelper.hasPermissions(PermissionConstants.SDCardPermissions) || !isExistSDCard())
            return;
        try {
            File file = FileUtils.createNewFile(filePath, fileName);
            FileOutputStream out = new FileOutputStream(file, false); // 如果追加方式用true
            out.write(content.getBytes("utf-8"));// 注意需要转换对应的字符集
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /***
     * 移除文件或者文件夹
     * @param
     */

    public static boolean deleteFile(File dirFile) {
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }

        return dirFile.delete();
    }

    public static boolean deleteFile(String path) {
        return deleteFile(new File(path));
    }

    /**
     * 方法名: hasSdcard
     * <p/>
     * 功能描述:检查是否存在SDCard
     *
     * @return 类型boolean:true:存在,false:不存在
     * <p/>
     * </br>throws
     */
    public static boolean isExistSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 读SD卡文件
     */
    public static String readFile(String filePath, String fileName) {
        if (!PermissionHelper.hasPermissions(PermissionConstants.SDCardPermissions) || !isExistSDCard()) {
            return null;
        }
        FileUtils.createDirectoryFile(filePath);
        File file = new File(filePath + File.separator + fileName);
        if (!file.exists())
            return null;
        BufferedReader reader = null;
        StringBuffer sbBuffer = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                sbBuffer.append(tempString);
            }
        } catch (IOException e) {
        } finally {
            QuietlyClose(reader);
        }
        return sbBuffer.substring(0);
    }


    /***
     * 关闭文件的工具，这里的层级太复杂了，所以抽成一个工具
     *
     * @param cloneable
     */
    public final static void QuietlyClose(Closeable cloneable) {
        if (cloneable != null) {
            try {
                cloneable.close();
            } catch (IOException e1) {
            }
        }
    }
}
