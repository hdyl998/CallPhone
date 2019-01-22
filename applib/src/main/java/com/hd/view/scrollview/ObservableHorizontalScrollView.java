package com.hd.view.scrollview;/**
 * Created by huangxk on 2016/1/27.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.HorizontalScrollView;

/**
 * @method
 * @pram
 * @return
 */
public class ObservableHorizontalScrollView extends HorizontalScrollView {
    public interface OnScrollListener {
        public void onScrollChanged(ObservableHorizontalScrollView scrollView, int x, int y, int
                oldX, int oldY);

        public void onEndScroll(ObservableHorizontalScrollView scrollView);
    }

    private boolean mIsScrolling;
    private boolean mIsTouching;
    private Runnable mScrollingRunnable;
    private OnScrollListener mOnScrollListener;

    public ObservableHorizontalScrollView(Context context) {
        this(context, null, 0);
    }

    public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ObservableHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if (action == MotionEvent.ACTION_MOVE) {
            mIsTouching = true;
            mIsScrolling = true;
        } else if (action == MotionEvent.ACTION_UP) {
            if (mIsTouching && !mIsScrolling) {
                if (mOnScrollListener != null) {
                    mOnScrollListener.onEndScroll(this);
                }
            }

            mIsTouching = false;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (Math.abs(oldX - x) > 0) {
            if (mScrollingRunnable != null) {
                removeCallbacks(mScrollingRunnable);
            }
            mScrollingRunnable = new Runnable() {
                public void run() {
                    if (mIsScrolling && !mIsTouching) {
                        if (mOnScrollListener != null) {
                            mOnScrollListener.onEndScroll(ObservableHorizontalScrollView.this);
                        }
                    }
                    mIsScrolling = false;
                    mScrollingRunnable = null;
                }
            };
            postDelayed(mScrollingRunnable, 200);
        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }

    public OnScrollListener getOnScrollListener() {
        return mOnScrollListener;
    }

    public void setOnScrollListener(OnScrollListener mOnEndScrollListener) {
        this.mOnScrollListener = mOnEndScrollListener;
    }

    /**
     * @param fromX   起始Y坐标
     * @param toX     终止Y坐标
     * @param fps     帧率
     * @param durtion 动画完成时间（毫秒）
     * @desc 平滑滚动
     */
    public void smoothScrollX( int fromX, int toX, int fps, long durtion) {
        smoothScrollThread = new SmoothScrollThread( fromX, toX, durtion, fps);
        smoothScrollThread.run();
    }

    private SmoothScrollThread smoothScrollThread;

    /**
     * @desc 平滑滚动线程，用于递归调用自己来实现某个视图的平滑滚动
     */
    class SmoothScrollThread implements Runnable {
//        //需要操控的视图
//        private View v = null;
        //原Y坐标
        private int fromX = 0;
        //目标Y坐标
        private int toX = 0;
        //动画执行时间（毫秒）
        private long durtion = 0;
        //帧率
        private int fps = 60;
        //间隔时间（毫秒），间隔时间 = 1000 / 帧率
        private int interval = 0;
        //启动时间，-1 表示尚未启动
        private long startTime = -1;
        //减速插值器
        private DecelerateInterpolator decelerateInterpolator = null;

        /**
         * @desc 构造方法，做好第一次配置
         */
        public SmoothScrollThread( int fromX, int toX, long durtion, int fps) {
            this.fromX = fromX;
            this.toX = toX;
            this.durtion = durtion;
            this.fps = fps;
            this.interval = 1000 / this.fps;
            decelerateInterpolator = new DecelerateInterpolator();
        }

        @Override
        public void run() {
            //先判断是否是第一次启动，是第一次启动就记录下启动的时间戳，该值仅此一次赋值
            if (startTime == -1) {
                startTime = System.currentTimeMillis();
            }
            //得到当前这个瞬间的时间戳
            long currentTime = System.currentTimeMillis();
            //放大倍数，为了扩大除法计算的浮点精度
            int enlargement = 1000;
            //算出当前这个瞬间运行到整个动画时间的百分之多少
            float rate = (currentTime - startTime) * enlargement / durtion;
            //这个比率不可能在 0 - 1 之间，放大了之后即是 0 - 1000 之间
            rate = Math.min(rate, 1000);
            //将动画的进度通过插值器得出响应的比率，乘以起始与目标坐标得出当前这个瞬间，视图应该滚动的距离。
            int changeDistance = (int) ((fromX - toX) * decelerateInterpolator.getInterpolation
                    (rate / enlargement));
            int currentX = fromX - changeDistance;
            scrollTo(currentX, 0);
            if (currentX != toX) {
                postDelayed(this, this.interval);
            } else {
                return;
            }
        }

        public void stop() {
            removeCallbacks(this);
        }
    }

}