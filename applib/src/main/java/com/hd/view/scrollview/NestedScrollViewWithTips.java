package com.hd.view.scrollview;//package com.caiyu.qqsd.view.scrollview;
//
//import android.content.Context;
//import android.support.v4.widget.NestedScrollView;
//import android.util.AttributeSet;
//import android.view.View;
//
//import com.caiyu.qqsd.view.BaseTipsView;
//
//public class NestedScrollViewWithTips extends BaseTipsView<NestedScrollView> {
//
//    public NestedScrollViewWithTips(Context mContext) {
//        this(mContext, null);
//    }
//
//    public NestedScrollViewWithTips(Context mContext, AttributeSet attrs) {
//        super(mContext, attrs);
//    }
//
//    @Override
//    protected NestedScrollView createTipsView(Context mContext, AttributeSet attrs) {
//        return new NestedScrollView(mContext);
//    }
//
//    /**
//     * 显示正常样子
//     */
//    @Override
//    public void showNormal() {
//        super.showNormal();
//    }
//
//    /**
//     * 添加scrollView Body
//     */
//    public void addScorllViewBody(View view) {
//        contentViewT.addView(view);
//    }
//
//    @Override
//    public void showError() {
//        contentViewT.setVisibility(View.GONE);
//        super.showError();
//    }
//
//    @Override
//    public void showEmpty() {
//        contentViewT.setVisibility(View.GONE);
//        super.showEmpty();
//    }
//
//    @Override
//    public void showLoading() {
//        contentViewT.setVisibility(View.GONE);
//        super.showLoading();
//    }
//}
