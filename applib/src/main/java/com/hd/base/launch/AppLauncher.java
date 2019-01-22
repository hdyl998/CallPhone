package com.hd.base.launch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;

import com.hd.R;
import com.hd.base.BaseConstants;
import com.hd.base.FragmentContainerActivity;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.toast.ToastUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * app启动工具
 * Created by liugd on 2017/5/22.
 */
public class AppLauncher {
    private Context context;
    private Object objs[];//传带的参数

    private String className;
    private boolean isFragmentMode = true;//默认以碎片形式

    private int requestCode = -1;//请求码
    private int animType = -1;
    private int pagerIndex = -1;//用于ViewPager指示页初始化


    private AppLauncher(Context context) {
        this.context = context;
    }

    /**
     * 以Activity方式启动
     *
     * @return
     */
    public AppLauncher asActivityMode() {
        isFragmentMode = false;
        return this;
    }

    /**
     * 以Fragment方式启动
     *
     * @return
     */
    public AppLauncher asFragmentMode() {
        isFragmentMode = true;
        return this;
    }

    public AppLauncher setClassName(String className) {
        this.className = className;
        return this;
    }


    public static AppLauncher withFragment(Context context, Class<? extends Fragment> clazz) {
        return withFragment(context, clazz.getName());
    }

    public static AppLauncher withFragment(Context context, String className) {
        return new AppLauncher(context).setClassName(className).asFragmentMode();
    }

    public static AppLauncher withActivity(Context context, String className) {
        return new AppLauncher(context).setClassName(className).asActivityMode();
    }

    public static AppLauncher withActivity(Context context, Class<? extends Activity> clazz) {
        return withActivity(context, clazz.getName());
    }

    public AppLauncher setPagerIndex(int pagerIndex) {
        this.pagerIndex = pagerIndex;
        return this;
    }

    private Class getClazz(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void launch(int requestCode) {
        setRequestCode(requestCode);
        launch();
    }

    public void launch() {
        if (getClazz(className) == null) {
            String str = "Error " + className + " is not found!";
            LogUitls.print(str);
            ToastUtils.show(str);
            return;
        }
        Intent intent = AppLauncherUtils.getIntentOfParams(objs);
        if (isFragmentMode) {
            intent.putExtra(BaseConstants.ID_NEW_MAIN, className);
            intent.setClass(context, FragmentContainerActivity.class);
        } else {
            intent.setClassName(context, className);
        }
        if (pagerIndex != -1)
            intent.putExtra(ID_NEW_PAGER_INDEX, pagerIndex);
        if (animType != -1) {
            intent.putExtra(ID_NEW_FINISH_ANIM_TYPE, animType);
        }
        if (context instanceof Activity) {
            Activity activity = ((Activity) context);
            activity.startActivityForResult(intent, requestCode);
            actionLaunchAnimation(activity, animType);
        } else {
            context.startActivity(intent);
        }
    }


    private AppLauncher setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public AppLauncher setObjs(Object... objs) {
        this.objs = objs;
        return this;
    }


    public AppLauncher setAnimType(@AnimType int animType) {
        this.animType = animType;
        return this;
    }


    //以下是工具类

    /***
     * 结束动画
     *
     * @param activity
     */
    public static void actionFinishAnimation(Activity activity) {
        switch (activity.getIntent().getIntExtra(ID_NEW_FINISH_ANIM_TYPE, -1)) {
            case -1:
                break;
            case ANIMTYPE_SLIDE_BOTTOM:
                activity.overridePendingTransition(R.anim.umeng_socialize_fade_in, R.anim.umeng_socialize_slide_out_from_bottom);
                break;
        }
    }

    /**
     * 启动动画
     *
     * @param activity
     * @param launchAnimType
     */
    public static void actionLaunchAnimation(Activity activity, int launchAnimType) {
        switch (launchAnimType) {
            case -1:
                break;
            case ANIMTYPE_SLIDE_BOTTOM:
                activity.overridePendingTransition(R.anim.umeng_socialize_slide_in_from_bottom, R.anim.umeng_socialize_fade_out);
                break;
        }

    }


    /**
     * 碎片初始化时需要的页码
     *
     * @param activity
     * @return
     */
    public static int getPagerIndex(Activity activity) {
        int var = activity.getIntent().getIntExtra(ID_NEW_PAGER_INDEX, -1);
        if (var != -1) {
            //防止嵌套调用出现问题,使用之后立即销毁
            activity.getIntent().putExtra(ID_NEW_PAGER_INDEX, -1);
        }
        return var;
    }


    /***
     * KEY键，只是本文件使用
     */
    public final static String ID_NEW_PAGER_INDEX = "pager_index";
    public final static String ID_NEW_FINISH_ANIM_TYPE = "finish_anim_type";


    //定义的动画类型
    @IntDef({ANIMTYPE_SLIDE_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimType {
    }

    public final static int ANIMTYPE_SLIDE_BOTTOM = 0;

}
