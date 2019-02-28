package com.hd.permission;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.hd.utils.GoUtils;
import com.hd.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限回调,默认实现,如果拒绝,则提示去打开
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public abstract class PermissionCallback {

    Context mContext;


    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public abstract void onPermissionGranted(); //全都授权

    /***
     * 某个拒绝
     * @param permissions
     */
    public void onPermissionDenied(List<String> permissions) {

        new AlertDialog.Builder(mContext)
                .setTitle("缺少必要权限")
                .setMessage(String.format("请点击\"设置\"-\"权限\"-打开以下权限:\n%s", listPermissions2String(permissions)))
                .setNegativeButton("取消", null)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GoUtils.goAppDetailsSetting(mContext);
                    }
                })
                .setCancelable(false)
                .show();
//        SimpleDialog.create(mContext).setTvTitle("缺少必要权限")
//                .setTvContentGravity(Gravity.LEFT)
//                .setTvContent(String.format("请点击\"设置\"-\"权限\"-打开以下权限:\n%s", listPermissions2String(permissions)))
//                .setBtnLeft("取消")
//                .setBtnRight("设置")
//                .setCancelableDialog(false)
//                .setBtnRightTextColor(ColorConsts.COLOR_THEME)
//                .setOnClickListener(new SimpleDialog.SimpleDialogClick() {
//                    @Override
//                    public void onRightClick(SimpleDialog simpleDialog) {
//                        GoUtils.goAppDetailsSetting(mContext);
//                    }
//                });
    }

    /***
     * 把一组权限转换成对应的文本
     * @param permissions
     * @return
     */
    protected static String listPermissions2String(List<String> permissions) {
        List<String> permissionNames = new ArrayList<>(permissions.size());
        for (String permission : permissions) {
            //去重
            String curPer = permission2String(permission);
            if (!permissionNames.contains(curPer)) {
                permissionNames.add(curPer);
            }
        }
        return Utils.listString2String(permissionNames, "\n");
    }

    /***
     * 把权限转成对应的文本
     * @param permissionString
     * @return
     */
    private static String permission2String(String permissionString) {
        String transString = "";
        switch (permissionString) {
            case Manifest.permission.READ_PHONE_STATE:
                transString = "获取手机信息(或电话)";
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                transString = "读写手机存储";
                break;
            case Manifest.permission.RECORD_AUDIO:
                transString = "录音";
                break;
            case Manifest.permission.CAMERA:
                transString = "相机";
                break;
            case Manifest.permission.ACCESS_COARSE_LOCATION:
            case Manifest.permission.ACCESS_FINE_LOCATION:
                transString = "定位";
                break;
            case Manifest.permission.CALL_PHONE:
                transString="拨打电话";
                break;
        }
        return transString;
    }

}
