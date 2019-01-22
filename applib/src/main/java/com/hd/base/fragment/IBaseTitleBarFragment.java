package com.hd.base.fragment;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;


import com.hd.R;
import com.hd.base.IBaseFragment;
import com.hd.view.TitleBar;

/**
 * titleView的基类
 * Created by liugd on 2017/3/20.
 */

public abstract class IBaseTitleBarFragment extends IBaseFragment {

    TitleBar titleBar;

    private boolean isInitHasTitleBar = true;

    /***
     * 设置BODY VIEW 也可以用setBodyID 代替
     *
     * @return
     */
    protected View setBodyView(Context mContext) {
        return null;
    }

    /***
     * 设置下面的布局
     *
     * @return
     */
    protected int getBodyId() {
        return 0;
    }

    /****
     * 父类，首先查看setLayoutID() 是否为0，不为0则加载布局，否则执行下面这个方法 添加一个布局
     *
     * @param context
     * @return
     */

    @Override
    protected final View getLayoutView(Context context) {
        return new LinearLayout(context);
    }

    @Override
    protected final void initView() {
        //有自已的布局，则找到布局中指定的控件
        int layoutID = getLayoutId();
        if (layoutID != 0) {
            titleBar = findViewByID(R.id.titleBar);
        } else {//否则初始化自定义布局的组件
            LinearLayout linearLayout = (LinearLayout) rootView;
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            titleBar = new TitleBar(mContext);
            linearLayout.addView(titleBar);
            LinearLayout.LayoutParams llMatchParent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            View bodyView = setBodyView(mContext);
            if (bodyView != null) {
                linearLayout.addView(bodyView, llMatchParent);
            } else {
                int bodyID = getBodyId();
                if (bodyID == 0) {
                    throw new RuntimeException("ID 不能为0");
                } else {
                    linearLayout.addView(View.inflate(mContext, bodyID, null), llMatchParent);
                }
            }
        }
        if (getTitleBar() != null) {
            //初始化时没有titleBar
            if (!isInitHasTitleBar()) {
                getTitleBar().setVisibility(View.GONE);
            } else {
                getTitleBar().initFragmentSetting(this);
            }
        }
        //初始化下面的后面的view
        initTitleBarView();

    }

    protected abstract void initTitleBarView();

    /***
     * 初始化的时候是否隐藏titleBar,解决懒加载时隐藏不及时，可见titlebar的问题
     *
     * @return
     */
//    protected boolean isInitHideTitleBar() {
//        return false;
//    }

    /***
     * 是否是标题栏
     *
     * @return
     */
    protected boolean isInitHasTitleBar() {
        return isInitHasTitleBar;
    }


    /**
     * 初始化时是否有titleBar
     *
     * @param initHasTitleBar
     */
    public void setInitHasTitleBar(boolean initHasTitleBar) {
        isInitHasTitleBar = initHasTitleBar;
    }

    //    /***
//     * 此布局可替换
//     *
//     * @return
//     */
//    @Override
//    public int getLayoutId() {
//        return R.layout.layout_base_titlebar;
//    }
    protected TitleBar getTitleBar() {
        return titleBar;
    }




}
