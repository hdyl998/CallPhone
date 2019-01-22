package com.hd.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.hd.R;
import com.hd.appconfig.IAppConfigFactory;
import com.hd.base.adapterbase.BaseViewHolder;
import com.hd.base.datacount.EventInjectHelper;
import com.hd.base.dialog.ILoadingDialog;
import com.hd.base.launch.AppLauncherUtils;
import com.hd.base.maininterface.IBaseMethod;
import com.hd.base.maininterface.IErrorRequest;
import com.hd.base.theme.ITheme;
import com.hd.utils.device.AppDeviceInfo;
import com.hd.utils.log.impl.LogUitls;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by liugd on 2016/12/5.
 */
public abstract class IBaseFragment extends Fragment implements View.OnClickListener, BaseConstants, IErrorRequest, IBaseMethod, ILoadingDialog, ITheme {
    public final static int REQUEST_CODE_DEFAULT = 1451;//缺省的请求码
    public final static int REQUEST_CODE_LOGIN = 888;//登录缺省用的请求码

    protected BaseViewHolder mHolder;

    //value
    private boolean isVisible;//是否可见
    private boolean isPrepared;//是否准备好了
    private boolean isInited = false;//是否初始化完成
    private boolean isVisibleEnable = true;//是否可用

    private boolean isHomeTab = false;//是否是主页的TAB


    public boolean isGetData;//是否请求到数据

    protected boolean isEverGetData = false;//是否曾经获取过数据

    public boolean isEverGetData() {
        return isEverGetData;
    }

    public void setEverGetData(boolean everGetData) {
        isEverGetData = everGetData;
    }

    /**
     * Activity引用
     */
    protected IBaseActivity mContext;

    /***
     * 根VIEW
     */
    protected View rootView;


    public View getRootView() {
        return rootView;
    }

    @Override
    public void onClick(View v) {
    }


    //开始加载数据lgd
    @Override
    public void startGetMyData() {
    }


//    public void setArgumentsData(String key, Object serializable) {
//        Bundle bundle = getArguments();
//        if (bundle == null) {
//            bundle = new Bundle();
//            setArguments(bundle);
//        }
//        if (serializable instanceof Serializable)
//            bundle.putSerializable(key, (Serializable) serializable);
//    }
//
//    public final <T> T getArgumentsData(String key) {
//        if (getArguments() == null) {
//            return null;
//        }
//        return (T) getArguments().get(key);
//    }

    /***
     * 安全请求数据
     */
    public void startGetDataSafely() {
        //初始化完成时且没有请求成功时
        if (isInited() && !isGetData()) {
            startGetMyData();
            setGetData(true);//转换标志，当请求错误时，才可以请求下一组数据
        }
    }

    /**
     * 初始化点击事件
     */
    public final void initClickListener() {
        int ids[] = setClickIDs();// 设置点击ID
        if (ids != null && ids.length > 0) {
            int index = 0;
            try {
                for (int id : ids) {
                    findViewByID(id).setOnClickListener(this);
                    index++;
                }
            } catch (Exception e) {
                LogUitls.print("initClickListener", "error index " + index + " " + getClass().getSimpleName());
            }

        }
    }

    //已交给activity来统计，所以这里不需要统计
//    @Override
//    public void onResume() {
//        super.onResume();
//        MobclickAgent.onPageStart(getName()); // 统计页面
//        TCAgent.onPageStart(mContext, getClass().getSimpleName());
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        MobclickAgent.onPageEnd(getName()); // 统计页面
//        TCAgent.onPageEnd(mContext, getClass().getSimpleName());
//    }

