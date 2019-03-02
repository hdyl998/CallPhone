package com.callphone.client.mine.login;

import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.callphone.client.R;
import com.hd.base.fragment.IBaseTitleBarFragment;
import com.hd.base.interfaceImpl.TextWatcherImpl;
import com.hd.net.NetBuilder;
import com.hd.net.NetCallbackImpl;
import com.hd.net.coderemind.IMessage;
import com.hd.net.socket.NetEntity;
import com.hd.utils.bufferknife.MyBindView;
import com.hd.utils.bufferknife.MyBufferKnifeUtils;
import com.hd.view.edit.EditTextWithDel;

/**
 * Created by liugd on 2019/2/28.
 */

public class ChangePwdFragment extends IBaseTitleBarFragment {
    @Override
    protected void initTitleBarView() {
        MyBufferKnifeUtils.inject(this);
        etPwd.addTextChangedListener(textWatcher);
        etPwd2.addTextChangedListener(textWatcher);
        etPwd.addPasswordFilter();
        etPwd2.addPasswordFilter();
    }

    TextWatcherImpl textWatcher=new TextWatcherImpl() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            btnConfirm.setEnabled(etPwd.length()>=LoginConstants.MIN_PASSWROD_LEN&&etPwd2.length()>=LoginConstants.MIN_PASSWROD_LEN);
        }
    };

    @MyBindView(value = R.id.iv_pwd_eye, click = true)
    ImageView ivPwdEye;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_change_pwd;
    }




    @MyBindView(R.id.etPwd)
    EditTextWithDel etPwd;

    @MyBindView(R.id.etPwd2)
    EditTextWithDel etPwd2;

    boolean isHidePwd = true;


    @MyBindView(value = R.id.btnConfirm,click = true)
    TextView btnConfirm;


    /***
     * 初始化密码输入器
     */
    private void initPwdInpter() {
        if (isHidePwd) {
            ivPwdEye.setImageResource(R.mipmap.my_login_eye_n);
            etPwd2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            ivPwdEye.setImageResource(R.mipmap.my_login_eye_o);
            etPwd2.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        etPwd2.setSelection(etPwd2.length());


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConfirm:
                changePwd(etPwd.getText().toString(),etPwd2.getText().toString());
                break;
            case R.id.iv_pwd_eye:
                isHidePwd = !isHidePwd;
                initPwdInpter();
                break;
        }
    }

    private void changePwd(String pwd,String pwd2){
        showDialogForLoading();
        btnConfirm.setEnabled(false);
        NetBuilder.create(mContext).add2Post("oldpassword",pwd)
                .add2Post("newpassword",pwd2)
                .setMessage(IMessage.allMessage)
                .start("updatePwd", new NetCallbackImpl() {
                    @Override
                    public void onSuccess(NetEntity entity) throws Exception {
                        hideDialogForLoadingImmediate();
                        mContext.finish();
                    }

                    @Override
                    public void onError(NetEntity entity) throws Exception {
                        hideDialogForLoading();
                        btnConfirm.setEnabled(true);
                    }
                });

    }
}
