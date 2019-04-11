package com.callphone.client.about;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.callphone.client.R;
import com.callphone.client.start.UpdateInfoItem;
import com.hd.base.dialog.SimpleDialog;
import com.hd.base.fragment.IBaseTitleBarFragment;
import com.hd.net.NetBuilder;
import com.hd.net.NetCallbackImpl;
import com.hd.net.download.DownListener;
import com.hd.net.download.DownUtil;
import com.hd.net.socket.NetEntity;
import com.hd.utils.Utils;
import com.hd.utils.ViewUtils;
import com.hd.utils.toast.ToastUtils;
import com.hd.utils.update.UpdateUtils;

import java.io.File;

/**
 * Created by liugd on 2019/2/28.
 */

public class AboutFragment extends IBaseTitleBarFragment {
    @Override
    protected void initTitleBarView() {
        TextView textView = findViewByID(R.id.textView);
        textView.setText("软件版本：" + Utils.getVersionName() + "\n" + "内部代码：" + Utils.getVersionCode());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                checkUpdate();
                break;
        }
    }

    @Override
    public int[] setClickIDs() {
        return new int[]{R.id.btnUpdate};
    }

    private void checkUpdate() {
        showDialogForLoading();
        NetBuilder.create(mContext).start("getVersion", new NetCallbackImpl<UpdateInfoItem>() {
            @Override
            public void onSuccess(NetEntity<UpdateInfoItem> entity) throws Exception {
                hideDialogForLoadingImmediate();
                boolean isNeedUpdate = UpdateUtils.isNeedUpdate(Utils.getVersionName(), entity.getDataBean().version);
                if (isNeedUpdate) {
                    goUpdate(entity.getDataBean());
                } else {
                    SimpleDialog.create(mContext).showSingleInfo("已是最新版本");
                }

            }
        });
    }

    @Override
    public int setBackgroundColor() {
        return R.color._FFFFFF;
    }


    /***
     * 弹出更新对话框
     * @param infoItem
     */
    private void goUpdate(UpdateInfoItem infoItem) {

        new AlertDialog.Builder(mContext).setTitle(getString(R.string.app_name) + " 发现新版本 " + infoItem.version)
                .setMessage(infoItem.remark)
                .setNegativeButton("取消", null).setPositiveButton("立即下载", new DialogInterface.OnClickListener() {
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


        View llMenu = view.findViewById(R.id.llMenu);
        llMenu.setVisibility(View.INVISIBLE);


        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView textView = view.findViewById(R.id.textView);
        AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("正在下载")
                .setView(view)
                .setCancelable(false)
                .setView(view).show();

        viewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                dialog.dismiss();
            }
        });
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
            public void downProgress(float progress, long sum, long target) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (downUtil != null)
            downUtil.cacleDown();
    }
}
