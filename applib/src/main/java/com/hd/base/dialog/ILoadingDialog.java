package com.hd.base.dialog;

/**
 * Created by Administrator on 2018/1/12.
 */

public interface ILoadingDialog {

    void showDialogForLoading(String msg);

    void showDialogForLoading();

    void hideDialogForLoading();

    void hideDialogForLoadingImmediate();
}
