package com.callphone.client.base;

import android.Manifest;

/**
 * Note：None
 * Created by Liuguodong on 2019/1/7 13:59
 * E-Mail Address：986850427@qq.com
 */
public interface Constants {
    String[] gpsPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    String[] SDCardPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String[] cameraPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    String[] audioPermissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] videoPermissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    String[] phonestatePermissions = {
            Manifest.permission.READ_PHONE_STATE};

    String[] defaultRequestPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    String C_TYPE = "2";// 请求来源:2表示安卓端
    String C_CPID = "2";// 请求站点id:2表示安卓端
    String C_KEY = "ake5%2*&$8k)dfek!r";// 密钥尾号



    /***
     * @变量名 IFCREATE_EMOJI_FILENAME: 是否建立了表情数据库文件名
     */
    String IFCREATE_EMOJI_FILENAME = "ifcreate_emoji_filename";
    /***
     * @变量名 IFCREATE_EMOJI_KEY: 是否建立了表情数据库KEY
     */
    String IFCREATE_EMOJI_KEY = "ifcreate_emoji_key";

    // 单次最多发送图片数
    int MAX_IMAGE_SIZE = 9;


    String ImgURL_180 = "_180.jpg";// 小图后缀
    String ImgURL_580 = "_580.jpg";// 中图后缀
    String ImgURL_238 = "_238.jpg";// 中图后缀

    String _30000 ="" ;//上传图片
}
