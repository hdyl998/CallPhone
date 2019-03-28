package com.hd.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.hd.R;
import com.hd.appconfig.IAppConfigFactory;
import com.hd.base.anno.NameAnno;
import com.hd.base.datacount.EventInjectHelper;
import com.hd.base.dialog.ILoadingDialog;
import com.hd.base.dialog.UiHelperDialog;
import com.hd.base.maininterface.IBaseMethod;
import com.hd.base.maininterface.IErrorRequest;
import com.hd.base.theme.ITheme;
import com.hd.base.theme.ThemeUtils;
import com.hd.net.HttpFactory;
import com.hd.utils.Utils;
import com.hd.utils.nightmode.NightModeHelper;
import com.hd.utils.nightmode.NightModeManager;

import org.greenrobot.eventbus.EventBus;


/**
 * 通用基类的构建
 * Created by liugd on 2016/12/5.
 */
public abstract class IBaseActivity extends FragmentActivity implements ILoadingDialog, View.OnClickListener, IErrorRequest, BaseConstants, IBaseMethod, ITheme {
    protected FragmentActivity mContext;
    /***
     * 是否取得数据的标志
     */
    protected boolean ifGetData = false;

    /***
     * 夜间模式助手
     */
    private NightModeHelper nightModeHelper;


    //加载对话框
    private UiHelperDialog uiHelper;

    protected void onBeforeOnCreate(){

    }

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //销毁恢复时,重启自已对现场数据不必恢复,因为处理起来实在太麻烦且很多BUG
        if (savedInstanceState != null) {
            Utils.restartActivitySelf(mContext);
            return;
        }
        onBeforeOnCreate();
        if (!(this instanceof FragmentContainerActivity))
            ThemeUtils.initThemeStyle(this, this);
        int layoutID = getLayoutId();
        if (layoutID != 0) {
            setContentView(layoutID);
        }
        if (!IAppConfigFactory.getConfig().isDebug()) {
            try {
                initAllDatas();
            } catch (Exception e) {
                e.printStackTrace();
                EventInjectHelper.getInject().reportError(e);
            }
        } else {
            initAllDatas();
        }
    }


    private void initAllDatas() {
        initView();
        initClickListener();
        if (isEventBus()) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    protected void onStart() {
        try {
            super.onStart();
        } catch (Exception e) {
            //恢复ID时可能会出错 onRestoreInstanceState
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //JPush中调 HMS SDK解决错误的接 传 的requestCode为10001,开发者调 是请注意不要同样使 10001
    }

    /***
     * 设置contentView ID
     *
     * @return
     */

    public abstract int getLayoutId();


    /***
     * 初始化View让子类去实现
     */
    protected abstract void initView();




    @Override
    public void onClick(View v) {

    }

    /***
     * 背景颜色，默认是灰色
     *
     * @return
     */
    public int setBackgroundColor() {
        return R.color.defaultBgColor;
    }


    @Override
    protected void onResume() {
        super.onResume();
//        LogUitls.print(getClass().getSimpleName(), "onResume");

        //dialogActivity指的是付钱的那个。。
        if (isDialogActivity() == false) {
            setNightMode(NightModeManager.getInstance().isNightMode());
        }
        EventInjectHelper.getInject().onResume(this,getName());
    }

    /***
     * 获得名字用于统计
     *
     * @return
     */
    private String getName() {
        String str = null;
        if (this instanceof FragmentContainerActivity) {
            str = getTitleName();//FragmentHolderActivity会去取子碎片的注解统计等
        } else {//activity会去取自已的注解
            NameAnno nameAnno = this.getClass().getAnnotation(NameAnno.class);
            if (nameAnno != null) {
                str = nameAnno.name();
//                LogUitls.print("取到了注解的名字" + str);
            }
            if (TextUtils.isEmpty(str)) {
                str = this.getClass().getSimpleName();
            }
        }
        return str;
    }


    @Override
    protected void onPause() {
        super.onPause();
        EventInjectHelper.getInject().onPause(this,getName());
    }

    @Override
    public void startGetMyData() {
        ifGetData = true;
    }

    /**
     * 初始化点击事件
     */
    public final void initClickListener() {
        int ids[] = setClickIDs();// 设置点击ID
        if (ids != null && ids.length > 0) {
            for (int id : ids) {
                (findViewById(id)).setOnClickListener(this);
            }
        }
    }

    //设置点击事件
    public int[] setClickIDs() {
        return null;
    }


    /***
     * 是否是浅色的状态栏
     * @return
     */
    public boolean isStatusBarLightStyle() {
        return true;
    }

    /***
     * 是否有颜色状态栏
     * 是否是颜色式状态栏(API 21及以上有效)默认是有的，当需要全屏顶部沉浸显示图片时重写此方法，把变量设置成false
     *
     * @return
     */
    @Override
    public boolean isStatusBarColor() {
        return true;
    }

    /**
     * 范型方法查找ID免去强制转型
     *
     * @param id
     * @param <T>
     * @return
     */
    public final <T extends View> T findViewByID(@IdRes int id) {
        return (T) findViewById(id);
    }

//    /***
//     * 此方法不建议使用，因为书写没findViewByID方便，效果其实是一样的
//     *
//     * @param id
//     * @return
//     */
//    @Deprecated
//    @Override
//    public View findViewById(int id) {
//        return super.findViewById(id);
//    }

    /***
     * 是否是dialogActivity的标志解决对话框状态的activity状态的问题
     *
     * @return
     */
    protected boolean isDialogActivity() {
        return false;
    }


    /***
     * 此方法弃用采用注解方式设置统计信息 @NameAnno
     *
     * @return
     */
    @Deprecated
    protected String getTitleName() {
        return null;
    }


    /**
     * 调整亮度(夜间模式)
     *
     * @param isNight 是否是夜间模式
     */
    public final void setNightMode(boolean isNight) {
        if (nightModeHelper == null) {
            nightModeHelper = new NightModeHelper(mContext, getWindow());
        }
        nightModeHelper.setNightMode(isNight);
    }

    /***
     * 是否使用eventBus
     *
     * @return
     */
    protected boolean isEventBus() {
        return false;
    }

    /***
     * 显示对话框(带自定义文字)
     *
     * @param msg 提示语
     */
    public void showDialogForLoading(String msg) {
        if (uiHelper == null) {
            uiHelper = new UiHelperDialog(mContext);
        }
        uiHelper.showDialogForLoading(msg);
    }

    /***
     * 显示对话框
     *
     * @param
     */
    public void showDialogForLoading() {
        if (uiHelper == null) {
            uiHelper = new UiHelperDialog(mContext);
        }
        uiHelper.showDialogForLoading();
    }

    /***
     * 隐藏对话框
     */
    public void hideDialogForLoading() {
        if (uiHelper != null) {
            uiHelper.hideDialog();
        }
    }

    public void hideDialogForLoadingImmediate() {
        if (uiHelper != null) {
            uiHelper.hideDialogImmediate();
        }
    }

    /***
     * 获得上一个页面传来的值
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getSerializableExtra(String key) {
        return (T) mContext.getIntent().getSerializableExtra(key);
    }

    /////////////////工具方法////////////////////////


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialogForLoading();
        HttpFactory.getNetHelper().cancelTag(mContext);
    }
}
