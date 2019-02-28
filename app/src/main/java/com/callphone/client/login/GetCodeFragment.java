package com.callphone.client.login;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.callphone.client.R;
import com.callphone.client.base.SPConstants;
import com.callphone.client.base.data.AppSaveData;
import com.callphone.client.main.bean.EventItem;
import com.callphone.client.utils.StringUtil;
import com.hd.base.fragment.IBaseTitleBarFragment;
import com.hd.base.interfaceImpl.TextWatcherImpl;
import com.hd.cache.SpUtils;
import com.hd.net.NetBuilder;
import com.hd.net.NetCallbackImpl;
import com.hd.net.coderemind.IMessage;
import com.hd.net.socket.NetEntity;
import com.hd.utils.EditTextUtil;
import com.hd.utils.bufferknife.MyBindView;
import com.hd.utils.bufferknife.MyBufferKnifeUtils;
import com.hd.utils.loopdo.MyCountDownTimer;
import com.hd.utils.other.KeyboardUtils;
import com.hd.utils.toast.ToastUtils;
import com.hd.view.edit.EditTextWithDel;
import com.hd.view.roundrect.ShapeCornerBgView;

import org.greenrobot.eventbus.EventBus;

public class GetCodeFragment extends IBaseTitleBarFragment {

    @MyBindView(R.id.etPhone)
    EditTextWithDel etPhone;

    @MyBindView(R.id.etPwd)
    EditTextWithDel etPwd;


    @MyBindView(value = R.id.scb_get, click = true)
    ShapeCornerBgView btnGet;

    @MyBindView(value = R.id.scb_confirm, click = true)
    TextView btnConfirm;

    @MyBindView(R.id.cbv_aggreement)
    CheckBox cbvAggreement;

    @MyBindView(value = R.id.tv_aggreement, click = true)
    TextView tvAggreement;


    String[] titles = {"注册手机", "验证码登录", "忘记密码", "绑定手机", "重置密码"};
    String[] confirms = {"下一步", "登录", "下一步", "下一步", "下一步"};

    int type = 0;// 1-注册 2-登录 3-修改密码 4-绑定手机
    int secondtype = 0;//  0绑定手机， 忘记密码， 1第三方绑定手机 ，重置密码

