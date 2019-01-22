package com.hd.view.gridview;

/**
 * <p>Created by liugd on 2018/4/4.<p>
 * <p>佛祖保佑，永无BUG<p>
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

import com.hd.R;

/**
 * Created by Administrator on 2018/1/5.
 */

public class DividerGridView extends GridView {

    public DividerGridView(Context context) {
        super(context);
    }

    public DividerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DividerGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    int colorRes = R.color.divider1pxColor;


    boolean hasDivider = false;

    public void setHasDivider(boolean hasDivider) {
        this.hasDivider = hasDivider;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!hasDivider) {
            return;
        }
        if (getWidth() == 0) {
            return;
        }

        int colnum = getNumColumns(); //获取列数
        int total = getChildCount();  //获取Item总数
        if (total == 0 || total == 0) {
            return;
        }
        int rownum = total / colnum;
        if (total % colnum != 0) {//不为满行行数自增1
            rownum++;
        }
        Paint localPaint = new Paint();
        localPaint.setStyle(Paint.Style.STROKE); //画笔实心
        localPaint.setColor(getContext().getResources().getColor(colorRes));//画笔颜色

        int oneX = getWidth() / colnum;
        int oneY = getHeight() / rownum;
        for (int i = 1; i <= colnum - 1; i++) {
            canvas.drawLine(oneX * i, 0, oneX * i, getHeight(), localPaint);
        }
        for (int i = 1; i <= rownum - 1; i++) {
            canvas.drawLine(0, oneY * i, getWidth(), oneY * i, localPaint);
        }

//        int colnum = getNumColumns(); //获取列数
//        int total = getChildCount();  //获取Item总数
//        if (total == 0 || total == 0) {
//            return;
//        }
//        int rownum;
//        //计算行数
//        if (total % colnum == 0) {
//            rownum = total / colnum;
//        } else {
//            rownum = (total / colnum) + 1; //当余数不为0时，要把结果加上1
//        }
//        Paint localPaint = new Paint();
//        localPaint.setStyle(Paint.Style.STROKE); //画笔实心
//        localPaint.setColor(getContext().getResources().getColor(colorRes));//画笔颜色
//
//        View view0 = getChildAt(0); //第一个view
//        View viewColLast = getChildAt(colnum - 1);//第一行最后一个view
//        View viewRowLast = getChildAt((rownum - 1) * colnum); //第一列最后一个view
//
//        if (view0 != null && viewColLast != null && viewRowLast != null) {
//            for (int i = 1, c = 1; i < rownum || c < colnum; i++, c++) {
//                //画横线
//                canvas.drawLine(view0.getLeft(), view0.getBottom() * i, viewColLast.getRight(), viewColLast.getBottom() * i, localPaint);
//                //画竖线
//                canvas.drawLine(view0.getRight() * c, view0.getTop(), viewRowLast.getRight() * c, viewRowLast.getBottom(), localPaint);
//            }
//        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    l.onClick(v);
                }
                return true;
            }
        });
    }
}
