package com.callphone.client.main.mine;

import android.content.Context;

import com.callphone.client.base.data.AppSaveData;
import com.callphone.client.login.LoginFragment;
import com.callphone.client.main.bean.EventItem;
import com.hd.base.IBaseFragment;
import com.hd.utils.toast.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 登录管理器
 * <p>Created by Administrator on 2018/10/22.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

public class LoginManager {


    public static void logout(){
        AppSaveData.getUserVInfo().setOffLine();
        EventBus.getDefault().post(new EventItem.LoginOutEvent());
    }


    public static boolean isLogin() {
        if (AppSaveData.getUserVInfo().isOffLine()) {
            return false;
        }
        return true;
    }

    /**
     * 是否登陆了并且跳转到指定的地方 没登陆则跳转到指定的地方
     *
     * @return
     */
    public static boolean isLoginAndRedict(Context context, int requestCode) {
        if (AppSaveData.getUserVInfo().isOffLine()) {
            LoginFragment.launchForRestult(context, requestCode);
            return false;
        }
        return true;
    }



    /**
     * 是否登陆了并且跳转到指定的地方 没登陆则跳转到指定的地方
     *
     * @return
     */
    public static boolean isLoginAndRedict(Context context) {
        return isLoginAndRedictWithCode(context, IBaseFragment.REQUEST_CODE_LOGIN);
    }

    /**
     * 是否登陆了并且跳转到指定的地方 没登陆则跳转到指定的地方
     * code 传的请求码
     *
     * @return
     */
    public static boolean isLoginAndRedictWithCode(Context context, int code) {
        return isLoginAndRedict(context, code);
    }


    /**
     * 登陆则返回true 没登陆则提示，返回false
     *
     * @param context
     * @return
     */
    public static boolean isLoginAndRemind(Context context) {
        if (AppSaveData.getUserVInfo().isOffLine()) {
            ToastUtils.show("未登录");
            return false;
        }
        return true;
    }
}


