package com.hd.view.scrollview;//package com.caiyu.qqsd.view.scrollview;
//
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.os.Build.VERSION;
//import android.os.Build.VERSION_CODES;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.DecelerateInterpolator;
//import android.view.animation.Interpolator;
//import android.view.animation.LinearInterpolator;
//import android.view.animation.RotateAnimation;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.caiyu.qqsd.R;
//
///**
// * @类名:ElasticScrollView
// * @功能描述:可下拉刷新的ScorllView
// * @作者:XuanKe'Huang
// * @时间:2015-7-31 下午2:22:31
// * @Copyright @2014
// */
//@SuppressLint({ "ClickableViewAccessibility", "InflateParams" })
//public class ElasticScrollView extends ScrollView {
//	private final static int RELEASE_To_REFRESH = 0;
//	private final static int PULL_To_REFRESH = 1;
//	private final static int REFRESHING = 2;
//	private final static int DONE = 3;
//	private final static int LOADING = 4;
//	// 实际的padding的距离与界面上偏移距离的比例
//	private final static int RATIO = 3;
//
//	private int headContentHeight;
//	private LinearLayout llvInner;
//	private LinearLayout headView;
//	private ImageView ivArrow;
//	private ProgressBar pbBar;
//	private TextView tvTips;
//	private OnRefreshListener mRefreshListener;
//	private boolean isRefreshable;
//	private int state;
//	private boolean isBack;
//
//	private RotateAnimation animation;
//	private RotateAnimation reverseAnimation;
//
//	private boolean canReturn;
//	private boolean isRecored;
//	private int startY;
//
//	public ElasticScrollView(Context context) {
//		super(context);
//		init(context);
//	}
//
//	public ElasticScrollView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		init(context);
//	}
//
//	private void init(Context context) {
//		LayoutInflater inflater = LayoutInflater.from(context);
//		llvInner = new LinearLayout(context);
//		llvInner.setLayoutParams(new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT));
//		llvInner.setOrientation(LinearLayout.VERTICAL);
//		headView = (LinearLayout) inflater.inflate(R.layout.view_refresh_head,
//				null);
//		ivArrow = (ImageView) headView
//				.findViewById(R.id.head_arrowImageView);
//		pbBar = (ProgressBar) headView
//				.findViewById(R.id.head_progressBar);
//		tvTips = (TextView) headView.findViewById(R.id.head_tipsTextView);
////		lastUpdatedTextView = (TextView) headView
////				.findViewById(R.id.head_lastUpdatedTextView);
//		measureView(headView);
//		headContentHeight = headView.getMeasuredHeight();
//		headView.setPadding(0, -1 * headContentHeight, 0, 0);
//		headView.invalidate();
//		llvInner.addView(headView);
//		addView(llvInner);
//		animation = new RotateAnimation(0, -180,
//				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//		animation.setInterpolator(new LinearInterpolator());
//		animation.setDuration(250);
//		animation.setFillAfter(true);
//
//		reverseAnimation = new RotateAnimation(-180, 0,
//				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//		reverseAnimation.setInterpolator(new LinearInterpolator());
//		reverseAnimation.setDuration(200);
//		reverseAnimation.setFillAfter(true);
//
//		state = DONE;
//		isRefreshable = false;
//		canReturn = false;
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (isRefreshable) {
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				if (getScrollY() == 0 && !isRecored) {
//					isRecored = true;
//					startY = (int) event.getY();
//				}
//				break;
//			case MotionEvent.ACTION_UP:
//				if (state != REFRESHING && state != LOADING) {
//					if (state == DONE) {
//						// 什么都不做
//					}
//					if (state == PULL_To_REFRESH) {
//						state = DONE;
//						changeHeaderViewByState();
//					}
//					if (state == RELEASE_To_REFRESH) {
//						state = REFRESHING;
//						changeHeaderViewByState();
//						onRefresh();
//					}
//				}
//				isRecored = false;
//				isBack = false;
//
//				break;
//			case MotionEvent.ACTION_MOVE:
//				int tempY = (int) event.getY();
//				if (!isRecored && getScrollY() == 0) {
//					isRecored = true;
//					startY = tempY;
//				}
//
//				if (state != REFRESHING && isRecored && state != LOADING) {
//					// 可以松手去刷新了
//					if (state == RELEASE_To_REFRESH) {
//						canReturn = true;
//
//						if (((tempY - startY) / RATIO < headContentHeight)
//								&& (tempY - startY) > 0) {
//							state = PULL_To_REFRESH;
//							changeHeaderViewByState();
//						}
//						// 一下子推到顶了
//						else if (tempY - startY <= 0) {
//							state = DONE;
//							changeHeaderViewByState();
//						} else {
//							// 不用进行特别的操作，只用更新paddingTop的值就行了
//						}
//					}
//					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
//					if (state == PULL_To_REFRESH) {
//						canReturn = true;
//
//						// 下拉到可以进入RELEASE_TO_REFRESH的状态
//						if ((tempY - startY) / RATIO >= headContentHeight) {
//							state = RELEASE_To_REFRESH;
//							isBack = true;
//							changeHeaderViewByState();
//						}
//						// 上推到顶了
//						else if (tempY - startY <= 0) {
//							state = DONE;
//							changeHeaderViewByState();
//						}
//					}
//
//					// done状态下
//					if (state == DONE) {
//						if (tempY - startY > 0) {
//							state = PULL_To_REFRESH;
//							changeHeaderViewByState();
//						}
//					}
//
//					// 更新headView的size
//					if (state == PULL_To_REFRESH) {
//						headView.setPadding(0, -1 * headContentHeight
//								+ (tempY - startY) / RATIO, 0, 0);
//
//					}
//
//					// 更新headView的paddingTop
//					if (state == RELEASE_To_REFRESH) {
//						headView.setPadding(0, (tempY - startY) / RATIO
//								- headContentHeight, 0, 0);
//					}
//					if (canReturn) {
//						canReturn = false;
//						return true;
//					}
//				}
//				break;
//			}
//		}
//		return super.onTouchEvent(event);
//	}
//
//	// 当状态改变时候，调用该方法，以更新界面
//	private void changeHeaderViewByState() {
//		switch (state) {
//		case RELEASE_To_REFRESH:
//			ivArrow.setVisibility(View.VISIBLE);
//			pbBar.setVisibility(View.GONE);
//			tvTips.setVisibility(View.VISIBLE);
//			ivArrow.clearAnimation();
//			ivArrow.startAnimation(animation);
//			tvTips.setText(R.string.release_to_refresh);
//			break;
//		case PULL_To_REFRESH:
//			pbBar.setVisibility(View.GONE);
//			tvTips.setVisibility(View.VISIBLE);
//			ivArrow.clearAnimation();
//			ivArrow.setVisibility(View.VISIBLE);
//			// 是由RELEASE_To_REFRESH状态转变来的
//			if (isBack) {
//				isBack = false;
//				ivArrow.clearAnimation();
//				ivArrow.startAnimation(reverseAnimation);
//			}
//			tvTips.setText(R.string.pull_to_refresh);
//			break;
//		case REFRESHING:
//			headView.setPadding(0, 0, 0, 0);
//			pbBar.setVisibility(View.VISIBLE);
//			ivArrow.clearAnimation();
//			ivArrow.setVisibility(View.GONE);
//			tvTips.setText(R.string.refreshing);
//			break;
//		case DONE:
//			headView.setPadding(0, -1 * headContentHeight, 0, 0);
//			pbBar.setVisibility(View.GONE);
//			ivArrow.clearAnimation();
//			ivArrow.setImageResource(R.drawable.finger_move);
//			tvTips.setText(R.string.pull_to_refresh);
//			break;
//		}
//	}
//
//	private void measureView(View child) {
//		ViewGroup.LayoutParams p = child.getLayoutParams();
//		if (p == null) {
//			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//					ViewGroup.LayoutParams.WRAP_CONTENT);
//		}
//		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
//		int lpHeight = p.height;
//		int childHeightSpec;
//		if (lpHeight > 0) {
//			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
//					MeasureSpec.EXACTLY);
//		} else {
//			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
//					MeasureSpec.UNSPECIFIED);
//		}
//		child.measure(childWidthSpec, childHeightSpec);
//	}
//
//	public void setonRefreshListener(OnRefreshListener refreshListener) {
//		this.mRefreshListener = refreshListener;
//		isRefreshable = true;
//	}
//
//	public interface OnRefreshListener {
//		public void onRefresh();
//	}
//
//	public void onRefreshComplete() {
//		if(pbBar.getVisibility()==View.VISIBLE){//手动拉下去的
//			changeHeaderViewByState();
//			smoothScrollTo(0);
//		}
//		else {
//			state=DONE;
//			changeHeaderViewByState();
//			invalidate();
//			scrollTo(0, 0);
//		}
//	}
//	protected final void smoothScrollTo(int scrollValue) {
//		smoothScrollTo(scrollValue, 200, 400, new OnSmoothScrollFinishedListener(){
//			@Override
//			public void onSmoothScrollFinished() {
//				state=DONE;
//				changeHeaderViewByState();
//				invalidate();
//				scrollTo(0, 0);
//			}});
//	}
//	private SmoothScrollRunnable mCurrentSmoothScrollRunnable;
//	private final void smoothScrollTo(int newScrollValue, long duration, long delayMillis,
//			OnSmoothScrollFinishedListener listener) {
//		if (null != mCurrentSmoothScrollRunnable) {
//			mCurrentSmoothScrollRunnable.stop();
//		}
//		final int oldScrollValue= headContentHeight+getScrollY();
//		if (oldScrollValue != newScrollValue) {
//			if (null == mScrollAnimationInterpolator) {
//				// Default interpolator is a Decelerate Interpolator
//				mScrollAnimationInterpolator = new DecelerateInterpolator();
//			}
//			mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(newScrollValue,oldScrollValue, duration, listener);
//
//			if (delayMillis > 0) {
//				postDelayed(mCurrentSmoothScrollRunnable, delayMillis);
//			} else {
//				post(mCurrentSmoothScrollRunnable);
//			}
//		}
//	}
//
//	private void onRefresh() {
//		if (mRefreshListener != null) {
//			mRefreshListener.onRefresh();
//		}
//	}
//
//	public void addChild(View child) {
//		llvInner.addView(child);
//	}
//
//	public void addChild(View child, int position) {
//		llvInner.addView(child, position);
//	}
//
//	private Interpolator mScrollAnimationInterpolator;
//
//	/**
//	 * Helper method which just calls scrollTo() in the correct scrolling
//	 * direction.
//	 *
//	 * @param value - New Scroll value
//	 */
//	protected final void setHeaderScroll(int value) {
//		scrollTo(0, value);
//	}
//	final class SmoothScrollRunnable implements Runnable {
//		private final Interpolator mInterpolator;
//		private final int mScrollToY;
//		private final int mScrollFromY;
//		private final long mDuration;
//		private OnSmoothScrollFinishedListener mListener;
//
//		private boolean mContinueRunning = true;
//		private long mStartTime = -1;
//		private int mCurrentY = -1;
//
//		public SmoothScrollRunnable(int fromY, int toY, long duration, OnSmoothScrollFinishedListener listener) {
//			mScrollFromY = fromY;
//			mScrollToY = toY;
//			mInterpolator = mScrollAnimationInterpolator;
//			mDuration = duration;
//			mListener = listener;
//		}
//
//		@Override
//		public void run() {
//
//			/**
//			 * Only set mStartTime if this is the first time we're starting,
//			 * else actually calculate the Y delta
//			 */
//			if (mStartTime == -1) {
//				mStartTime = System.currentTimeMillis();
//			} else {
//
//				/**
//				 * We do do all calculations in long to reduce software float
//				 * calculations. We use 1000 as it gives us good accuracy and
//				 * small rounding errors
//				 */
//				long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / mDuration;
//				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);
//
//				final int deltaY = Math.round((mScrollFromY - mScrollToY)
//						* mInterpolator.getInterpolation(normalizedTime / 1000f));
//				mCurrentY = mScrollFromY - deltaY;
//				setHeaderScroll(mCurrentY);
//			}
//
//			// If we're not at the target Y, keep going...
//			if (mContinueRunning && mScrollToY != mCurrentY) {
//				ViewCompat.postOnAnimation(ElasticScrollView.this, this);
//			} else {
//				if (null != mListener) {
//					mListener.onSmoothScrollFinished();
//				}
//			}
//		}
//
//		public void stop() {
//			mContinueRunning = false;
//			removeCallbacks(this);
//		}
//	}
//
//	static interface OnSmoothScrollFinishedListener {
//		void onSmoothScrollFinished();
//	}
//	public static class ViewCompat {
//
//		public static void postOnAnimation(View view, Runnable runnable) {
//			if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
//				SDK16.postOnAnimation(view, runnable);
//			} else {
//				view.postDelayed(runnable, 16);
//			}
//		}
//
//		public static void setBackground(View view, Drawable background) {
//			if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
//				SDK16.setBackground(view, background);
//			} else {
//				view.setBackgroundDrawable(background);
//			}
//		}
//
//		public static void setLayerType(View view, int layerType) {
//			if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB) {
//				SDK11.setLayerType(view, layerType);
//			}
//		}
//
//		@TargetApi(11)
//		static class SDK11 {
//
//			public static void setLayerType(View view, int layerType) {
//				view.setLayerType(layerType, null);
//			}
//		}
//
//		@TargetApi(16)
//		static class SDK16 {
//
//			public static void postOnAnimation(View view, Runnable runnable) {
//				view.postOnAnimation(runnable);
//			}
//
//			public static void setBackground(View view, Drawable background) {
//				view.setBackground(background);
//			}
//
//		}
//
//	}
//}
