package com.hd.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;


import com.hd.R;
import com.hd.base.IBaseFragment;
import com.hd.base.theme.StatusBarColorUtils;
import com.hd.utils.DensityUtils;
import com.hd.utils.TextViewUtils;
import com.hd.utils.ViewUtils;
import com.hd.utils.device.AppDeviceInfo;
import com.hd.utils.other.KeyboardUtils;

/**
 * Created by liugd on 2017/3/20.
 */

public class TitleBar extends LinearLayout implements View.OnClickListener {

    public final static int ID_LEFT = R.id.tvLeft;
    public final static int ID_TITLE = R.id.tvTitle;
    public final static int ID_RIGHT = R.id.tvRight;
    public final static int ID_RIGHT_SECOND = R.id.ivRightSecond;


    public TitleBar(Context context) {
        this(context,null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.titleBarStyle);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs,defStyleAttr);
    }



    TextView tvLeft, tvTitle, tvRight;
    ImageView ivRightSecond;//右上角 次级菜单

    TextView leftNum, rightNum;


    /***
     * 初始化设置,如果是首页的tab则隐藏返回键
     * @param fragment
     */
    public void initFragmentSetting(IBaseFragment fragment) {
        if (fragment.isHomeTab()) {
            tvLeft.setVisibility(View.GONE);
        }
    }


    /***
     * 设置成白色的新主题
     */
    public TitleBar setLightStyle() {
        int color = getResources().getColor(R.color.colorWhiteStyleTextColor);
//        ic_back_blue4_0
        setLeftImageResource(R.mipmap.ic_back_black);
        tvTitle.setTextColor(color);
        tvLeft.setTextColor(color);
        tvRight.setTextColor(color);
        this.setBackgroundResource(R.color.white);
        return this;
    }


//    public TitleBar setStyle(@TitleBarStyle int styleType) {
//        switch (styleType) {
//            case STYLE_LIGHT:
//                setLightStyle();
//                break;
//            case STYLE_DARK:
//                setDarkStyle();
//                break;
//            case STYLE_NONE:
//                break;
//        }
//        return this;
//    }


    /***
     * 设置成黑色的新主题
     */
    public TitleBar setDarkStyle() {
        int color = getResources().getColor(R.color.white);
//        ic_back_blue4_0
        setLeftImageResource(R.mipmap.ic_back_white);
        tvTitle.setTextColor(color);
        tvLeft.setTextColor(color);
        tvRight.setTextColor(color);
        this.setBackgroundResource(R.drawable.default_dark_bg);
        viewLine.setVisibility(View.GONE);
        makeActivityStatusBarDarkStyle();
        return this;
    }


    /***
     * 设置成黑色的新主题
     */
    public TitleBar setDarkStyleNoBg() {
        int color = getResources().getColor(R.color.white);
//        ic_back_blue4_0
        setLeftImageResource(R.mipmap.ic_back_white);
        tvTitle.setTextColor(color);
        tvLeft.setTextColor(color);
        tvRight.setTextColor(color);
        this.setBackgroundResource(R.color.transparent);
        viewLine.setVisibility(View.GONE);
        makeActivityStatusBarDarkStyle();
        return this;
    }

    /***
     * 添加一个布局在titleBar下面
     *
     * @param layoutID
     */
    public TitleBar addBodyView(@LayoutRes int layoutID) {
        ViewUtils.getInflateView(this, layoutID, true);
        return this;
    }

