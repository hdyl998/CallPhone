package com.hd.view.scrollview;//package com.caiyu.qqsd.view.scrollview;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//
//import com.caiyu.qqsd.view.BaseTipsView;
//import com.caiyu.qqsd.view.EmptyLayout;
//
//public class MyTipsObScrollView extends BaseTipsView<ObservableVertialScrollView> {
//
//	public MyTipsObScrollView(Context mContext) {
//		this(mContext, null);
//	}
//
//	public MyTipsObScrollView(Context mContext, AttributeSet attrs) {
//		super(mContext, attrs);
//	}
//
//	@Override
//	protected ObservableVertialScrollView createTipsView(Context mContext, AttributeSet attrs) {
//		return new ObservableVertialScrollView(mContext, attrs);
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if(!getContentView().isEnabled())return false;
//		return super.onTouchEvent(event);
//	}
//
//	/**
//	 * 显示正常样子
//	 */
//	@Override
//	public void showNormal() {
//		super.showNormal();
//	}
//
//	/**
//	 * 添加scrollView Body
//	 */
//	public void addScorllViewBody(View view) {
//		contentViewT.addView(view);
//	}
//
//	@Override
//	public EmptyLayout getEmptyLayout() {
//		return emptyLayout;
//	}
//
//	@Override
//	public void showError() {
//		contentViewT.setVisibility(View.GONE);
//		super.showError();
//	}
//
//	@Override
//	public void showEmpty() {
//		contentViewT.setVisibility(View.GONE);
//		super.showEmpty();
//	}
//
//	@Override
//	public void showLoading() {
//		contentViewT.setVisibility(View.GONE);
//		super.showLoading();
//	}
//
//}
