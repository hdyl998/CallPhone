package com.hd.utils.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.hd.base.HdApp;
import com.hd.utils.log.impl.LogUitls;

import me.drakeet.support.toast.ToastCompat;

/**
 * @类名:ToastUtils
 * @功能描述: Toast工具类
 * @作者:XuanKe'Huang
 * @时间:2015-3-30 下午5:13:57
 * @Copyright @2015
 */
@SuppressLint("InflateParams")
public class ToastUtils {

    private static Toast toast;
//    private static int lastResid;

    private static final String TAG = "ToastUtils";

    public static void show(String string) {
        if (TextUtils.isEmpty(string)) {//空TOAST 或者 在后台时不弹出提示
            LogUitls.print(TAG, string);
            return;
        }
        if (toast == null) {
            toast = ToastCompat.makeText(HdApp.getContext(), "", Toast.LENGTH_SHORT);
        }
        toast.setText(string);
        toast.show();
    }


//    public static void showSnackMessage(Context mContext, String message) {
//        if (mContext instanceof Activity && !((Activity) mContext).isFinishing()) {
//            //添加到contentView上，而不是添加到decorView上，防止有虚拟按键的手机键盘遮挡的问题
//            View view = ((Activity) mContext).getWindow().getDecorView().findViewById(android.R.id.content);
//            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
//        }
//    }


    public static void makeTextAndShow(String msg){
        show(msg);
    }

    public static void makeTextAndShow(int msg){
        show(msg);
    }

    /**
     * show content for res id.
     *
     * @param id res id.
     */
    public static void show(@StringRes int id) {
        Context context = HdApp.getContext();
        show(context.getResources().getString(id));
    }
//
//    public static void showNoifatication(String tag, String title, String message) {
//        NotificationManager manager = (NotificationManager) HdApp.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        //API level 11
//        Notification.Builder builder = new Notification.Builder(HdApp.getContext());
//        builder.setContentTitle(title);
//        builder.setContentText(message);
//        builder.setSmallIcon(R.drawable.ic_launcher);
//        Notification notification = builder.getNotification();
//        manager.notify(tag, R.drawable.ic_launcher, notification);
//    }
}
