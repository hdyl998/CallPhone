//package com.hd.business.view.ui;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.util.AttributeSet;
//
///**
// * <p>Created by liugd on 2018/4/4.<p>
// * <p>佛祖保佑，永无BUG<p>
// */
//
//public class RullToRefreshRecyclerView  extends PullToRefreshBase<RecyclerView> {
//    /**
//     * 构造方法
//     *
//     * @param context mContext
//     */
//    public RullToRefreshRecyclerView(Context context) {
//        this(context, null);
//    }
//
//    /**
//     * 构造方法
//     *
//     * @param context mContext
//     * @param attrs attrs
//     */
//    public RullToRefreshRecyclerView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    /**
//     * 构造方法
//     *
//     * @param context mContext
//     * @param attrs attrs
//     * @param defStyle defStyle
//     */
//    public RullToRefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    /**
//     */
//    @Override
//    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
//        RecyclerView recyclerView = new RecyclerView(context);
//        return recyclerView;
//    }
//
//    /**
//     */
//    @Override
//    protected boolean isReadyForPullDown() {
//        return mRefreshableView.getScrollY() == 0;
//    }
//
//    /**
//     */
//    @Override
//    protected boolean isReadyForPullUp() {
//        float exactContentHeight =mRefreshableView.getHeight() * mRefreshableView.getScaleY();
//        return mRefreshableView.getScrollY() >= (exactContentHeight - mRefreshableView.getHeight());
//    }
//}
