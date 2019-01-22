package com.hd.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class CircularImageView extends AppCompatImageView {
    public CircularImageView(Context context) {
        super(context);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressWarnings("unused")
    @Override
    protected void onDraw(Canvas canvas) {
        try {
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }
            if (getWidth() == 0 || getHeight() == 0) {
                return;
            }
            /**
             * 主要修改代码(NetWorkImageView的加载机制是使用ImageContainer,
             * 所以得到的图片有可能是一个StateListDrawable,或者是一个.9图)
             */
            Bitmap b = null;
            if (drawable instanceof BitmapDrawable) {
                b = ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable instanceof StateListDrawable) {// 判断图片是不是一个StateListDrawable图片
                StateListDrawable drawable2 = (StateListDrawable) drawable;
                Drawable a = drawable2.getCurrent();// 得到当前显示的图片
                try {// 防止图片是一个.9图
                    b = ((BitmapDrawable) a).getBitmap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (null == b) {
                return;
            }
            Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            int w = getWidth();
            int h = getHeight();
            if (cirecleRoundRadius != 0) {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.WHITE);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, paint);
            }

            Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
            canvas.drawBitmap(roundBitmap, 0, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        // final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xffBAB399);
        canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2 - cirecleRoundRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

    //圆环周围的半径
    int cirecleRoundRadius = 0;

    /***
     * 设置圆环周围的半径
     *
     * @param cirecleRoundRadius
     */
    public void setCirecleRoundRadius(int cirecleRoundRadius) {
        this.cirecleRoundRadius = cirecleRoundRadius;
    }
}
