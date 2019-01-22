package com.hd.view.roundrect;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hd.R;
import com.hd.base.inter.IPagerAdapterTitle;
import com.hd.base.inter.IViewPagerIndicator;
import com.hd.base.maininterface.IComCallBacks;
import com.hd.utils.DensityUtils;

/***
 * 圆角条形菜单
 */
public class ShapeCornerSelectView extends LinearLayout implements OnClickListener ,IViewPagerIndicator {

    Paint mPaint;


    // 可定义自定义的


    private int textColorSelect = Color.WHITE;// 选中的字体的颜色

    private int textSize = 22;// 字体大小

    private int bgColorSelect = Color.RED;// 选中的背景颜色

    private int textColor = bgColorSelect;// 字体颜色

    private int bgColor = Color.TRANSPARENT;// 没选中的颜色是透明的

    private int mRadius = 5;// 圆角矩形半径

    private int borderColor = bgColorSelect;// 边框的颜色

    private int borderWidth = 2;// 边框的宽度

    private boolean isDivider = false;// 是否有分割线

    private int dividerColor = bgColorSelect;// 分割线的颜色

    private boolean isBorder = true;// 是否有边框,默认有

    private RectF rectf = new RectF();

    public ShapeCornerSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);// 水平条子
        setWillNotDraw(false);// 解决linearlayout不绘制问题

        // 转换单位
        textSize = DensityUtils.getDimenPxForText(textSize);
        mRadius = DensityUtils.getDimenPx(mRadius);
