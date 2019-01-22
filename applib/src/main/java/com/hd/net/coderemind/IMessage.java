package com.hd.net.coderemind;

import com.hd.net.socket.NetEntity;
import com.hd.utils.toast.ToastUtils;

/**
 * 网络toast提示,当网络回调callBack为空时,不作任何提示
 * Created by liugd on 2017/3/15.
 */

public interface IMessage {

    IMessage errorMessage = new ErrorMessage();
    IMessage allMessage = new AllMessage();
    IMessage successMessage = new SuccessMessage();
    IMessage noMessage = new NoMessage();

    boolean isShowMsg(NetEntity netEntity);


    default void showMessage(NetEntity netEntity) {
        if (isShowMsg(netEntity)) {
            ToastUtils.show(netEntity.getCodeString());
        }
    }




//    default void showMsgImpl(NetEntity netEntity) {
//
////        if (netEntity.getContext() instanceof Activity) {
////            ToastUtils.showSnackMessage(netEntity.getContext(), netEntity.msg);
////        } else {
////            ToastUtils.show(netEntity.msg);
////        }
//    }

}
