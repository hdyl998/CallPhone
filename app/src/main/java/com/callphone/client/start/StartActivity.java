package com.callphone.client.start;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.callphone.client.R;
import com.callphone.client.base.AppConstants;
import com.callphone.client.main.MainNewActivity;
import com.hd.base.IBaseActivity;
import com.hd.base.launch.AppLauncherUtils;
import com.hd.permission.PermissionCallback;
import com.hd.permission.PermissionHelper;
import com.hd.utils.GoUtils;
import com.hd.utils.toast.ToastUtils;

import java.util.List;

/**
 * Note：None
 * Created by Liuguodong on 2019/1/3 16:12
 * E-Mail Address：986850427@qq.com
 */
public class StartActivity extends IBaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        PermissionHelper.create(mContext).setPermissions(AppConstants.permissionStart)
                .request(new PermissionCallback() {
                    @Override
                    public void onPermissionGranted() {
                        goNextPage();
                    }

                    @Override
                    public void onPermissionDenied(List<String> permissions) {
                        new AlertDialog.Builder(mContext)
                                .setTitle("缺少必要权限")
                                .setMessage(String.format("请点击\"设置\"-\"权限\"-打开以下权限:\n%s", listPermissions2String(permissions)))
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtils.show("没有授权相关权限,应用退出!");
                                        mContext.finish();
                                    }
                                })
                                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        GoUtils.goAppDetailsSetting(mContext);
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                });
    }

    private static final String TAG = "StartActivity";

    /***
     * 进入时检查SUID为空进行初始化SUID的操作，确保每台手机都有SUID
     */


    private void goNextPage() {
        AppLauncherUtils.startActivity(mContext, MainNewActivity.class);
        mContext.finish();
    }

    public static void launch(Context context) {
        context.startActivity(new Intent(context, StartActivity.class));
    }


}
