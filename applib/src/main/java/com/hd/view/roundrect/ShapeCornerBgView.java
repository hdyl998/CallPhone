package com.hd.view.roundrect;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Checkable;

import com.hd.R;


/**
 * 圆角背景控件
 */
public class ShapeCornerBgView extends DrawableCenterTextView implements Checkable {
    int mBorderWidth = RoundRectConstants.shapeLineWidth;
    boolean isHasBorder = false;

    int mColorBorder;// 线条的颜色，默认与字的颜色相同
    int mColorBg;// 背景的颜色，默认是透明的
    int mRadius = RoundRectConstants.cornerRadius;

    int mColorText;

    int mColorBorderEnd = Color.TRANSPARENT;
    int mColorBgEnd = Color.TRANSPARENT;
    float mAlpha;

    private Rect rect = new Rect();// 方角

//    // 四个角落是否是全是圆角
//    boolean isTopLeftCorner = true;
//    boolean isBottomLeftCorner = true;
//    boolean isTopRightCorner = true;
//    boolean isBottomRightCorner = true;

    int mDisableColorBg;
    int mDisableColorText;
    int mDisableColorBorder;
    boolean isDisableHasBorder = false;
    int mDisableColorBorderEnd;
    int mDisableColorBgEnd;
    float mDisableAlpha;


    int mUnCheckedColorBg;
    int mUnCheckedColorText;
    int mUnCheckedColorBorder;
    boolean isUnCheckedHasBorder = false;
    int mUnCheckedColorBorderEnd ;
    int mUnCheckedColorBgEnd ;


    boolean isPressedStyle = false;
    int mColorTextPressed;
    int mColorBgPressed;
    //默认是选中状态
    boolean isChecked = true;
    Paint mPaint;//边线绘制图线

    public ShapeCornerBgView(Context context, AttributeSet attrs) {
        super(context, attrs);


        mUnCheckedColorText = mColorText = mDisableColorText = mColorBorder = getCurrentTextColor();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeCornerBgView);
        isHasBorder = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appBorder, isHasBorder);// 默认无边框
        mBorderWidth = mTypedArray.getDimensionPixelSize(R.styleable.ShapeCornerBgView_appBorderWidth, mBorderWidth);
        mRadius = mTypedArray.getDimensionPixelSize(R.styleable.ShapeCornerBgView_appRadius, mRadius);

        mColorBorder = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appBorderColor, mColorBorder);
        //初始化赋值
        mUnCheckedColorBorder = mDisableColorBorder = mColorBorder;

        mColorBg = isHasBorder ? Color.TRANSPARENT : Color.RED;

        mColorBg = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appBgColor, mColorBg);
        //初始化赋值
        mDisableColorBg = mUnCheckedColorBg = mColorBg;

        //渐变色
        mColorBgEnd = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appBgColorEnd, mColorBgEnd);
        mColorBorderEnd = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appBorderColorEnd, mColorBorderEnd);
        //初始化赋值
        mDisableColorBgEnd=mUnCheckedColorBgEnd=mColorBgEnd;
        mDisableColorBorderEnd=mUnCheckedColorBorderEnd=mColorBorderEnd;


        mDisableColorBg = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appDisableBgColor, mDisableColorBg);
        mDisableColorText = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appDisableTextColor, mDisableColorText);
        mDisableColorBorder = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appDisableBorderColor, mDisableColorBorder);
        isDisableHasBorder = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appDisableBorder, isDisableHasBorder);

        //渐变色,禁用时
        mDisableColorBgEnd = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appDisableBgColorEnd, mDisableColorBgEnd);
        mDisableColorBorderEnd = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appDisableBorderColorEnd, mDisableColorBorderEnd);
        mDisableAlpha = mTypedArray.getFloat(R.styleable.ShapeCornerBgView_appDisableAlpha, 1f);

        mUnCheckedColorBg = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appUnCheckedBgColor, mUnCheckedColorBg);
        mUnCheckedColorText = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appUnCheckedTextColor, mUnCheckedColorText);
        mUnCheckedColorBorder = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appUnCheckedBorderColor, mUnCheckedColorBorder);
        isUnCheckedHasBorder = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appUnCheckedBorder, isUnCheckedHasBorder);
        //渐变色
        mUnCheckedColorBgEnd = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appUnCheckedBgColorEnd, mUnCheckedColorBgEnd);
        mUnCheckedColorBorderEnd = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appUnCheckedBorderColorEnd, mUnCheckedColorBorderEnd);


        isChecked = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appChecked, isChecked);

        isPressedStyle = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appPressedStyle, isPressedStyle);

        mColorTextPressed = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appTextColorPressed, mColorTextPressed);
        mColorBgPressed = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appBgColorPressed, mColorBgPressed);
        mTypedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mAlpha = getAlpha();

