package com.callphone.client.mine;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.callphone.client.R;
import com.callphone.client.base.data.AppSaveData;
import com.callphone.client.home.MiUIHelpFragment;
import com.callphone.client.main.bean.EventItem;
import com.callphone.client.main.mine.UserCacheConfig;
import com.callphone.client.mine.login.ChangePwdFragment;
import com.callphone.client.main.mine.LoginManager;
import com.callphone.client.utils.MIUIUtils;
import com.callphone.client.utils.OSHelper;
import com.hd.base.fragment.IBaseTitleBarFragment;
import com.hd.utils.GoUtils;
import com.hd.utils.bufferknife.MyBindView;
import com.hd.utils.bufferknife.MyBufferKnifeUtils;
import com.hd.utils.log.impl.LogUitls;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by liugd on 2019/2/26.
 */

public class MineFragment extends IBaseTitleBarFragment {

    @MyBindView(value = R.id.ivHead)
    ImageView ivHead;

    @MyBindView(value = R.id.tvLogin)
    TextView tvLogin;
    @MyBindView(value = R.id.llMiSetting, click = true)
    View llMiSetting;


    @Override
    public void onClick(View v) {

        if (!LoginManager.isLoginAndRedict(mContext)) {
            return;
        }
        switch (v.getId()) {
            case R.id.llHeader:
                break;
            case R.id.tvSetting:
                startFragment(SettingFragment.class);
                break;
            case R.id.tvChangePwd:
                startFragment(ChangePwdFragment.class);
                break;
            case R.id.llMiSetting:
                startFragment(MiUIHelpFragment.class);
                return;
            case R.id.tvAppSetting:
                GoUtils.goAppDetailsSetting(mContext);
                return;
        }
    }


    @Override
    public int[] setClickIDs() {
        return new int[]{R.id.tvChangePwd, R.id.tvSetting, R.id.llHeader, R.id.tvAppSetting};
    }

    @Override
    protected void initTitleBarView() {
        MyBufferKnifeUtils.inject(this);
        updateUI(LoginManager.isLogin() ? AppSaveData.getUserVInfo() : null);

//        if (MIUIUtils.isMIUI()) {
//            llMiSetting.setVisibility(View.VISIBLE);
//        } else {
//            llMiSetting.setVisibility(View.GONE);
//        }

    }

    @Override
    protected boolean isEventBus() {
        return true;
    }


    /***
     * 当用户登录时
     */
    @Subscribe
    public void onUserLogin(EventItem.LoginEvent item) {
        updateUI(AppSaveData.getUserVInfo());
    }

    /***
     * 当用户登录时
     */
    @Subscribe
    public void onUserLogout(EventItem.LoginOutEvent item) {
        updateUI(null);
    }

    private void updateUI(UserCacheConfig item) {

        if (item == null) {
            ivHead.setImageResource(R.mipmap.ic_default_person);
            tvLogin.setText("登录后查看");
        } else {
            ivHead.setImageResource(R.mipmap.ic_person);
            tvLogin.setText(item.phone);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }
}
