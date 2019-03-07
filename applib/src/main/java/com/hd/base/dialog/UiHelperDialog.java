package com.hd.base.dialog;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.hd.R;
import com.hd.utils.other.CalcTime;

/**
 * Note：None
 * Created by lgd on 2018/12/19 16:19
 * E-Mail Address：986850427@qq.com
 */
public class UiHelperDialog extends IBaseDialog {
    Activity activity;
    TextView textView;
    private final static String defaultString = "请稍后...";

    CalcTime calcTime;
    public UiHelperDialog(Context context) {
        super(context,R.style.lucencyDialog);
        this.activity= (Activity) context;
        setCanceledOnTouchOutside(false);
        textView = (TextView) findViewById(R.id.textView);
        calcTime=new CalcTime();
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_loading_dialog;
    }

    @Override
    protected int setWindowAnimation() {
        return R.style.dialogAnimFadeinFadeout;
    }
    /***
     * 判定ACTIVITY 是否为空
     *
     * @return
     */
    private boolean isValidContext() {
        if (activity.isFinishing()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 显示加载对话框
     */
    public void showDialogForLoading() {
        showDialogForLoading(defaultString);
    }

    /**
     * 显示加载对话框
     *
     * @param msg 对话框显示内容
     */
    public void showDialogForLoading(String msg) {
        textView.setText(msg);
        if (isValidContext()) {
            this.show();
        }
    }




    @Override
    public void show() {
        super.show();
        calcTime.recordStart();
    }

    /**
     * 关闭加载对话框,如果时间小于 TIEM_WAIT则多显示一会儿
     */
    public void hideDialog() {
        if (this.isShowing()&&isValidContext()) {
            long distance=calcTime.getTimeDistence();
            if(distance>=TIEM_WAIT){
                super.dismiss();
            }
            else {
                textView.postDelayed(()->superDismiss(),TIEM_WAIT - distance);
            }
        }
    }

    /***
     * 立即关闭对话框
     */
    public void hideDialogImmediate(){
        if (this.isShowing()&&isValidContext()) {
            super.dismiss();
        }
    }

    private void superDismiss(){
        super.dismiss();
    }

    private final static int TIEM_WAIT=300;

}