//    /***
//     * 在titlebar下面添加一个布局，使背景为一致
//     *
//     * @param layoutID
//     */
//
//    public void addBodyViewWithCommonBg(@LayoutRes int layoutID) {
//        setTitleBarBackgroundResource(R.color._00000000);
//        addBodyView(layoutID);
//        setBackgroundResource(R.drawable.bg_002);
//    }


    public TextView getLeftNum() {
        return leftNum;
    }

    public TextView getRightNum() {
        return rightNum;
    }


    public TitleBar setLeftNum(int num) {
        if (num == 0) {
            leftNum.setVisibility(View.GONE);
        } else {
            leftNum.setVisibility(View.VISIBLE);
            leftNum.setText(String.valueOf(num));
        }
        return this;
    }

    public TitleBar setRightNum(int num) {
        if (num == 0) {
            rightNum.setVisibility(View.GONE);
        } else {
            rightNum.setVisibility(View.VISIBLE);
            rightNum.setText(String.valueOf(num));
        }
        return this;
    }

    public TitleBar setLeftNum(String text) {
        if (TextUtils.isEmpty(text)) {
            leftNum.setVisibility(View.GONE);
        } else {
            leftNum.setText(text);
            leftNum.setVisibility(View.VISIBLE);
        }
        return this;
    }


    public TitleBar setRightNum(String text) {
        if (TextUtils.isEmpty(text)) {
            rightNum.setVisibility(View.GONE);
        } else {
            rightNum.setText(text);
            rightNum.setVisibility(View.VISIBLE);
        }
        return this;
    }


    View viewLine;

    //    //定义范围
