package com.hd.utils.other;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 旋转动画
 * Created by liugd on 2017/1/11.
 */

public class Rotate3D extends Animation {

    private float mCenterX = 0;
    private float mCenterY = 0;

    public void setCenter(float centerX, float centerY) {
        mCenterX = centerX;
        mCenterY = centerY;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Matrix matrix = t.getMatrix();
        Camera camera = new Camera();
        camera.save();
        //算转多少圈，除少360 然后*360 是取整
        long sss = (this.getDuration()) / 360 * 360;
        camera.rotateY(sss * interpolatedTime);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-mCenterX, -mCenterY);
        matrix.postTranslate(mCenterX, mCenterY);
    }

}
