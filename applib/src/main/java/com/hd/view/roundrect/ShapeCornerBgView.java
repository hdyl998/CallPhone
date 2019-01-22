package com.hd.view.roundrect;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Checkable;

import com.hd.R;
import com.hd.utils.DensityUtils;


/**
 * 圆角背景控件
 */
public class ShapeCornerBgView extends DrawableCenterTextView implements Checkable {
    int mBorderWidth = 2;// 默认1dimpx
    boolean isHasBorder = false;

    int mColorBorder;// 线条的颜色，默认与字的颜色相同
    int mColorBg;// 背景的颜色，默认是透明的
    int mRadius = 5;// 默认5

    int mColorText;

    private Rect rect = new Rect();// 方角

//    // 四个角落是否是全是圆角
//    boolean isTopLeftCorner = true;
//    boolean isBottomLeftCorner = true;
//    boolean isTopRightCorner = true;
//    boolean isBottomRightCorner = true;

    int mColorBgEnableFalse;
    int mColorTextEnableFalse;
    int mColorBorderEnableFalse;
    boolean isHasBorderEnableFalse = false;


    int mColorBgUnChecked;
    int mColorTextUnchecked;
    int mColorBorderUnchecked;
    boolean isHasBorderUnchecked = false;


    boolean isPressedStyle = false;
    int mColorTextPressed;
    int mColorBgPressed;
    //默认是选中状态
    boolean isChecked = true;
    Paint mPaint;//边线绘制图线

    public ShapeCornerBgView(Context context, AttributeSet attrs) {
        super(context, attrs);

//        mBorderWidth = Tools.getDimenPx(mBorderWidth);
        mRadius = DensityUtils.getDimenPx(mRadius);

        mColorTextUnchecked=   mColorText = mColorTextEnableFalse = mColorBorder = getCurrentTextColor();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeCornerBgView);
        isHasBorder = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appBorder, isHasBorder);// 默认无边框
        mBorderWidth = mTypedArray.getDimensionPixelSize(R.styleable.ShapeCornerBgView_appBorderWidth, mBorderWidth);
        mRadius = mTypedArray.getDimensionPixelSize(R.styleable.ShapeCornerBgView_appRadius, mRadius);

        mColorBorder = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appBorderColor, mColorBorder);

        mColorBg = isHasBorder ? Color.TRANSPARENT : Color.RED;

        mColorBg = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appBgColor, mColorBg);
        // 四个角落是否全是圆角,默认全是真的
//        isTopLeftCorner = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appTopLeftCorner, isTopLeftCorner);
//        isBottomLeftCorner = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appBottomLeftCorner, isBottomLeftCorner);
//        isTopRightCorner = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appTopRightCorner, isTopRightCorner);
//        isBottomRightCorner = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appBottomRightCorner, isBottomRightCorner);

        mColorBgEnableFalse = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appEnableFalseBgColor, mColorBg);
        mColorTextEnableFalse = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appEnableFalseTextColor, mColorTextEnableFalse);
        mColorBorderEnableFalse = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appEnableFalseBorderColor, mColorBorder);
        isHasBorderEnableFalse = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appEnableFalseBorder, isHasBorderEnableFalse);

        mColorBgUnChecked = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appUnCheckedBgColor, mColorBg);
        mColorTextUnchecked = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appUnCheckedTextColor, mColorTextUnchecked);
        mColorBorderUnchecked = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appUnCheckedBorderColor, mColorBorder);
        isHasBorderUnchecked = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appUnCheckedBorder, isHasBorderUnchecked);

        isChecked = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appChecked, isChecked);

        isPressedStyle = mTypedArray.getBoolean(R.styleable.ShapeCornerBgView_appPressedStyle, isPressedStyle);

        mColorTextPressed = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appPressedTextColor, mColorTextPressed);
        mColorBgPressed = mTypedArray.getColor(R.styleable.ShapeCornerBgView_appPressedBgColor, mColorBgPressed);
        mTypedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

//        shapeDrawable = new ShapeDrawable();

        //外部没设置值采用center为默认值
        if(this.getGravity()==(Gravity.TOP | Gravity.START)){
            this.setGravity(Gravity.CENTER);// 全部居中显示
        }
        this.setIncludeFontPadding(false);//设置居中时要用到，不然会不能居中
        this.setChecked(isChecked);
        this.setEnabled(isEnabled());
    }

//    ShapeDrawable shapeDrawable;

    @Override
    protected void onDraw(Canvas canvas) {
        if (getWidth() == 0) // 没初始化完成不需要绘制
            return;
        int colorBG = mColorBg;
        boolean hasBorder = isHasBorder;
        int borderColor = mColorBorder;
        //禁用
        if (isEnabled() == false) {
            colorBG = mColorBgEnableFalse;
            hasBorder = isHasBorderEnableFalse;
            borderColor = mColorBorderEnableFalse;
        } else {
            if (isChecked() == false) {//无选中时,默认是选中
                colorBG = mColorBgUnChecked;
                borderColor = mColorBorderUnchecked;
                hasBorder = isHasBorderUnchecked;
            } else if (isPressedStyle && isPressed) {
                colorBG = mColorBgPressed;
                hasBorder = false;
            }
        }

        // 先画背景
        if (colorBG != Color.TRANSPARENT) {// 透明就不用画了
            RectF rectF = new RectF(0, 0, getWidth(), getHeight());
            mPaint.setColor(colorBG);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(rectF, mRadius, mRadius, mPaint);
        }
        // 有边框
        if (hasBorder) {
            //// 内部矩形与外部矩形的距离
            int borderHalf = mBorderWidth / 2;
            RectF rectF = new RectF(borderHalf, borderHalf, getWidth() - borderHalf, getHeight() - borderHalf);
            mPaint.setStrokeWidth(mBorderWidth);
            mPaint.setColor(borderColor);
            mPaint.setStyle(Paint.Style.STROKE);
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
        setTextColor(enabled ? mColorText : mColorTextEnableFalse);
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
        setTextColor(checked ? mColorText : mColorTextUnchecked);
        invalidate();
    }

    public boolean exchangeChecked() {
        toggle();
        return isChecked;
    }

}