//    @IntDef({STYLE_LIGHT, STYLE_DARK, STYLE_NONE})
//    @Retention(RetentionPolicy.SOURCE)
//    public @interface TitleBarStyle {
//    }


    public View getBottomLine() {
        return viewLine;
    }

    public TitleBar setBottomLineVisiable(boolean isVisiable) {
        viewLine.setVisibility(isVisiable ? View.VISIBLE : View.GONE);
        return this;
    }

    private void initView(Context context, AttributeSet attrs,int defStyleAttr) {
        setOrientation(VERTICAL);
        addBodyView(R.layout.view_titlelayout);
        this.setBackgroundResource(R.color.white);

        addBodyView(R.layout.view_line_1_deep);
        viewLine = findViewById(R.id.view_line);
        this.setFitsSystemWindows(true);
        tvLeft = findViewById(R.id.tvLeft);
        tvTitle = findViewById(R.id.tvTitle);
        tvRight = findViewById(R.id.tvRight);
        ivRightSecond = findViewById(R.id.ivRightSecond);


        leftNum = findViewById(R.id.tvLeftNum);
        rightNum = findViewById(R.id.tvRightNum);

        leftNum.setVisibility(View.GONE);
        rightNum.setVisibility(View.GONE);

        tvLeft.setOnClickListener(this);
        tvRight.setVisibility(View.GONE);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TitleBar,defStyleAttr,0);
        if (attributes != null) {
            //处理titleBar背景色
            int titleBarBackGround = attributes.getResourceId(R.styleable.TitleBar_tbBackground, -1);
            if (titleBarBackGround != -1)
                this.setBackgroundResource(titleBarBackGround);
            //先处理左边按钮
            //获取是否要显示左边按钮
            boolean leftButtonVisible = attributes.getBoolean(R.styleable.TitleBar_tbLeftVisiable, true);
            if (leftButtonVisible) {
                tvLeft.setVisibility(View.VISIBLE);
            } else {
                tvLeft.setVisibility(View.INVISIBLE);
            }
            //设置左边按钮的文字
            String leftButtonText = attributes.getString(R.styleable.TitleBar_tbLeftText);
            if (!TextUtils.isEmpty(leftButtonText)) {
                setLeftText(leftButtonText);
            }
            //设置左边按钮的图片
            int leftButtonDrawableID = attributes.getResourceId(R.styleable.TitleBar_tbLeftImage, -1);
            if (leftButtonDrawableID != -1) {
                TextViewUtils.setDrawable(this.tvLeft, leftButtonDrawableID, TextViewUtils.DIRECTION_LEFT);
            }
            //设置左边按钮文字颜色
            int leftButtonTextColor = attributes.getColor(R.styleable.TitleBar_tbLeftTextColor, 0);
            if (leftButtonTextColor != 0)
                tvLeft.setTextColor(leftButtonTextColor);


            //处理标题
            //如果不是图片标题 则获取文字标题
            String titleText = attributes.getString(R.styleable.TitleBar_tbTitle);
            tvTitle.setText(titleText);

            //获取标题显示颜色
            int titleTextColor = attributes.getColor(R.styleable.TitleBar_tbTitleColor, 0);
            if (titleTextColor != 0)
                tvTitle.setTextColor(titleTextColor);

            //设置右边按钮的文字
            String rightButtonText = attributes.getString(R.styleable.TitleBar_tbRightText);
            if (!TextUtils.isEmpty(rightButtonText)) {
                tvRight.setText(rightButtonText);
                tvRight.setVisibility(View.VISIBLE);
            }
            //设置右边图片icon 这里是二选一 要么只能是文字 要么只能是图片
            int rightButtonDrawable = attributes.getResourceId(R.styleable.TitleBar_tbRightImage, -1);
            if (rightButtonDrawable != -1) {
                TextViewUtils.setDrawable(this.tvRight, rightButtonDrawable, TextViewUtils.DIRECTION_LEFT);
                tvRight.setVisibility(View.VISIBLE);
            }

            //默认可见
            //获取是否要显示右边按钮
            boolean rightButtonVisible = attributes.getBoolean(R.styleable.TitleBar_tbRightVisiable, true);
            if (rightButtonVisible) {
                tvRight.setVisibility(View.VISIBLE);
            } else {
                tvRight.setVisibility(View.INVISIBLE);
            }
            //设置右边按钮文字颜色
            int rightButtonTextColor = attributes.getColor(R.styleable.TitleBar_tbRightTextColor, 0);
            if (rightButtonTextColor != 0)
                tvRight.setTextColor(rightButtonTextColor);
            int rightSecondID = attributes.getResourceId(R.styleable.TitleBar_tbRightSecondImage, -1);
            if (rightSecondID != -1) {
                ivRightSecond.setImageResource(rightSecondID);
                ivRightSecond.setVisibility(View.VISIBLE);
            }
            boolean isShowBottomLine = attributes.getBoolean(R.styleable.TitleBar_tbBottomLine, true);
            viewLine.setVisibility(isShowBottomLine ? View.VISIBLE : View.GONE);//白色设置颜色可见

            //获得样式
            isDarkStatusBar = attributes.getBoolean(R.styleable.TitleBar_tbDarkStatusBar, false);
            if (isDarkStatusBar) {
                makeActivityStatusBarDarkStyle();
            }
            attributes.recycle();
        }
    }

    public boolean isDarkStatusBar() {
        return isDarkStatusBar;
    }

    boolean isDarkStatusBar = false;

    /***
     * 使titileBar对应的Activity变成深色主题
     */
    private void makeActivityStatusBarDarkStyle() {
        if(getContext() instanceof Activity) {
            StatusBarColorUtils.setDarkStyle((Activity) getContext());
        }
    }


    public TitleBar setTitleText(CharSequence ch) {
        this.tvTitle.setText(ch);
        return this;
    }

    public TitleBar setTitleText(@StringRes int id) {
        this.tvTitle.setText(id);
        return this;
    }

    public TitleBar setTitleText(CharSequence ch, OnClickListener onClickListener) {
        this.setTitleText(ch).setTitleOnClickListener(onClickListener);
        return this;
    }

    /***
     * 设置标题右边的drawable
     *
     * @param resID
     * @param pxPadding
     * @return
     */
    public TitleBar setTitleRightDrawable(@DrawableRes int resID, int pxPadding) {
        TextViewUtils.setDrawable(this.tvTitle, resID, TextViewUtils.DIRECTION_RIGHT);
        this.tvTitle.setCompoundDrawablePadding(pxPadding);
        return this;
    }

    /***
     * 设置右边的文本
     *
     * @param ch
     * @return
     */
    public TitleBar setRightText(CharSequence ch) {
        this.tvRight.setText(ch);
        this.tvRight.setCompoundDrawables(null, null, null, null);
        this.tvRight.setVisibility(View.VISIBLE);
        return this;
    }

    /***
     * 设置左边的文本
     *
     * @param ch
     * @return
     */
    public TitleBar setLeftText(CharSequence ch) {
        this.tvLeft.setText(ch);
        this.tvLeft.setCompoundDrawables(null, null, null, null);
        return this;
    }


    public TitleBar setLeftText(CharSequence ch, View.OnClickListener onClickListener) {
        return setLeftText(ch).setLeftOnClickListener(onClickListener);
    }

    /***
     * 设置background的资源ID
     *
     * @param id
     */
    public TitleBar setTitleBarBackgroundResource(@DrawableRes int id) {
        this.setBackgroundResource(id);
        return this;
    }


    /***
     * 设置左边的
     *
     * @param resID
     * @return
     */
    public TitleBar setLeftImageResource(@DrawableRes int resID) {
        TextViewUtils.setDrawable(this.tvLeft, resID, TextViewUtils.DIRECTION_LEFT);
        this.tvLeft.setText(null);
        return this;
    }

    public TitleBar setRightImageResource(@DrawableRes int resID) {
        TextViewUtils.setDrawable(this.tvRight, resID, TextViewUtils.DIRECTION_LEFT);
        this.tvRight.setText(null);
        this.tvRight.setVisibility(View.VISIBLE);
        return this;
    }

    /***
     * 设置标题右边的drawable
     *
     * @param resID
     * @return
     */
    public TitleBar setRightTextAndImageResource(CharSequence text, @DrawableRes int resID,
                                                 @TextViewUtils.TextDrawableDirection int direction) {
        TextViewUtils.setDrawable(this.tvRight, resID, direction);
        this.tvRight.setCompoundDrawablePadding(DensityUtils.getDimenPx(6));
        this.tvRight.setText(text);
        this.tvRight.setVisibility(View.VISIBLE);
        return this;
    }


    public TitleBar setRightImageResource(@DrawableRes int resID, View.OnClickListener onClickListener) {
        this.setRightImageResource(resID);
        this.tvRight.setOnClickListener(onClickListener);
        return this;
    }

    public TitleBar setTitleAlpha(float alpha) {
        this.tvTitle.setAlpha(alpha);
        return this;
    }


    public ImageView getRightSecondImageView() {
        return ivRightSecond;
    }

    /***
     * 设置右边第二个按钮的图片资源
     * @param resID
     * @return
     */
    public TitleBar setRightSecondImageResource(@DrawableRes int resID) {
        return setRightSecondImageResource(resID, null);
    }

    /***
     * 设置右边第二个按钮的图片资源和事件
     * @param resID 资源ID
     * @param clickListener 事件
     * @return
     */
    public TitleBar setRightSecondImageResource(@DrawableRes int resID, View.OnClickListener clickListener) {
        ivRightSecond.setImageResource(resID);
        ivRightSecond.setVisibility(View.VISIBLE);
        ivRightSecond.setOnClickListener(clickListener);
        return this;
    }

    public TitleBar setRightSecondOnClickListener(View.OnClickListener onClickListener) {
        this.getRightSecondImageView().setOnClickListener(onClickListener);
        return this;
    }

    /***
     * 设置右边的文本
     *
     * @param ch
     * @return
     */
    public TitleBar setRightText(CharSequence ch, View.OnClickListener onClickListener) {
        this.setRightText(ch);
        this.tvRight.setOnClickListener(onClickListener);
        return this;
    }

    public TitleBar setLeftOnClickListener(View.OnClickListener onClickListener) {
        this.getTvLeft().setOnClickListener(onClickListener);
        return this;
    }

    public TitleBar setRightOnClickListener(View.OnClickListener onClickListener) {
        this.getTvRight().setOnClickListener(onClickListener);
        return this;
    }

    public TitleBar setTitleOnClickListener(View.OnClickListener onClickListener) {
        this.getTvTitle().setOnClickListener(onClickListener);
        return this;
    }

    /***
     * 在顶部添加一个纯色的状态栏，少量地方用到
     */
    public void addColorStatusBar() {
        View view;
        addView(view = new Space(getContext()));
        view.getLayoutParams().height = AppDeviceInfo.mStatusBarHeight;
    }


    public TextView getTvLeft() {
        return tvLeft;
    }

    public TextView getTvRight() {
        return tvRight;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == ID_LEFT) {
            Activity activity = (Activity) getContext();
            activity.finish();//结束掉
            KeyboardUtils.hideSoftKeyboard(activity);
        }
    }

    /***
     * 设置左边的
     *
     * @param visibility
     * @return
     */
    public TitleBar setLeftVisibility(int visibility) {
        this.tvLeft.setVisibility(visibility);
        return this;
    }

    public TitleBar setCenterVisibility(int visibility) {
        this.tvTitle.setVisibility(visibility);
        return this;
    }

    public TitleBar setRightVisibility(int visibility) {
        this.tvRight.setVisibility(visibility);
        return this;
    }
}
