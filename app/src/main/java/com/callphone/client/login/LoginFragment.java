//package com.callphone.client.login;
//
//import android.app.Activity;
//import android.content.Context;
//import android.text.Editable;
//import android.view.View;
//import android.widget.TextView;
//
//import com.hd.base.dialog.SimpleDialog;
//import com.hd.base.fragment.IBaseTitleBarFragment;
//import com.hd.base.interfaceImpl.TextWatcherImpl;
//import com.hd.base.launch.AppLauncher;
//import com.hd.base.maininterface.IComCallBacks;
//import com.hd.cache.SpUtils;
//import com.hd.net.NetBuilder;
//import com.hd.net.NetCallbackImpl;
//import com.hd.net.coderemind.IMessage;
//import com.hd.net.socket.NetEntity;
//import com.hd.utils.EditTextUtil;
//import com.hd.utils.bufferknife.MyBindView;
//import com.hd.utils.bufferknife.MyBufferKnifeUtils;
//import com.hd.utils.log.impl.LogUitls;
//import com.hd.utils.loopdo.MyCountDownTimer;
//import com.hd.utils.other.KeyboardUtils;
//import com.hd.utils.text.ITextStyle;
//import com.hd.utils.toast.ToastUtils;
//import com.hd.view.edit.EditTextWithDel;
//import com.hd.view.roundrect.ShapeCornerBgView;
//import com.topsc.R;
//import com.topsc.base.ColorConsts;
//import com.topsc.base.SPConstants;
//import com.topsc.base.data.AppSaveData;
//import com.topsc.main.bean.EventItem;
//import com.topsc.third.UmengOurthHelper;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//
//import org.greenrobot.eventbus.EventBus;
//
///**
// * Note：None
// * Created by Liuguodong on 2018/12/29 15:07
// * E-Mail Address：986850427@qq.com
// */
//public class LoginFragment extends IBaseTitleBarFragment {
//
//
//    @MyBindView(R.id.tvInfo)
//    TextView tvInfo;
//
//    @MyBindView(R.id.etPhone)
//    EditTextWithDel etPhone;
//
//    @MyBindView(R.id.etPwd)
//    EditTextWithDel etPwd;
//
//
//    @MyBindView(value = R.id.btnGet, click = true)
//    ShapeCornerBgView btnGet;
//
//    @MyBindView(value = R.id.btnConfirm, click = true)
//    TextView btnConfirm;
//
//
//    @Override
//    protected void initTitleBarView() {
//        MyBufferKnifeUtils.inject(this);
//        ITextStyle.setTextStyle(tvInfo, ITextStyle.createDefaultText("点击确定即表示已阅读并同意"),
//                ITextStyle.createClickText("《注册会员服务条款》").
//                        setColor(ColorConsts.COLOR_THEME).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TextScanFragment.launch(mContext, "注册会员服务条款", "clause/index");
//                    }
//                }));
//        getTitleBar().setBottomLineVisiable(false);
//        EditTextUtil.setTypePhoneNumAddSpace(etPhone);
////        String phone = SpUtils.getString(SPConstants.File_cache, SPConstants.KEY_phone);
////        if (phone != null) {
////            etPhone.setText(phone);
////            etPhone.setSelection(etPhone.length());
////        }
//        etPhone.addTextChangedListener(textWatcher);
//        etPwd.addTextChangedListener(textWatcher);
//    }
//
//    private TextWatcherImpl textWatcher = new TextWatcherImpl() {
//        @Override
//        public void afterTextChanged(Editable s) {
//            if (etPhone.length() == 13 && etPhone.getText().charAt(0) == '1' && etPwd.length() > 5) {
//                btnConfirm.setEnabled(true);
//            } else {
//                btnConfirm.setEnabled(false);
//            }
//        }
//    };
//
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.fragment_login;
//    }
//
//
//    @Override
//    public int[] setClickIDs() {
//        return new int[]{R.id.btnWechat};
//    }
//
//    MyCountDownTimer countDownTimer;
//
//
//    private void createLooper() {
//        if (countDownTimer == null) {
//            countDownTimer = new MyCountDownTimer(60, 1);
//            countDownTimer.setOnTimerEvent(new MyCountDownTimer.OnTimerEvent() {
//                @Override
//                public void onStart(int leftSeconds) {
//                    btnGet.setEnabled(false);
//                    onTicker(leftSeconds);
//                }
//
//                @Override
//                public void onTicker(int leftSeconds) {
//                    btnGet.setText(leftSeconds + "S后重发");
//                }
//
//                @Override
//                public void onFinish() {
//                    btnGet.setEnabled(true);
//                    btnGet.setText("获取验证码");
//                }
//            });
//        }
//        countDownTimer.start();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnWechat:
//                doOauthVerify(SHARE_MEDIA.WEIXIN);// 微信登录
//                break;
//            case R.id.btnConfirm:
//                String phone = etPhone.getTextTrim().replace(" ", "");
//                String pwd = etPwd.getTextTrim();
//
//                showDialogForLoading();
//                NetBuilder.create(mContext)
//                        .add2Url("mobile", phone)
//                        .add2Url("smscode", pwd)
//                        .setMessage(IMessage.successMessage)
//                        .start("user/login/", new NetCallbackImpl<LoginSuccItem>() {
//                            @Override
//                            public void onSuccess(NetEntity<LoginSuccItem> entity) throws Exception {
//                                hideDialogForLoadingImmediate();
//                                AppSaveData.getUserVInfo().setUserInfo(entity.getDataBean());
//                                resultOKFinish();
//                            }
//
//                            @Override
//                            public void onError(NetEntity entity) throws Exception {
//                                hideDialogForLoading();
//                                SimpleDialog.create(mContext).showSingleInfo(entity.getCodeString());
//                            }
//                        });
//
//
//                break;
//            case R.id.btnGet:
//                String phone1 = etPhone.getTextTrim().replace(" ", "");
//                if (phone1.length() != 11) {
//                    ToastUtils.show("请输入手机号码");
//                    return;
//                }
//                SpUtils.putString(SPConstants.File_cache, SPConstants.KEY_phone, phone1);
//                showDialogForLoading();
//                btnGet.setEnabled(false);
//                NetBuilder.create(mContext)
//                        .add2Url("mobile", phone1)//type int 1：注册，2：修改密码，3：通用,4：登录确认
//                        .add2Url("type", 1)
//                        .setMessage(IMessage.errorMessage)
//                        .start("user/sendsms/", new NetCallbackImpl() {
//                            @Override
//                            public void onSuccess(NetEntity entity) throws Exception {
//                                hideDialogForLoading();
//                                ToastUtils.show("发送成功");
//                                createLooper();
//                            }
//
//                            @Override
//                            public void onError(NetEntity entity) throws Exception {
//                                hideDialogForLoading();
//                                btnGet.setEnabled(true);
//                            }
//                        });
//                break;
//        }
//    }
//
//    private void resultOKFinish() {
//        KeyboardUtils.hideSoftKeyboard(mContext);
//        mContext.setResult(Activity.RESULT_OK);
//        mContext.finish();
//        //发送登录成功的消息
//        EventBus.getDefault().post(new EventItem.LoginEvent());
//    }
//
//    private static final String TAG = "LoginFragment";
//
//    //第三方登录,只微信
//    String registerType = "weixin";
//
//    /**
//     * 开始第三方登录
//     *
//     * @param shareMedia
//     */
//    private void doOauthVerify(SHARE_MEDIA shareMedia) {
//        showDialogForLoading();
//        UmengOurthHelper.doOauthVerify(mContext, shareMedia, new IComCallBacks<UmengOurthHelper.InfoParams>() {
//            @Override
//            public void call(UmengOurthHelper.InfoParams obj) {
//                hideDialogForLoading();
//                LogUitls.print("授权登录", obj);
//                if (obj == null) {
//                    return;
//                }
////                //授权登录之后就注销
//                UmengOurthHelper.deleteOauth(mContext, obj.shareMedia, null);
//                loginThird(obj.getPrefixUid(), obj.getNickname(), obj.getHeadImg(), obj.getToken(), obj.getOpenid(), obj.getUid());
//            }
//        });
//    }
//
//    private void loginThird(String prefixUid, String nickname, String headImg, String token, String openid, String uid) {
////        NetBuilder.create(mContext)
////                .start
//
//    }
//
//    @Override
//    public int setBackgroundColor() {
//        return R.color.white;
//    }
//
//    public static void launchForRestult(Context context, int resultCode) {
//        AppLauncher.withFragment(context, LoginFragment.class)
//                .setAnimType(AppLauncher.ANIMTYPE_SLIDE_BOTTOM)
//                .launch(resultCode);
//    }
//
//    public static void launchWithAction(Context mContext, String loginSuccessAction) {
//        AppLauncher.withFragment(mContext, LoginFragment.class)
//                .setAnimType(AppLauncher.ANIMTYPE_SLIDE_BOTTOM)
//                .setObjs(loginSuccessAction)
//                .launch();
//    }
//}
