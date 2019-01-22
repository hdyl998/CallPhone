package com.hd.base.launch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hd.base.BaseConstants;
import com.hd.base.IBaseFragment;
import com.hd.utils.log.impl.LogUitls;

import java.io.Serializable;

/**
 * 启动工具类
 * Created by liugd on 2017/3/16.
 */

public class AppLauncherUtils {

    public static void startActivity(Context mContext, Class<? extends Activity> clazz) {
        AppLauncher.withActivity(mContext, clazz).launch();
    }

    public static void startActivity(Context mContext, Class<? extends Activity> clazz, Object... seObjs) {
        AppLauncher.withActivity(mContext, clazz).setObjs(seObjs).launch();
    }

    public static void startActivityForResult(Context mContext, Class<? extends Activity> clazz, int requestCode) {
        AppLauncher.withActivity(mContext, clazz).launch(requestCode);
    }

    public static void startActivityForResult(Context mContext, Class<? extends Activity> clazz, int requestCode, Object... seObjs) {
        AppLauncher.withActivity(mContext, clazz).setObjs(seObjs).launch(requestCode);
    }


    public static void startFragment(Context mContext, Class<? extends IBaseFragment> clazz, Object... seObjs) {
        AppLauncher.withFragment(mContext, clazz).setObjs(seObjs).launch();
    }

    public static void startFragment(Context mContext, Class<? extends IBaseFragment> clazz) {
        AppLauncher.withFragment(mContext, clazz).launch();
    }

    public static void startFragmentForRestult(Context mContext, Class<? extends IBaseFragment> clazz, int requestCode, Object... seObjs) {
        AppLauncher.withFragment(mContext, clazz).setObjs(seObjs).launch(requestCode);
    }


    public static void startFragmentWithPager(Context context, Class<? extends IBaseFragment> clazz, int pagerIndex, Object... seObjs) {
        AppLauncher.withFragment(context, clazz)
                .setPagerIndex(pagerIndex)
                .setObjs(seObjs)
                .launch();
    }


    /***
     * 把参数按照顺序传入intent
     *
     * @param seObjs
     * @return
     */

    public static Intent getIntentOfParams(Object... seObjs) {
        Intent intent = new Intent();
        if (seObjs != null) {
            int count = 0;
            for (Object o : seObjs) {
                if (o != null) {
                    if (!(o instanceof Serializable)) {
                        throw new RuntimeException("传递数据类型不对，必需是序列化数据" + count);
                    }
                    intent.putExtra(String.format(BaseConstants.ID_NEW_PARAM_FORMAT, count + 1), (Serializable) o);
                }
                count++;
            }
            LogUitls.print("传递的参数", seObjs);
        }
        return intent;
    }


}
