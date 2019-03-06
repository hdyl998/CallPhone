package com.callphone.client.start;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.format.Formatter;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.callphone.client.R;
import com.callphone.client.base.App;
import com.callphone.client.base.AppConstants;
import com.callphone.client.base.data.AppSaveData;
import com.callphone.client.main.MainNewActivity;
import com.hd.base.IBaseActivity;
import com.hd.base.launch.AppLauncherUtils;
import com.hd.net.NetBuilder;
import com.hd.net.NetCallbackImpl;
import com.hd.net.OkHttpHelper;
import com.hd.net.download.DownListener;
import com.hd.net.download.DownUtil;
import com.hd.net.socket.NetEntity;
import com.hd.permission.PermissionCallback;
import com.hd.permission.PermissionHelper;
import com.hd.utils.GoUtils;
import com.hd.utils.Utils;
import com.hd.utils.ViewUtils;
import com.hd.utils.toast.ToastUtils;
import com.hd.utils.update.UpdateUtils;

import java.io.File;
import java.util.List;

/**
 * Note：None
 * Created by lgd on 2019/1/3 16:12
 * E-Mail Address：986850427@qq.com
 */
public class StartActivity extends IBaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_start;
    }

    TextView textView;

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        textView = findViewByID(R.id.text);
        NetBuilder.create(mContext).start("getVersion", new NetCallbackImpl<UpdateInfoItem>() {
            @Override
            public void onSuccess(NetEntity<UpdateInfoItem> entity) throws Exception {
                boolean isNeedUpdate = UpdateUtils.isNeedUpdate(Utils.getVersionName(), entity.getDataBean().version);
                if (isNeedUpdate) {
                    textView.setText("发现新版本 " + entity.getDataBean().version);
                    goUpdate(entity.getDataBean());
                } else {
                    textView.setText("更新检查完毕！");
                    go();
                }
            }

            @Override
            public void onError(NetEntity entity) throws Exception {
                textView.setText("更新检查错误！");
                go();
            }
        });
    }

    /***
     * 弹出更新对话框
     * @param infoItem
     */
    private void goUpdate(UpdateInfoItem infoItem) {

        new AlertDialog.Builder(mContext).setTitle(getString(R.string.app_name) + " 发现新版本 " + infoItem.version)
                .setMessage(infoItem.remark)
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mContext.finish();
                    }
                }).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadApk(infoItem);
            }
        }).setCancelable(false).show();
    }

    DownUtil downUtil;

    private void downloadApk(UpdateInfoItem infoItem) {
        View view = ViewUtils.getInflateView(mContext, R.layout.dialog_update_progress);
        View viewConfirm = view.findViewById(R.id.btnConfirm);
        viewConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUtils.installApk(mContext, new File(v.getTag().toString()));
            }
        });
        View viewCancel = view.findViewById(R.id.btnCancel);
        viewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.finish();
            }
        });

        View llMenu = view.findViewById(R.id.llMenu);
        llMenu.setVisibility(View.GONE);


        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView textView = view.findViewById(R.id.textView);
        AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("正在下载")
                .setView(view)
                .setCancelable(false)
                .setView(view).show();
        if (downUtil != null)
            downUtil.cacleDown();
        downUtil = new DownUtil();
        downUtil.setDownUrl(infoItem.url);
        downUtil.setDownPath(UpdateUtils.getAppCachePath());
        downUtil.setDownName("update.apk");
        downUtil.downFile(new DownListener() {
            @Override
            public void downStart() {
                progressBar.setProgress(0);
                textView.setText("连接中...");
            }

            @Override
            public void downProgress(float progress, long speed, long sum, long target) {
                progressBar.setProgress((int) progress);
                textView.setText(Formatter.formatFileSize(mContext, sum) + "/" + Formatter.formatFileSize(mContext, target));
            }

            @Override
            public void downSuccess(String downPath) {
                ToastUtils.show("下载成功!");
                dialog.setTitle("下载完成，点击立即安装");
                UpdateUtils.installApk(mContext, new File(downPath));
                viewConfirm.setTag(downPath);

                llMenu.setVisibility(View.VISIBLE);
            }

            @Override
            public void downFailed(String failedDesc) {
                ToastUtils.show("下载失败! " + failedDesc);
            }
        });
    }


    private void go() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goNextPage();
            }
        }, 1000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);

        if (downUtil != null)
            downUtil.cacleDown();
    }

    Handler handler = new Handler();

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
