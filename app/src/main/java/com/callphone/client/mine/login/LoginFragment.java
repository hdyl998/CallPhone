package com.callphone.client.mine.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.callphone.client.R;
import com.callphone.client.base.SPConstants;
import com.callphone.client.base.data.AppSaveData;
import com.callphone.client.main.bean.EventItem;
import com.callphone.client.utils.StringUtil;
import com.hd.base.fragment.IBaseTitleBarFragment;
import com.hd.base.interfaceImpl.TextWatcherImpl;
import com.hd.base.launch.AppLauncher;
import com.hd.base.launch.AppLauncherUtils;
import com.hd.cache.SpUtils;
import com.hd.net.NetBuilder;
import com.hd.net.NetCallbackImpl;
import com.hd.net.socket.NetEntity;
import com.hd.utils.EditTextUtil;
import com.hd.utils.bufferknife.MyBindView;
import com.hd.utils.bufferknife.MyBufferKnifeUtils;
import com.hd.utils.other.KeyboardUtils;
import com.hd.view.edit.EditTextWithDel;

import org.greenrobot.eventbus.EventBus;

/**
 * Note：None
 * Created by Liuguodong on 2018/12/29 15:07
 * E-Mail Address：986850427@qq.com
 */
public class LoginFragment extends IBaseTitleBarFragment {

    @MyBindView(R.id.etPhone)
    EditTextWithDel etPhone;

    @MyBindView(R.id.etPwd)
    EditTextWithDel etPwd;


    @MyBindView(value = R.id.btnConfirm, click = true)
    TextView btnConfirm;

    @MyBindView(value = R.id.iv_pwd_eye, click = true)
    ImageView ivPwdEye;

    boolean isHidePwd = true;

    @Override
    protected void initTitleBarView() {

        MyBufferKnifeUtils.inject(this);


//        getTitleBar().setRightOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AppLauncherUtils.startFragment(mContext, GetCodeFragment.class,1);
//            }
//        });
        EditTextUtil.setTypePhoneNumAddSpace(etPhone);
        String phone = SpUtils.getString(SPConstants.File_cache, SPConstants.KEY_phone);
        if (phone != null) {
            etPhone.setText(phone);
            etPhone.setSelection(etPhone.length());
        }
        etPhone.addTextChangedListener(textWatcher);
        etPwd.addTextChangedListener(textWatcher);
        etPwd.addTextChangedListener(pwdtextWatcher);
    }






    private TextWatcherImpl pwdtextWatcher=new TextWatcherImpl() {
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

    private TextWatcherImpl textWatcher=new TextWatcherImpl() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateConfirmBtnEnable();
        }
    };


    private void updateConfirmBtnEnable(){
        if (etPhone.length() == 13 && etPhone.getText().charAt(0) == '1' && etPwd.length() > 5) {
            btnConfirm.setEnabled(true);
        } else {
            btnConfirm.setEnabled(false);
        }
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



    @Override
    public int[] setClickIDs() {
        return new int[]{R.id.tv_register, R.id.tv_forget};
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pwd_eye:
                isHidePwd = !isHidePwd;
                initPwdInpter();
                break;
            case R.id.tv_forget://忘记密码
                AppLauncherUtils.startFragment(mContext, GetCodeFragment.class,GetCodeFragment.TYPE_FORGET_PWD);
                break;
            case R.id.tv_register://注册
                AppLauncherUtils.startFragment(mContext, GetCodeFragment.class,GetCodeFragment.TYPE_REGISTER);
                break;
            case R.id.btnConfirm:
                String phone = etPhone.getTextTrim().replace(" ", "");
                String pwd = etPwd.getTextTrim();
                showDialogForLoading();
                NetBuilder.create(mContext)
                        .add2Post("phone",phone)
                        .add2Post("password",pwd)
                        .start("login", new NetCallbackImpl<LoginSuccItem>() {
                            @Override
                            public void onSuccess(NetEntity<LoginSuccItem> entity) throws Exception {
                                SpUtils.putString(SPConstants.File_cache, SPConstants.KEY_phone, phone);
                                LoginSuccItem item=  entity.getDataBean();
                                item.phone=phone;
                                AppSaveData.getUserVInfo().setUserInfo(item);
                                resultOKFinish();
                            }

                            @Override
                            public void onError(NetEntity entity) throws Exception {
                                hideDialogForLoading();
                            }
                        });
                break;

        }
    }

    private void resultOKFinish() {
        hideDialogForLoadingImmediate();
        KeyboardUtils.hideSoftKeyboard(mContext);
        mContext.setResult(Activity.RESULT_OK);
        mContext.finish();
        //发送登录成功的消息
        EventBus.getDefault().post(new EventItem.LoginEvent());
    }

    private static final String TAG = "LoginFragment";

    @Override
    public int setBackgroundColor() {
        return R.color.white;
    }

    public static void launchForRestult(Context context, int resultCode) {
        AppLauncher.withFragment(context, LoginFragment.class)
                .launch(resultCode);
    }

    public static void launchWithAction(Context mContext, String loginSuccessAction) {
        AppLauncher.withFragment(mContext, LoginFragment.class)
                .setObjs(loginSuccessAction)
                .launch();
    }




    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }


}
