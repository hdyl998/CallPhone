package com.callphone.client.start;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goNextPage();
            }
        },1000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }

    Handler handler=new Handler();

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