    ////////////////////重写系统组件//////////////////////////
    @Override
    public final void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleEnable) {
            //初始化完成后才做处理
//            if (isInited) {
//                onVisibleHint(isVisibleToUser);
//            }
            if (!isLaycyInitFinal()) {
                return;
            }
            if (getUserVisibleHint()) {
                isVisible = true;
                lazyLoad();
            } else {
                isVisible = false;
            }
        }
    }


    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (IBaseActivity) getActivity();
        //这里优先去解析布局里的，如果布局ID=0表示是使用的是new出来的则使用getLayoutView(mcontext)
        int layoutID = getLayoutId();
        if (layoutID != 0) {
            rootView = inflater.inflate(layoutID, container, false);
        } else {
            rootView = getLayoutView(mContext);
        }
        if (rootView != null) {
            mHolder = BaseViewHolder.get(rootView);
            return rootView;
//            emptyViewManager = new EmptyViewManager(mContext, rootView);
//            return emptyViewManager.getRootView();
        } else {
            throw new RuntimeException("getLayoutId 和 getLayoutView你必须实现一个");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        LogUitls.print("UserMainFragmnet", "onDestroyView" + rootView + this.getClass());
    }

    /***
     * 延时加载开关,用于初始化时不确定标题栏，需要等待接口返回后再定制
     *
     * @return
     */
    protected boolean isInitedLater() {
        return false;
    }


    /***
     * 是否可以调用延时加载了
     */
    private boolean canInitLater = false;


    /***
     * 手动调用初始化
     *
     * @return
     */
    protected final boolean initLaterByHand() {
        if (getContext() == null) {
            return false;
        }
        if (isInitedLater() == true && canInitLater == true) {
            canInitLater = false;
            initAllDatas();
            return true;
        }
        return false;
    }

    /***
     * 当延时加载之前要做的事，动画等初始化
     */
    protected void onInitLaterInitViews() {

    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isLaycyInitFinal()) {//懒加载
            isPrepared = true;
            lazyLoad();
        } else {//不懒加载
            //延时初始化，等手动初始化
            if (isInitedLater()) {
                canInitLater = true;
                onInitLaterInitViews();
                return;
            }
            if (IAppConfigFactory.getConfig().isOnline()) {
                try {
                    initAllDatas();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                initAllDatas();
            }
        }
    }

    /***
     * findview方法
     *
     * @param id
     * @param <T>
     * @return
     */
    public final <T extends View> T findViewByID(@IdRes int id) {
        return (T) mHolder.getView(id);
    }


    public TextView setText(int viewId, CharSequence value) {
        TextView view = findViewByID(viewId);
        view.setText(value);
        return view;
    }

    public TextView setTextColor(int viewId, int textColor) {
        TextView view = findViewByID(viewId);
        view.setTextColor(textColor);
        return view;
    }

    public View setVisibility(int viewId, int visible) {
        View view = findViewByID(viewId);
        view.setVisibility(visible);
        return view;
    }

    /***
     * findview方法
     *
     * @param id
     * @param <T>
     * @return
     */
    public final <T extends View> T findViewById(@IdRes int id) {
        return findViewByID(id);
    }

    //////////////////需要字类去重写的方法////////////////////////

    /**
     * 设置contentview 使用new出来的对象
     *
     * @return
     */
    protected View getLayoutView(Context mContext) {
        return null;
    }

    /**
     * 设置点击事件
     *
     * @return
     */
    public int[] setClickIDs() {
        return null;
    }

    /***
     * 子类可以覆写
     *
     * @return
     */
    protected boolean isLacyInit() {
        return false;
    }

    /****
     * 懒加载总判断
     * @return
     */
    private boolean isLaycyInitFinal() {
        if (isSetLacyInit == null) {
            return isLacyInit();
        }
        return isSetLacyInit;
    }

    /***
     * 是否手动设置懒加载
     */
    private Boolean isSetLacyInit;


    public void setVisibleEnable(boolean visibleEnable) {
        isVisibleEnable = visibleEnable;
    }

    /***
     * 初始化采用懒加载，只针对viewpager+fragment这种形式
     *
     * @param lacyInit
     */
    public void setLacyInit(boolean lacyInit) {
        isSetLacyInit = lacyInit;
    }

    public boolean isGetData() {
        return isGetData;
    }


    public boolean isHomeTab() {
        return isHomeTab;
    }


    public void setHomeTab(boolean homeTab) {
        isHomeTab = homeTab;
    }

    //是否初始化完成
    public final boolean isInited() {
        return isInited;
    }


    public void setGetData(boolean getData) {
        isGetData = getData;
    }

    /***
     * 懒加载
     */
    protected final void lazyLoad() {
        if (!isPrepared || !isVisible || isInited) {
            return;
        }
        isInited = true;
        //延时初始化，等手动初始化
        if (isInitedLater()) {
            canInitLater = true;
            onInitLaterInitViews();
            return;
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


    /***
     * 初始化界面之前要做的
     */
    protected void onPreInitView() {
    }


    /***
     * 初始化调用
     */
    private final void initAllDatas() {
        onPreInitView();
        initView();
        initClickListener();
        if (isEventBus()) {
            EventBus.getDefault().register(this);
        }
        addFullScreenPadding();
        isInited = true;//初始化完成的标志
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isEventBus()) {
            EventBus.getDefault().unregister(this);
        }
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
     * 设置contentView ID
     *
     * @return
     */
    @LayoutRes
    public int getLayoutId() {
        return 0;
    }


    /***
     * 初始化View让子类去实现
     */
    protected abstract void initView();



//    /***
//     * 设置状态栏是颜色(API 21及以上有效)(含有电量，WIFI状态，时间显示的顶栏)
//     *
//     * @return
//     */
//    public int setStatusBarColor() {
//        return R.color.statusBarColorLight;
//    }

    /***
     * 背景颜色
     * 背景颜色默认为灰色
     *
     * @return
     */
    public int setBackgroundColor() {
        return R.color.defaultBgColor;
    }

    /***
     * 是否有颜色状态栏
     * 是否是颜色式状态栏(API 21及以上有效)默认是有的，当需要全屏顶部沉浸显示图片时重写此方法，把变量设置成false
     *
     * @return
     */
    public boolean isStatusBarColor() {
        return true;
    }


    /***
     * 是否是浅色的状态栏
     * @return
     */
    public boolean isStatusBarLightStyle() {
        return true;
    }


    /***
     * onbackpress回调 返回false由系统处理，true由自已处理
     *
     * @return
     */
    public boolean onBackPressed() {
        return false;
    }


    //当点击了结束的时候
    public void onFinish() {
    }


    public <T> T getSerializableExtra(String key) {
        return (T) mContext.getIntent().getSerializableExtra(key);
    }

    /***
     * use mContext.startActivityForResult
     *
     * @param intent
     * @param requestCode
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        mContext.startActivityForResult(intent, requestCode);
    }

    /***
     * ACTIVITY 返回结果回调
     *
     * @param isResultOK
     * @param requestCode
     * @param data
     */
    public void onGetActivityResult(boolean isResultOK, int requestCode, Intent data) {
    }

    /***
     * USE onGetActivityResult instead
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Deprecated
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    /////////////////////////////工具方法////////////////////////////////////


    public void showDialogForLoading(String msg) {
        if (mContext instanceof IBaseActivity) {
            mContext.showDialogForLoading(msg);
        }

    }

    public void showDialogForLoading() {
        if (mContext instanceof IBaseActivity)
             mContext.showDialogForLoading();

    }

    public void hideDialogForLoading() {
        if (mContext instanceof IBaseActivity) {
            mContext.hideDialogForLoading();
        }
    }

    public void hideDialogForLoadingImmediate() {
        if (mContext instanceof IBaseActivity) {
            mContext.hideDialogForLoadingImmediate();
        }
    }


//    protected EmptyViewManager emptyViewManager;
//
//
//    public EmptyViewManager getEmptyViewManager() {
//        return emptyViewManager;
//    }
//
//    @Override
//    public void showLoading() {
//        emptyViewManager.showLoading();
//    }
//
//    @Override
//    public void showError() {
//        emptyViewManager.showError();
//    }
//
//    @Override
//    public void showNone() {
//        emptyViewManager.showNone();
//    }
//
//    @Override
//    public void showEmpty() {
//        emptyViewManager.showEmpty();
//    }

    //////////////////////////////////////////////////////////
    ////////////////////////启动一个页面的工具/////////////////
    ////////////////////////////////////////////////////////


    public void startFragment(Class<? extends IBaseFragment> clazz, Object... seObjs) {
        AppLauncherUtils.startFragment(mContext, clazz, seObjs);
    }

    public void startFragment(Class<? extends IBaseFragment> clazz) {
        AppLauncherUtils.startFragment(mContext, clazz);
    }

    public void startFragmentForRestult(Class<? extends IBaseFragment> clazz, int requestCode, Object... seObjs) {
        AppLauncherUtils.startFragmentForRestult(mContext, clazz, requestCode, seObjs);
    }

    /***
     * 启动Activity时，显示软键盘
     */
    public void showWindowSoftKeyboard() {
        mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    /***
     * 启动Activity时，隐藏软键盘，弹出软键盘时，布局不被挤压
     */
    public void setSoftKeyboardAdjustPan() {
        mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

//    public void showSnackMessage(String message) {
//        ToastUtils.showSnackMessage(mContext, message);
//    }


    @Override
    public void onDetach() {
        super.onDetach();
//        mContext=null;
//        LogUitls.print(TAG, "onDetach");
    }

    private static final String TAG = "IBaseFragment";


    /***
     * 全屏主题时,需要增加一个padding,仅当有llStatusBar这个id存在时
     */
    public void addFullScreenPadding() {
        if (!mContext.isStatusBarColor()) {
            View view = findViewById(R.id.llStatusBar);
            if (view != null) {
//                view.setPaddingRelative(0,0,);
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + AppDeviceInfo.mStatusBarHeight,
                        view.getPaddingRight(), view.getPaddingBottom()
                );
            }
        }
    }

}
