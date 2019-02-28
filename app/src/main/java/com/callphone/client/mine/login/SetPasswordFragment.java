//package com.callphone.client.mine.login;
//
//import android.app.Activity;
//import android.text.Editable;
//import android.text.InputType;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.callphone.client.R;
//import com.callphone.client.utils.StringUtil;
//import com.hd.base.fragment.IBaseTitleBarFragment;
//import com.hd.base.launch.AppLauncherUtils;
//import com.hd.net.NetBuilder;
//import com.hd.net.NetCallbackImpl;
//import com.hd.net.coderemind.IMessage;
//import com.hd.net.socket.NetEntity;
//import com.hd.utils.bufferknife.MyBindView;
//import com.hd.utils.bufferknife.MyBufferKnifeUtils;
//import com.hd.view.roundrect.ShapeCornerBgView;
//
//public class SetPasswordFragment extends IBaseTitleBarFragment {
//
//    SaveRegisterItem saveRInfo;
//
//    @MyBindView(value = R.id.tv_enter,click = true)
//    ShapeCornerBgView tvEnter;
//
//    @MyBindView(R.id.et_password)
//    EditText edPassWord;
//    @MyBindView(value = R.id.iv_passwordflag,click = true)
//    ImageView ivShowPassword;
//    @MyBindView(value = R.id.iv_deletepassword,click = true)
//    ImageView ivDeletePassword;
//    @MyBindView(R.id.tv_title)
//    TextView tvTitle;
//
//
//    public final static int TYPE_REGISTER_PWD=
//
//
//
//
//    @Override
//    protected void initTitleBarView() {
//        MyBufferKnifeUtils.inject(this);
//        getTitleBar().getTvLeft().setText(" 上一步");
//        saveRInfo = (SaveRegisterItem) mContext.getIntent().getSerializableExtra(ID_NEW_PARAM1);
//        int type = mContext.getIntent().getIntExtra(ID_NEW_PARAM2, 0);
//        if (type == 0) {
//            tvTitle.setText("忘记密码");
//        } else {
//            tvTitle.setText("重置密码");
//        }
//
//        edPassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);//设置显示状态
//
//        edPassWord.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//
//                String editable = edPassWord.getText().toString();
//                String str = StringUtil.stringPassword(editable.toString());
//                if (!editable.equals(str)) {
//                    edPassWord.setText(str);
//                    // 设置新的光标所在位置
//                    edPassWord.setSelection(str.length());
//                }
//                if (edPassWord.getEditableText().toString().trim()
//                        .length() != 0
//                        && edPassWord.isFocused()) {
//                    ivDeletePassword.setVisibility(View.VISIBLE);
//                } else {
//                    ivDeletePassword.setVisibility(View.GONE);
//                }
//                tvEnter.setEnabled(edPassWord.getEditableText().toString().trim()
//                        .length() >5);
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//    }
//
//
//
//    private boolean isShowPassFlag = true;// 默认是隐藏密码的
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.iv_passwordflag:
//                isShowPassFlag = isShowPassFlag == true ? false : true;
//                if (isShowPassFlag) {
//                    ivShowPassword
//                            .setImageResource(R.mipmap.my_login_eye_o);
//                    edPassWord.setInputType(InputType.TYPE_CLASS_TEXT
//                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                } else {
//                    ivShowPassword
//                            .setImageResource(R.mipmap.my_login_eye_n);
//                    edPassWord.setInputType(InputType.TYPE_CLASS_TEXT);
//                }
//                break;
//            case R.id.iv_deletepassword:
//                edPassWord.setText("");
//                break;
//
//            case R.id.tv_enter:// 提交修改密码
//                tvEnter.setEnabled(false);
//                tvEnter.setText("提交中...");
//                findPassword( edPassWord.getEditableText()
//                        .toString().trim());
//                break;
//
//        }
//    }
//    private void findPassword( String password) {
//        NetBuilder.create(mContext)
//                .add2Post("account",saveRInfo.phone)
//                .add2Post("psword",password)
//                .add2Post("code",saveRInfo.phonecode)
//                .setMessage(IMessage.errorMessage)
//                .start("user/uppasswd/", new NetCallbackImpl() {
//            @Override
//            public void onSuccess(NetEntity entity) throws Exception {
//                super.onSuccess(entity);
//                mContext.setResult(Activity.RESULT_OK,
//                        AppLauncherUtils.getIntentOfParams(saveRInfo));
//                mContext.finish();
//            }
//        });
//    }
//
//
//    @Override
//    protected int getBodyId() {
//        return R.layout.fragment_forgetpassword;
//    }
//}