//        borderWidth = Tools.getDimenPx(borderWidth);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeCornerSelectView);

        //bgSelectColor颜色 默认与不选中字色，边框色一样,默认选中字是白色的，不选中时是背景颜色，bgColor默认是透明的，
        // 选中的背景颜色
        bgColorSelect = mTypedArray.getColor(R.styleable.ShapeCornerSelectView_appsBgColorSelect, bgColorSelect);
        textColor = dividerColor = borderColor = bgColorSelect;

        // 未选中的字体颜色
        textColor = mTypedArray.getColor(R.styleable.ShapeCornerSelectView_appsTextColor, textColor);
        // 选中的字体的颜色
        textColorSelect = mTypedArray.getColor(R.styleable.ShapeCornerSelectView_appsTextColorSelect, textColorSelect);
        // 字体大小
        textSize = mTypedArray.getDimensionPixelSize(R.styleable.ShapeCornerSelectView_appsTextSize, textSize);

        // 没选中的颜色是透明的
        bgColor = mTypedArray.getColor(R.styleable.ShapeCornerSelectView_appsBgColor, bgColor);
        // 圆角矩形半径
        mRadius = mTypedArray.getDimensionPixelSize(R.styleable.ShapeCornerSelectView_appsRadius, mRadius);
        // 没选中的颜色是透明的
        borderColor = mTypedArray.getColor(R.styleable.ShapeCornerSelectView_appsBorderColor, borderColor);
        // 边框的宽度
        borderWidth = mTypedArray.getDimensionPixelSize(R.styleable.ShapeCornerSelectView_appsBorderWidth, borderWidth);
        // 是否有分割线
        isDivider = mTypedArray.getBoolean(R.styleable.ShapeCornerSelectView_appsDivider, isDivider);
        // 分割线的颜色
        dividerColor = mTypedArray.getColor(R.styleable.ShapeCornerSelectView_appsDividerColor, dividerColor);
        // 是否有边框
        isBorder = mTypedArray.getBoolean(R.styleable.ShapeCornerSelectView_appsBorder, isBorder);
        mTypedArray.recycle();

        // 初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }


    /**
     * 较正圆角矩形
     *
     * @param rectF
     */
    private void changeRectF(RectF rectF) {
        int half = borderWidth / 2;
        rectF.top += half;
        rectF.left += half;
        rectF.bottom -= half;
        rectF.right -= half;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 没有初始化完成,则返回
        if (width == 0 || buttonNum == 0) {
            return;
        }
        // 画背景
        drawBackground(canvas);

        //画选中
        drawSelect(canvas);
        // 画边框，透明则不画
        drawBorder(canvas);
        // 画分隔线
        drawDivider(canvas);

    }


    // 画背景，透明则不画
    private void drawBackground(Canvas canvas) {
        if (bgColor != Color.TRANSPARENT) {
            mPaint.setColor(bgColor);
            mPaint.setStyle(Style.FILL);
            drawFullRoundRect(canvas);
        }
    }

    // 画边框，透明则不画
    private void drawBorder(Canvas canvas) {
        if (isBorder && borderColor != Color.TRANSPARENT) {
            mPaint.setColor(borderColor);
            mPaint.setStyle(Style.STROKE);
            mPaint.setStrokeWidth(borderWidth);
            drawFullRoundRect(canvas);
        }
    }

    //画选中
    private void drawSelect(Canvas canvas) {
        if (bgColorSelect != Color.TRANSPARENT) {
            mPaint.setStyle(Style.FILL);
            mPaint.setColor(bgColorSelect);
            float piece = width / buttonNum;
            // 线宽一半
            rectf.left = piece * currentIndex;
            rectf.top = 0;
            rectf.right = piece * (currentIndex + 1); // 右边
            rectf.bottom = height; // 下边
            // 索引的第一个及最后一个是圆角矩形填充
            if (currentIndex == 0 || currentIndex == buttonNum - 1) {
                changeRectF(rectf);// 修正
                if (currentIndex == 0) {
                    rectf.right += borderWidth / 2;// 修正
                    canvas.drawRoundRect(rectf, mRadius, mRadius, mPaint);
                    rectf.left = rectf.right - mRadius;
                } else {
                    rectf.left -= borderWidth / 2;// 修正
                    canvas.drawRoundRect(rectf, mRadius, mRadius, mPaint);
                    rectf.right = rectf.left + mRadius;
                }
                //完全不透明时才绘制
                int color = mPaint.getAlpha();
                if (color == 0xFF) {
                    // 填充圆角矩形成直角
                    canvas.drawRect(rectf, mPaint);
                }

            } else {
                // 画直角
                canvas.drawRect(rectf, mPaint);
            }
        }
    }

    // 画分隔线
    private void drawDivider(Canvas canvas) {
        if (isDivider && dividerColor != Color.TRANSPARENT) {
            mPaint.setColor(dividerColor);
            float piece = width / buttonNum;
            for (int i = 1; i < buttonNum; i++) {
                canvas.drawLine(i * piece, borderWidth, i * piece, height - borderWidth, mPaint);
            }
        }
    }

    // 画全屏的圆角矩形
    private void drawFullRoundRect(Canvas canvas) {
        rectf.left = 0;
        rectf.top = 0;
        rectf.right = width;
        rectf.bottom = height;
        if (mPaint.getStyle() == Style.STROKE) {// 画空心时需要减掉线的宽度的一半
            changeRectF(rectf);
        } else {// 实心
            if (isBorder && borderColor != Color.TRANSPARENT) {// 有边线，向内部多画一点
                changeRectF(rectf);
            }
        }
        canvas.drawRoundRect(rectf, mRadius, mRadius, mPaint); // 绘制圆角矩形
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    int width, height;
    private IComCallBacks<Integer> callBacks;
    // 选中的String
    private String arrStrings[];
    private int currentIndex = -1; // 当前哪个被选中
    private int buttonNum = 0;// 按钮的数量

    //初始化所有的button
    public void initButtons(String arr[], IComCallBacks<Integer> callBacks) {
        this.callBacks = callBacks;
        this.arrStrings = arr;
        this.buttonNum = arr.length;
        // 添加BUTTON,并初始化事件
        addAllButtons();
        // 设置button选中,默认第一个
        setCurrentIndex(0);
    }

    /***
     * 添加所有的button
     */
    private void addAllButtons() {
        this.removeAllViews();
        int count = 0;
        for (String string : arrStrings) {
            TextView textView = new TextView(getContext());
            this.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            textView.setOnClickListener(this);
            // 方便外面使用
            textView.setId(count++);
            textView.setText(string);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            LayoutParams ll = ((LayoutParams) textView.getLayoutParams());
            ll.width = 0;
            ll.weight = 1;
            textView.setGravity(Gravity.CENTER);
        }
    }

    //选中的是否可以点击
    boolean isSelectedClickable=false;


    /**
     * 选中的是否可以点击
     * @param selectedClickable
     */
    public void setSelectedClickable(boolean selectedClickable) {
        isSelectedClickable = selectedClickable;
    }

    /***
     * 设置第几个BUTTON 被选中
     *
     * @param index
     */
    public void setCurrentIndex(int index) {
        if (currentIndex != index) {
            if (index >= 0 && index < buttonNum) {//合法性检查
                currentIndex = index;
                for (int i = 0; i < buttonNum; i++) {
                    TextView textView = (TextView) this.getChildAt(i);
                    textView.setEnabled(isSelectedClickable||i != currentIndex);// 选中的无法再点击
                    textView.setTextColor(i == currentIndex ? textColorSelect : textColor);
                }
                invalidate();// 重画背景
            }
        }
    }


    @Override
    public void onClick(View arg0) {
        int index = arg0.getId();
        setCurrentIndex(index);
        if (callBacks != null)
            callBacks.call(index);
    }

    //set get methord
    // set get methord
    public String getCurrentButtonText() {
        return arrStrings[currentIndex];
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public TextView getChildTextViewAt(int currentIndex) {
        return (TextView) this.getChildAt(currentIndex);
    }

    @Override
    public void setViewPager(ViewPager pager) {

        PagerAdapter adapter=pager.getAdapter();
        if(adapter==null){
            throw new RuntimeException("adapter为空");
        }
        if(!(adapter instanceof IPagerAdapterTitle)){
            throw new RuntimeException("adapter 必需为 IPagerAdapterTitle 的实例");
        }
        IPagerAdapterTitle title= (IPagerAdapterTitle) adapter;
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setCurrentIndex(position);
            }
        });
        initButtons(title.getTitles(), v -> pager.setCurrentItem(v));
    }
}
