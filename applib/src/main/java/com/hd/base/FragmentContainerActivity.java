package com.hd.base;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.hd.base.anno.NameAnno;
import com.hd.base.launch.AppLauncher;
import com.hd.base.theme.ThemeUtils;
import com.hd.utils.log.impl.LogUitls;

/***
 * 持有碎片的activity
 */
public final class FragmentContainerActivity extends IBaseActivity {

    IBaseFragment fragment;

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initView() {
        String className = getIntent().getStringExtra(BaseConstants.ID_NEW_MAIN);
        try {
            Class<?> class1 = Class.forName(className);
            fragment = (IBaseFragment) class1.newInstance();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(android.R.id.content, fragment).commit();//加到根布局
            //初始化主题样式,放这里防止为空

            ThemeUtils.initThemeStyle(mContext, fragment);
            //禁用初始化懒加载
            fragment.setLacyInit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        LogUitls.print("onBackPressedActivity", "Activity");
        //将事件交给系统处理，返回真时由自已处理
        if (fragment != null && !fragment.onBackPressed()) {
            super.onBackPressed();
            LogUitls.print("onBackPressed", "调用系统的onBackPressed");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fragment != null) {
            try {
                fragment.onGetActivityResult(RESULT_OK == resultCode, requestCode, data);//回调
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LogUitls.print(TAG, "requestCode  " + requestCode + " resultCode" + resultCode + "  data" + data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static final String TAG = "FragmentContainerActivity";

    /***
     * 统计页面生命周期所需要的
     *
     * @return
     */
    @Override
    protected final String getTitleName() {
        if (fragment != null) {
            NameAnno nameAnno = fragment.getClass().getAnnotation(NameAnno.class);
            String str = null;
            if (nameAnno != null) {
                str = nameAnno.name();
//                LogUitls.print("取到了注解的名字" + str);
            }
            if (TextUtils.isEmpty(str)) {
                str = fragment.getClass().getSimpleName();
            }
            return str;
        }
        return getClass().getSimpleName();
    }

    @Override
    public void finish() {
        super.finish();
        AppLauncher.actionFinishAnimation(this);
        if (fragment != null)
            fragment.onFinish();
    }
}