    @Override
    protected void initTitleBarView() {
        MyBufferKnifeUtils.inject(this);
        type = mContext.getIntent().getIntExtra(ID_NEW_PARAM1, 0);
        secondtype = mContext.getIntent().getIntExtra(ID_NEW_PARAM1, 0);
        ((TextView) findViewByID(R.id.tv_title)).setText(titles[type]);
        getTitleBar().getTvLeft().setText(" 上一步");

        btnConfirm.setText(confirms[type]);
        findViewByID(R.id.rlv_lableframe).setVisibility(type == 2 ? View.VISIBLE : View.GONE);
        EditTextUtil.setTypePhoneNumAddSpace(etPhone);
        etPhone.addTextChangedListener(textWatcher);
        etPwd.addTextChangedListener(textWatcher);

        saveRInfo = new SaveRegisterItem();
        cbvAggreement.setChecked(true);
        cbvAggreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setEnter();
            }
        });
    }

    private TextWatcherImpl textWatcher = new TextWatcherImpl() {
        @Override
        public void afterTextChanged(Editable s) {
            setEnter();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String editable = etPwd.getText().toString();
            String str = StringUtil.stringPassword(editable.toString());
            if (!editable.equals(str)) {
                etPwd.setText(str);
                // 设置新的光标所在位置
                etPwd.setSelection(str.length());
            }
        }
    };

    private void setEnter() {
        if (!cbvAggreement.isChecked()) {//协议(默认勾选了)
            btnConfirm.setEnabled(false);
            return;
        }
        if (etPhone.length() == 13 && etPhone.getText().charAt(0) == '1' && etPwd.length() > 3) {
            btnConfirm.setEnabled(true);
        } else {
            btnConfirm.setEnabled(false);
        }


    }


    @Override
    public int[] setClickIDs() {
        return new int[]{};
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_aggreement:
                break;
            case R.id.scb_confirm:
                String phone = etPhone.getTextTrim().replace(" ", "");
                String code = etPwd.getTextTrim();

                switch (type) {// 0-注册 1-登录 2-修改密码 3-绑定手机，4密码重置
                    case 0://注册手机
                        saveRInfo.phone = phone;
                        saveRInfo.phonecode = code;
                        saveRInfo.tinyhint = "0";
//                        AppLauncherUtils.startFragmentForRestult(mContext,
//                                RegisterInformationFragment.class, REQUEST_CODE_REGISTER, saveRInfo);
//                        break;
                    case 1://验证码登录
                        showDialogForLoading();
                        toLogin(phone, code);
                        break;
                    case 2://忘记密码
                        saveRInfo.phone = phone;
                        saveRInfo.phonecode = code;
                        saveRInfo.tinyhint = "0";
//                        AppLauncherUtils.startFragmentForRestult(mContext,
//                                ForgetPasswordFragment.class, REQUEST_CODE_FORGETPASSWORD,
//                                saveRInfo, 0);
                        break;
                    case 3://绑定手机
//                        if (secondtype == 1) {
//                            AppLauncherUtils.startFragmentForRestult(mContext,
//                                    SetLoginInfofirstFragment.class, REQUEST_INCOMPLETE_INFORMATION,
//                                    1);
//                        } else {
//
//                        }
                        break;
                    case 4://重置密码
                        saveRInfo.phone = phone;
                        saveRInfo.phonecode = code;
                        saveRInfo.tinyhint = "0";
                        break;

                }


                break;
            case R.id.scb_get:
                String phone1 = etPhone.getTextTrim();
                if (phone1.length() != 13) {
                    ToastUtils.show("请输入手机号码");
                    return;
                }
                createLooper();
                SpUtils.putString(SPConstants.File_cache, SPConstants.KEY_phone, phone1);
                showDialogForLoading();
                btnGet.setEnabled(false);
                NetBuilder.create(mContext)
                        .add2Post("mobile", phone1.replace(" ", ""))
                        .add2Post("t", (type == 4 ? 3 : type + 1))//t int  1-注册 2-登录 3-修改密码 4-绑定手机
                        .setMessage(IMessage.errorMessage)
                        .setFlag(type + 1)
                        .start("sms/send/", new NetCallbackImpl() {
                            @Override
                            public void onSuccess(NetEntity entity) throws Exception {
                                hideDialogForLoading();
                                ToastUtils.show("发送成功");
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

    private void toLogin(String phone, String code) {
        NetBuilder.create(mContext)
                .add2Post("account", phone)
                .add2Post("psword", code)
                .add2Post("t", "2")
                .setMessage(IMessage.allMessage)
                .start("user/login/", new NetCallbackImpl<LoginSuccItem>() {
                    @Override
                    public void onSuccess(NetEntity<LoginSuccItem> entity) throws Exception {
                        AppSaveData.getUserVInfo().setUserInfo(entity.getDataBean());

                    }

                    @Override
                    public void onError(NetEntity entity) throws Exception {
                        hideDialogForLoading();
                    }
                });

    }

    MyCountDownTimer countDownTimer;
    SaveRegisterItem saveRInfo;

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
                    btnGet.setText(leftSeconds + "S后重发");
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

    public final static int REQUEST_CODE_REGISTER = 100;
    public final static int REQUEST_CODE_FORGETPASSWORD = 101;
    public final static int REQUEST_INCOMPLETE_INFORMATION = 102;

    @Override
    public void onGetActivityResult(boolean isResultOK, int requestCode, Intent data) {
        super.onGetActivityResult(isResultOK, requestCode, data);
        if (requestCode == REQUEST_CODE_REGISTER || requestCode == REQUEST_CODE_FORGETPASSWORD) {
            if (isResultOK) {
                resultOKFinish();
            } else {
                saveRInfo = (SaveRegisterItem) data.getSerializableExtra(ID_NEW_PARAM1);
            }
        } else if (requestCode == REQUEST_INCOMPLETE_INFORMATION) {
            resultOKFinish();
        }

    }

    private void resultOKFinish() {
        KeyboardUtils.hideSoftKeyboard(mContext);
        mContext.setResult(Activity.RESULT_OK);
        mContext.finish();
        //发送登录成功的消息
        EventBus.getDefault().post(new EventItem.LoginEvent());
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_getcode;
    }

}
