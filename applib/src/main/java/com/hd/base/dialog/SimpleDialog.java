package com.hd.base.dialog;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hd.R;
import com.hd.utils.ViewUtils;


/**
 * 两个按钮的对话框
 * Created by liugd on 2017/3/22.
 */

public class SimpleDialog extends IBaseDialog {
    TextView tvTitle, tvContent;

    View viewSeprate;//分割线

    TextView btnLeft, btnRight;

    FrameLayout frameLayout;

    public SimpleDialog(Context context) {
        super(context);
        initView();
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            int id=v.getId();
            if(id==R.id.tv_cancle){
                onClickListener.onLeftClick(this);
                onClickListener.onSingleClick(this);
            }
            else if(id==R.id.tv_enter){
                onClickListener.onRightClick(this);
            }
        }
        dismiss();
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_dialog_alert;
    }

    public void initView() {
        tvTitle = findViewByID(R.id.tv_dialogtitle);
        tvContent = findViewByID(R.id.tv_dialogcontent);

        btnLeft = findViewByID(R.id.tv_cancle);
        btnRight = findViewByID(R.id.tv_enter);
        frameLayout = findViewByID(R.id.ll_content);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        //默认隐藏
        tvTitle.setVisibility(View.GONE);
        tvContent.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);

        viewSeprate = findViewByID(R.id.space);
        ViewUtils.setWindowDialogWidth(getWindow());
        show();
    }

    public static SimpleDialog create(Context context) {
        return new SimpleDialog(context);
    }

    public SimpleDialog setTvTitle(CharSequence tvTitle) {
        this.tvTitle.setText(tvTitle);
        this.tvTitle.setVisibility(View.VISIBLE);
        return this;
    }

    public SimpleDialog setTvContent(CharSequence tvContent) {
        this.tvContent.setVisibility(View.VISIBLE);
        this.tvContent.setText(tvContent);
        return this;
    }

    public SimpleDialog setBtnLeft(CharSequence ch) {
        this.btnLeft.setText(ch);
        return this;
    }

    public SimpleDialog setTvTitleColor(int color){
        this.tvTitle.setTextColor(color);
        return this;
    }
    public SimpleDialog setBtnLeftTextColor(int color) {
        this.btnLeft.setTextColor(color);
        return this;
    }

    public SimpleDialog setBtnRightTextColor(int color) {
        this.btnRight.setTextColor(color);
        return this;
    }

    public SimpleDialog defaultBtnText() {
        return setBtnLeft("取消").setBtnRight("确定");
    }


    /***
     * 设置对话框的内容VIEW
     * @param view
     */
    public SimpleDialog setBodyView(View view) {
        frameLayout.setVisibility(View.VISIBLE);
        frameLayout.addView(view);
        viewSeprate.setVisibility(View.GONE);
        return this;
    }

    public FrameLayout getFrameLayout() {
        return frameLayout;
    }

    public SimpleDialog setBodyView(@LayoutRes int layoutId) {
        return setBodyView(ViewUtils.getInflateView(mContext, layoutId));
    }

    /***
     * 显示一个按钮
     *
     * @return
     */
    public SimpleDialog setSingleButton(CharSequence ch) {
        getBtnLeft().setVisibility(View.GONE);
        getBtnRight().setText(ch);
        findViewByID(R.id.line1).setVisibility(View.GONE);
        return this;
    }


    public SimpleDialog showSingleInfo(CharSequence ch) {
        setTvTitle(ch).setSingleButton("确定");
        return this;
    }

    public SimpleDialog showSingleInfo(CharSequence ch, SimpleDialogClick onClickListener) {
        setTvTitle(ch).setSingleButton("确定").setOnClickListener(onClickListener);
        return this;
    }

    /***
     * 获取单个button
     *
     * @return
     */
    public TextView getSingleButton() {
        return getBtnLeft();
    }

    /***
     * 着重按钮的颜色
     * @return
     */
    public SimpleDialog stressSingleButtonColor() {
        setSingleTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        return this;
    }

    public SimpleDialog setSingleTextColor(int color) {
        getSingleButton().setTextColor(color);
        return this;
    }


    public SimpleDialog setBtnRight(CharSequence ch) {
        this.btnRight.setText(ch);
        return this;
    }

    SimpleDialogClick onClickListener;

    public SimpleDialog setOnClickListener(SimpleDialogClick onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }


    public SimpleDialog setTvContentGravity(int gravity) {
        getTvContent().setGravity(gravity);
        return this;
    }


    public SimpleDialog setCancelableDialog(boolean flag) {
        super.setCancelable(flag);
        return this;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getBtnRight() {
        return btnRight;
    }


    public TextView getTvContent() {
        return tvContent;
    }

    public TextView getBtnLeft() {
        return btnLeft;
    }


    public abstract static class SimpleDialogClick {

        public void onLeftClick(SimpleDialog simpleDialog) {

        }

        public void onRightClick(SimpleDialog simpleDialog) {

        }

        public void onSingleClick(SimpleDialog simpleDialog) {

        }
    }

}
