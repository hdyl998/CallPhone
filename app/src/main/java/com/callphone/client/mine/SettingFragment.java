//package com.callphone.client.mine;
//
//import android.view.View;
//
//import com.callphone.client.R;
//import com.callphone.client.about.AboutFragment;
//import com.callphone.client.main.mine.LoginManager;
//import com.hd.base.dialog.SimpleDialog;
//import com.hd.base.fragment.IBaseTitleBarFragment;
//import com.hd.net.NetBuilder;
//import com.hd.net.NetCallbackImpl;
//import com.hd.net.coderemind.IMessage;
//import com.hd.net.socket.NetEntity;
//
///**
// * 设置
// * Created by liugd on 2019/2/25.
// */
//
//public class SettingFragment extends IBaseTitleBarFragment {
//    @Override
//    protected void initTitleBarView() {
//
//    }
//
//    @Override
//    public int[] setClickIDs() {
//        return new int[]{R.id.tvAbout, R.id.tvLogout};
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tvAbout:
//                startFragment(AboutFragment.class);
//                break;
//            case R.id.tvLogout:
//                SimpleDialog.create(mContext)
//                        .setTvTitle("确定要退出登录？")
//                        .setBtnLeft("否")
//                        .setBtnRight("退出登录")
//                        .setOnClickListener(new SimpleDialog.SimpleDialogClick() {
//                            @Override
//                            public void onRightClick(SimpleDialog simpleDialog) {
//                                requestExit();
//                            }
//                        });
//                break;
//        }
//    }
//
//    private void requestExit() {
//        NetBuilder.create(mContext)
//                .setMessage(IMessage.allMessage)
//                .start("loginOut", new NetCallbackImpl() {
//            @Override
//            public void onSuccess(NetEntity entity) throws Exception {
//                LoginManager.logout();
//                hideDialogForLoadingImmediate();
//                mContext.finish();
//            }
//
//            @Override
//            public void onError(NetEntity entity) throws Exception {
//                hideDialogForLoading();
//            }
//        });
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.fragment_setting;
//    }
//}