//        shapeDrawable = new ShapeDrawable();

        //外部没设置值采用center为默认值
        if (this.getGravity() == (Gravity.TOP | Gravity.START)) {
            this.setGravity(Gravity.CENTER);// 全部居中显示
        }
        this.setIncludeFontPadding(false);//设置居中时要用到，不然会不能居中
        this.setChecked(isChecked);
        this.setEnabled(isEnabled());
    }

    //    ShapeDrawable shapeDrawable;
    RectF rectF = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        if (getWidth() == 0) // 没初始化完成不需要绘制
            return;
        int colorBG = mColorBg;
        boolean hasBorder = isHasBorder;
        int borderColor = mColorBorder;
        int colorBGEnd = mColorBgEnd;
        int colorBorderEnd = mColorBorderEnd;
        //禁用
        if (!isEnabled()) {
            colorBG = mDisableColorBg;
            hasBorder = isDisableHasBorder;
            borderColor = mDisableColorBorder;
            colorBGEnd = mDisableColorBgEnd;
            colorBorderEnd = mDisableColorBorderEnd;
            setAlpha(mDisableAlpha);
        } else {
            setAlpha(mAlpha);
            if (!isChecked()) {//无选中时,默认是选中
                colorBG = mUnCheckedColorBg;
                borderColor = mUnCheckedColorBorder;
                hasBorder = isUnCheckedHasBorder;
                colorBGEnd = mUnCheckedColorBgEnd;
                colorBorderEnd = mUnCheckedColorBorderEnd;
            } else if (isPressedStyle && isPressed) {
                colorBG = mColorBgPressed;
                hasBorder = false;
            }
        }

        // 先画背景
        if (colorBG != Color.TRANSPARENT) {// 透明就不用画了
            rectF.left = 0;
            rectF.top = 0;
            rectF.right = getWidth();
            rectF.bottom = getHeight();

            mPaint.setColor(colorBG);
            mPaint.setStyle(Paint.Style.FILL);

            if (colorBGEnd != Color.TRANSPARENT) {
                LinearGradient shader = new LinearGradient(0, 0, getWidth(), 0, new int[]{colorBG, colorBGEnd}, null, Shader.TileMode.CLAMP);
                mPaint.setShader(shader);
            } else {
                mPaint.setShader(null);
            }
            canvas.drawRoundRect(rectF, mRadius, mRadius, mPaint);
        }
        // 有边框
        if (hasBorder) {
            //// 内部矩形与外部矩形的距离
            int borderHalf = mBorderWidth / 2;

            rectF.left = borderHalf;
            rectF.top = borderHalf;
            rectF.right = getWidth() - borderHalf;
            rectF.bottom = getHeight() - borderHalf;

            mPaint.setStrokeWidth(mBorderWidth);
            mPaint.setColor(borderColor);
            mPaint.setStyle(Paint.Style.STROKE);

            if (colorBorderEnd != Color.TRANSPARENT) {
                LinearGradient shader = new LinearGradient(0, 0, getWidth(), 0, new int[]{borderColor, colorBorderEnd},null, Shader.TileMode.CLAMP);
                mPaint.setShader(shader);
            } else {
                mPaint.setShader(null);
            }
            canvas.drawRoundRect(rectF, mRadius, mRadius, mPaint);
        }
        super.onDraw(canvas);
    }

    boolean isPressed;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //不可用时不处理，按压样式不可用进，不处理
        if (!isPressedStyle || !isEnabled()) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                setTextColor(mColorTextPressed);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            default:
                isPressed = false;
                setTextColor(mColorText);
                invalidate();
                break;
        }
        return false;
    }


    //    //获得圆角的度数
//    private float[] getOutterRadii() {
//        float fRandis[] = {mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius};
//        return fRandis;
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rect.left = 0;
        rect.top = 0;
        rect.bottom = getHeight();
        rect.right = getWidth();
    }

    // 设置是否可用更改颜色
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setTextColor(enabled ? mColorText : mDisableColorText);
        invalidate();
    }

    // 设置是否可用更改颜色
    public void setEnabledNotChangeUI(boolean enabled) {
        super.setEnabled(enabled);
    }

    public void setColorBorder(int mColorBorder) {
        this.mColorBorder = mColorBorder;
        invalidate();
    }

    public void setColorBg(int mColorBg) {
        this.mColorBg = mColorBg;
        invalidate();
    }

//
//    public void setThemeColor(int color){
//        this.
//    }

    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    public void setChecked(boolean checked) {
        if (!isEnabled()) {
            return;
        }
        isChecked = checked;
        setTextColor(checked ? mColorText : mUnCheckedColorText);
        invalidate();
    }

    public boolean exchangeChecked() {
        toggle();
        return isChecked;
    }

}