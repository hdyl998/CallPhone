package com.callphone.client.webview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.callphone.client.R;


/**
 * Date:2017/11/7 17:16
 * Author:liugd
 * Modification:
 * ................
 * 佛祖保佑，永无BUG
 **/


public class WebViewProgressBar extends ProgressBar {


    private boolean isAnimStart = false;
    private int currentProgress;

    public WebViewProgressBar(Context context) {
        super(context, null, android.R.attr.progressBarStyleHorizontal);
        initViews();
    }

    private void initViews() {
        setProgressDrawable(getResources().getDrawable(R.drawable.webview_progressbar));
        setMax(100);
    }

    /***
     * 开始进度
     */
    public void startProgress() {
        setVisibility(View.VISIBLE);
        setProgress(0);
        setAlpha(1.0f);
    }


    /***
     * 结束进度
     */
    public void finishProgress() {
        onProgressChanged(100);
    }


    public void onError() {
        finishProgress();
    }

    /***
     * 外部去调用,当进度条改变时
     * @param newProgress
     */
    public void onProgressChanged(int newProgress) {
        currentProgress = getProgress();
        if (newProgress >= 100 && !isAnimStart) {
            // 防止调用多次动画
            isAnimStart = true;
            setProgress(newProgress);
            // 开启属性动画让进度条平滑消失
            startDismissAnimation(getProgress());
        } else {
            // 开启属性动画让进度条平滑递增
            startProgressAnimation(newProgress);
        }
    }

    /**
     * progressBar递增动画
     */
    private void startProgressAnimation(int newProgress) {
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "progress", currentProgress, newProgress);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    /**
     * progressBar消失动画
     */
    private void startDismissAnimation(final int progress) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
        anim.setDuration(800);  // 动画时长
        anim.setInterpolator(new DecelerateInterpolator());     // 减速
        // 关键, 添加动画进度监听器
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();      // 0.0f ~ 1.0f
                int offset = 100 - progress;
                setProgress((int) (progress + offset * fraction));
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束
                setProgress(0);
                setVisibility(View.GONE);
                isAnimStart = false;
            }
        });
        anim.start();
    }

}
