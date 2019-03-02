package com.callphone.client.mine.login;

import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.callphone.client.R;
import com.callphone.client.base.SPConstants;
import com.hd.base.fragment.IBaseTitleBarFragment;
import com.hd.base.interfaceImpl.TextWatcherImpl;
import com.hd.cache.SpUtils;
import com.hd.net.NetBuilder;
import com.hd.net.NetCallbackImpl;
import com.hd.net.socket.NetEntity;
import com.hd.utils.EditTextUtil;
import com.hd.utils.bufferknife.MyBindView;
import com.hd.utils.bufferknife.MyBufferKnifeUtils;
import com.hd.utils.loopdo.MyCountDownTimer;
import com.hd.utils.toast.ToastUtils;
import com.hd.view.edit.EditTextWithDel;
import com.hd.view.roundrect.ShapeCornerBgView;

public class GetCodeFragment extends IBaseTitleBarFragment {

    @MyBindView(R.id.etPhone)
    EditTextWithDel etPhone;

    @MyBindView(R.id.etCode)
    EditTextWithDel etCode;


    @MyBindView(R.id.etPwd)
    EditTextWithDel etPwd;


    @MyBindView(value = R.id.scb_get, click = true)
    ShapeCornerBgView btnGet;

    @MyBindView(value = R.id.btnConfirm, click = true)
    TextView btnConfirm;

    @MyBindView(R.id.cbv_aggreement)
    CheckBox cbvAggreement;

    @MyBindView(value = R.id.tv_aggreement, click = true)
    TextView tvAggreement;

    @MyBindView(value = R.id.iv_pwd_eye, click = true)
    ImageView ivPwdEye;


    public final static int TYPE_REGISTER = 0;
    public final static int TYPE_FORGET_PWD = 2;


    String[] titles = {"注册手机", "验证码登录", "忘记密码", "绑定手机", "重置密码"};
    String[] confirms = {"提交", "登录", "提交", "提交", "提交"};

    int type = 0;// 1-注册 2-登录 3-修改密码 4-绑定手机

