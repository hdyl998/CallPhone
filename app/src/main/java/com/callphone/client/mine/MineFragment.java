package com.callphone.client.mine;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.callphone.client.R;
import com.callphone.client.about.AboutFragment;
import com.callphone.client.base.SPConstants;
import com.callphone.client.base.data.AppSaveData;
import com.callphone.client.common.DeviceHelper;
import com.callphone.client.home.socket.MsgSocket;
import com.callphone.client.main.OnPageCheckedListener;
import com.callphone.client.main.bean.EventItem;
import com.callphone.client.main.mine.LoginManager;
import com.callphone.client.main.mine.UserCacheConfig;
import com.callphone.client.mine.login.ChangePwdFragment;
import com.callphone.client.utils.RomUtil;
import com.callphone.client.utils.StringUtil;
import com.callphone.client.webview.WebViewFragment;
import com.callphone.client.webview.WebViewManager;
import com.hd.base.dialog.SimpleDialog;
import com.hd.base.fragment.IBaseTitleBarFragment;
import com.hd.net.NetBuilder;
import com.hd.net.NetCallbackImpl;
import com.hd.net.coderemind.IMessage;
import com.hd.net.socket.NetEntity;
import com.hd.utils.GoUtils;
import com.hd.utils.bufferknife.MyBindView;
import com.hd.utils.bufferknife.MyBufferKnifeUtils;
import com.hd.utils.log.impl.LogUitls;
import com.hd.utils.save.IntValueSaveHelper;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by liugd on 2019/2/26.
 */

public class MineFragment extends IBaseTitleBarFragment implements OnPageCheckedListener {

    @MyBindView(value = R.id.ivHead)
    ImageView ivHead;

    @MyBindView(value = R.id.tvLogin)
    TextView tvLogin;
    @MyBindView(value = R.id.llMiSetting, click = true)
    View llMiSetting;
    @MyBindView(value = R.id.llMiMsgSetting, click = true)
    View llMiMsgSetting;


    @MyBindView(value = R.id.tvLogout)
    TextView tvLogout;
    @MyBindView(value = R.id.tvChangePwd)
    TextView tvChangePwd;


    private final IntValueSaveHelper helper = new IntValueSaveHelper(SPConstants.File_cache, SPConstants.KEY_mine_remind);


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llHeader:
                LoginManager.isLoginAndRedict(mContext);
                break;
            case R.id.tvChangePwd:
                startFragment(ChangePwdFragment.class);
                break;
            case R.id.llMiSetting:
                startFragment(MiUIHelpFragment.class);
                return;
            case R.id.llMiMsgSetting:
                String url = "https://csmobile.alipay.com/detailSolution.htm?knowledgeType=1&scene=dd_gdwt&questionId=201602080133";
                WebViewManager.launchWithAbsoluteUrl(mContext, "小米手机消息提醒设置说明", url);
                break;

            case R.id.tvAppSetting:
                GoUtils.goAppDetailsSetting(mContext);
                return;
            case R.id.tvAbout:
                startFragment(AboutFragment.class);
                break;
            case R.id.tvLogout:
                SimpleDialog.create(mContext)
                        .setTvTitle("确定要退出登录？")
                        .setBtnLeft("否")
                        .setBtnRight("退出登录")
                        .setOnClickListener(new SimpleDialog.SimpleDialogClick() {
                            @Override
                            public void onRightClick(SimpleDialog simpleDialog) {
                                requestExit();
                            }
                        });
                break;
            case R.id.tvScreenMode:
                new AlertDialog.Builder(mContext)
                        .setSingleChoiceItems(new String[]{"低亮屏模式", "熄屏模式", "高亮屏模式"}, DeviceHelper.getInstance().getSaveHelper().getValue(),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DeviceHelper.getInstance().updateNewRequire(which);
                                        dialog.dismiss();
                                    }
                                }).show();
                break;
            case R.id.llHWSetting:
                String url1="https://jingyan.baidu.com/article/597a06430676cb312b5243ed.html";
                WebViewManager.launchWithAbsoluteUrl(mContext, "华为手机系统参考设置", url1);
                break;
        }
    }

    private void requestExit() {
        //连接成功的，才先断开socket
        if (MsgSocket.getInstance().isConnectSuccess()) {
            MsgSocket.getInstance().stopSocket();
        }
        showDialogForLoading();
        NetBuilder.create(mContext)
                .setMessage(IMessage.allMessage)
                .start("loginOut", new NetCallbackImpl() {
                    @Override
                    public void onSuccess(NetEntity entity) throws Exception {
                        LoginManager.logout();
                        LogUitls.print("logint", "requestExit");
                        hideDialogForLoadingImmediate();
                    }

                    @Override
                    public void onError(NetEntity entity) throws Exception {
                        hideDialogForLoading();
                    }
                });
    }

    @Override
    public int[] setClickIDs() {
        return new int[]{R.id.llHWSetting,R.id.tvScreenMode, R.id.tvChangePwd, R.id.llHeader, R.id.tvAppSetting, R.id.tvAbout, R.id.tvLogout};
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
            tvLogin.setText("点击这里登录");
            tvLogout.setVisibility(View.GONE);
            tvChangePwd.setVisibility(View.GONE);
        } else {
            ivHead.setImageResource(R.mipmap.ic_person);
            tvLogin.setText(StringUtil.mobileStrHide(item.phone));
            tvLogout.setVisibility(View.VISIBLE);
            tvChangePwd.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onPageChecked() {
        if (!isInited()) {
            return;
        }
        if (helper.isDefaultValue()) {
            helper.setValue(1);

            new AlertDialog.Builder(mContext).setTitle("温馨提示")
                    .setMessage("如果您是小米手机，请按照本页菜单里的参考操作（1）、（2）\n进行设置\n如果您是华为手机，请按华为手机参考设置")
                    .setPositiveButton("我知道了",null).show();
        }


    }
}
