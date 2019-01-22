package com.hd.view.listview;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * 可下拉浮动标题栏效果listView
 *
 * @author liugd
 */
public class PinnedHeaderListView extends ListView {
    public interface PinnedHeaderAdapterInter {

        int PINNED_HEADER_GONE = 0;
        int PINNED_HEADER_VISIBLE = 1;
        int PINNED_HEADER_PUSHED_UP = 2;

        int getPinnedHeaderState(int position);

        void configurePinnedHeader(View header, int position);
    }

    public static abstract class PinnedHeaderAdapter extends BaseAdapter implements PinnedHeaderAdapterInter {

    }

    private PinnedHeaderAdapterInter mAdapter;
    /* 浮动的头部 */
    private View mHeaderView;
    private boolean mHeaderViewVisible;
    private int mHeaderViewWidth;
    private int mHeaderViewHeight;

    public PinnedHeaderListView(Context context) {
        super(context);
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPinnedHeaderView(View view) {
        mHeaderView = view;
        if (mHeaderView != null) {
            setFadingEdgeLength(0);
        }
    }

//	/**
//	 * 头部设置监听
//	 *
//	 * @param clickListener
//	 * @param id
//	 */
//	public void setOnPinnedHeaderViewListener(OnClickListener clickListener, int id) {
//		if (mHeaderView != null) {
//			if (id != -1) {
//				mHeaderView.findViewById(id).setOnClickListener(clickListener);
//			} else
//				mHeaderView.setOnClickListener(clickListener);
//		}
//	}

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (PinnedHeaderAdapterInter) adapter;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        try {
            super.onLayout(changed, left, top, right, bottom);
            if (mHeaderView != null) {
                mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                configureHeaderView(getFirstVisiblePosition());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void configureHeaderView(int position) {
        position -= getHeaderViewsCount();
        if (mHeaderView == null || mAdapter == null) {
            return;
        }
        int state = mAdapter.getPinnedHeaderState(position);
        switch (state) {
            case PinnedHeaderAdapterInter.PINNED_HEADER_GONE: {
                mHeaderViewVisible = false;
                break;
            }

            case PinnedHeaderAdapterInter.PINNED_HEADER_VISIBLE: {
                mAdapter.configurePinnedHeader(mHeaderView, position);
                mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                mHeaderViewVisible = true;
                break;
            }

            case PinnedHeaderAdapterInter.PINNED_HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);// 得到最顶部的view
                int bottom = firstView.getBottom();
                int headerHeight = mHeaderView.getHeight();
                int y;
                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                } else {
                    y = 0;
                }
                mAdapter.configurePinnedHeader(mHeaderView, position);
                if (mHeaderView.getTop() != y) {
                    mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
                }
                mHeaderViewVisible = true;
                break;

            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (Exception e) {
        }
        if (mHeaderViewVisible) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }
}