    @Override
    protected void initTitleBarView() {
        MyBufferKnifeUtils.inject(this);
        type = mContext.getIntent().getIntExtra(ID_NEW_PARAM1, 0);
        ((TextView) findViewByID(R.id.tv_title)).setText(titles[type]);
        btnConfirm.setText(confirms[type]);
        findViewByID(R.id.rlv_lableframe).setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        EditTextUtil.setTypePhoneNumAddSpace(etPhone);
        etPhone.addTextChangedListener(textWatcher);
        etCode.addTextChangedListener(textWatcher);
        etPwd.addTextChangedListener(textWatcher);
        etPwd.addPasswordFilter();
        cbvAggreement.setChecked(true);
        cbvAggreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateEnableUi();
            }
        });
    }

    /***
     * 初始化密码输入器
     */
    private void initPwdInpter() {
        if (isHidePwd) {
            ivPwdEye.setImageResource(R.mipmap.my_login_eye_n);
            etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            ivPwdEye.setImageResource(R.mipmap.my_login_eye_o);
            etPwd.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        etPwd.setSelection(etPwd.length());
    }

    private TextWatcherImpl textWatcher = new TextWatcherImpl() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateEnableUi();
        }
    };

    private void updateEnableUi() {
        if (etPhone.length() == 13 && etPhone.getText().charAt(0) == '1' && etCode.length() > 3 && isPassWordOK()) {
            btnConfirm.setEnabled(true);
        } else {
            btnConfirm.setEnabled(false);
        }


    }

    private boolean isPassWordOK() {
        if (etPwd.length() < LoginConstants.MIN_PASSWROD_LEN) {
            return false;
        }
//        char ch[] = etPwd.getText().toString().toCharArray();
//        boolean isCharchter = false;
//        boolean isLetter = false;
//        for (char c : ch) {
//            if (Character.isDigit(c)) {
//                isCharchter = true;
//            } else if (Character.isLetter(c)) {
//                isLetter = true;
//            }
//        }
//        if (isCharchter && isLetter) {
//            return true;
//        }
        return true;
    }


    /**
     * 注册
     *
     * @param phone
     * @param code
     */
    private void register(String phone, String pwd, String code) {
        NetBuilder.create(mContext)
                .add2Post("phone", phone)
                .add2Post("password", pwd)
                .add2Post("vcode", code)
                .start("register", new NetCallbackImpl() {
                    @Override
                    public void onSuccess(NetEntity entity) throws Exception {
                        ToastUtils.show("注册成功!");
                        hideDialogForLoadingImmediate();
                        mContext.finish();
                    }

                    @Override
                    public void onError(NetEntity entity) throws Exception {
                        hideDialogForLoading();
                    }
                });
    }

    private void forgetPwd(String phone, String pwd, String code) {
        NetBuilder.create(mContext)
                .add2Post("phone", phone)
                .add2Post("password", pwd)
                .add2Post("vcode", code)
                .start("forgetPwd", new NetCallbackImpl() {
                    @Override
                    public void onSuccess(NetEntity entity) throws Exception {
                        ToastUtils.show("修改成功!");
                        hideDialogForLoadingImmediate();
                        mContext.finish();
                    }

                    @Override
                    public void onError(NetEntity entity) throws Exception {
                        hideDialogForLoading();
                    }
                });
    }


    boolean isHidePwd = true;
    boolean isGetCode = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_aggreement:
                break;
            case R.id.iv_pwd_eye:
                isHidePwd = !isHidePwd;
                initPwdInpter();
                break;
            case R.id.btnConfirm:
                if (!isGetCode) {
                    ToastUtils.show("请先获取验证码！");
                    return;
                }
                if (!cbvAggreement.isChecked()) {
                    ToastUtils.show("请勾选同意《注册协议》！");
                    return;
                }
                String phone = etPhone.getTextTrim().replace(" ", "");
                String code = etCode.getTextTrim();
                String pwd = etPwd.getTextTrim();
                showDialogForLoading();
                switch (type) {// 0-注册 1-登录 2-修改密码 3-绑定手机，4密码重置
                    case 0://注册手机
                        register(phone, pwd, code);
                        break;
                    case 2://忘记密码
                        forgetPwd(phone, pwd, code);
                        break;
                }
                break;
            case R.id.scb_get:
                String phone1 = etPhone.getTextTrim().replace(" ", "");
                if (phone1.length() != 11) {
                    ToastUtils.show("请输入正确的手机号码");
                    return;
                }
                isGetCode = true;
                SpUtils.putString(SPConstants.File_cache, SPConstants.KEY_phone, phone1);
                showDialogForLoading();
                btnGet.setEnabled(false);
                String params[] = {"register", "", "forgetPwd"};        //t int  0-注册 1-登录 2-修改密码
                NetBuilder.create(mContext)
                        .add2Url("type", params[type])
                        .add2Url("phone", phone1)
                        .start("verifyCode", new NetCallbackImpl() {
                            @Override
                            public void onSuccess(NetEntity entity) throws Exception {
                                hideDialogForLoading();
                                ToastUtils.show("发送成功!");
                                createLooper();
                            }

                            @Override
                            public void onError(NetEntity entity) throws Exception {
                                hideDialogForLoading();
                                btnGet.setEnabled(true);
                            }
                        });
                break;
        }
    }


    MyCountDownTimer countDownTimer;

    private void createLooper() {
        if (countDownTimer == null) {
            countDownTimer = new MyCountDownTimer(60, 1);
            countDownTimer.setOnTimerEvent(new MyCountDownTimer.OnTimerEvent() {
                @Override
                public void onStart(int leftSeconds) {
                    btnGet.setEnabled(false);
                    onTicker(leftSeconds);
                }

                @Override
                public void onTicker(int leftSeconds) {
                    btnGet.setText(leftSeconds + "秒后重发");
                }

                @Override
                public void onFinish() {
                    btnGet.setEnabled(true);
                    btnGet.setText("获取验证码");
                }
            });
        }
        countDownTimer.start();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_getcode;
    }

}
