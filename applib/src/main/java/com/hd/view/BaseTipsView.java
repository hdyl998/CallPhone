package com.hd.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hd.R;
import com.hd.base.maininterface.IErrorRequest;
import com.hd.utils.ViewUtils;


public abstract class BaseTipsView<T extends View> extends RelativeLayout implements EmptyLayout.IOnEmptyDataChangedListener {
    protected EmptyLayout emptyLayout;

    protected T contentViewT;

    public BaseTipsView(Context context) {
        this(context, null);
    }

    @SuppressWarnings("deprecation")
    public BaseTipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        contentViewT = createTipsView(context, attrs);
        contentViewT.setId(0);
        contentViewT.setBackgroundDrawable(null);//去掉背景色，防止重绘
        this.addView(contentViewT, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTipsListView);
        boolean isShowNorma = mTypedArray.getBoolean(R.styleable.MyTipsListView_isShowNormal, false);
        emptyLayout = new EmptyLayout(context, contentViewT, isShowNorma);
        mTypedArray.recycle();
        initSomething(context);
    }

    protected void initSomething(Context context) {

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
    }

    /**
     * 创建可以提示的View
     *
     * @param context mContext
     * @param attrs   属性
     * @return View
     */
    protected abstract T createTipsView(Context context, AttributeSet attrs);

    /**
     * 得到可以提示的view的内部view
     *
     * @return
     */
    public T getContentView() {
//		LogUitls.print("siyehua-scrollable", getClass().getSimpleName() + ": getContentView()");
        return contentViewT;
    }

    /**
     * 设置网络错误
     *
     * @param errorButtonClickListener
     */
    public void setErrorDataClickListener(OnClickListener errorButtonClickListener) {
        emptyLayout.setErrorButtonClickListener(errorButtonClickListener);
    }

    /**
     * 设置网络错误
     *
     * @param
     */
    public void setErrorDataInterface(final IErrorRequest iErrorRequest) {
        setErrorDataClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iErrorRequest.startGetMyData();
            }
        });
    }

    /***
     * 得到提示的emptylayout
     *
     * @return
     */
    @Override
    public EmptyLayout getEmptyLayout() {
        return emptyLayout;
    }

    @Override
    public void showError() {
        emptyLayout.showError();
    }

    @Override
    public void showEmpty() {
        emptyLayout.showEmpty();
    }

    @Override
    public void showLoading() {
        emptyLayout.showLoading();
        if (emptyLayout.isShowNormal == true) {
            contentViewT.setVisibility(View.GONE);
        }
    }

    /**
     * 显示普通只在 T 为scorllView里用
     */
    @Override
    public void showNormal() {
        emptyLayout.setViewGone();
        contentViewT.setVisibility(View.VISIBLE);
//        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
//        contentViewT.startAnimation(animation);
    }

    /**
     * 标志是否初化化emptylayout
     */
    boolean isSetEmptylayout = false;

    /***
     * 设置空数据样式为两行样式
     *
     * @param fistString
     * @param secondString
     */
    public void initEmptyViewOfDoubleLine(String fistString, String secondString) {
        if (isSetEmptylayout == false) {
            getEmptyLayout().setEmptyView(ViewUtils.getEmptyViewOfDoubleLine(getContext(), fistString, secondString));
        } else {
            ViewGroup viewGroup = getEmptyLayout().getEmptyView();
            try {
                TextView tView = (TextView) viewGroup.getChildAt(0);
                TextView tView2 = (TextView) viewGroup.getChildAt(1);
                tView.setText(fistString);
                tView2.setText(secondString);
            } catch (Exception e) {
            }
        }
        isSetEmptylayout = true;
    }

    /**
     * 初始化双行emptylayout,并可以动态设值
     *
     * @param residDoubleMsg
     */
    public void initEmptyViewOfDoubleLine(int residDoubleMsg) {
        String strings[] = getContext().getResources().getString(residDoubleMsg).split("\\|");
        if (strings.length < 2) {
            initEmptyViewOfDoubleLine(strings[0], "");
        } else {
            initEmptyViewOfDoubleLine(strings[0], strings[1]);
        }
    }

    public void setAdapter(ListAdapter listAdapter) {
    }

}