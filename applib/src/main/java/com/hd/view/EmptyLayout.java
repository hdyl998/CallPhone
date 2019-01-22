package com.hd.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.hd.R;


public class EmptyLayout {

    private Context mContext;
    private ViewGroup vgLoading;
    private ViewGroup vgEmpty;
    private ViewGroup vgError;
    private Animation mLoadingAnimation;
    private View mListView;
    private int mErrorMessageViewId;
    private int mEmptyMessageViewId;
    private int mLoadingMessageViewId;
    private LayoutInflater mInflater;
    private boolean mViewsAdded;
    private int mLoadingAnimationViewId;
    private View.OnClickListener mLoadingButtonClickListener;
    private View.OnClickListener mEmptyButtonClickListener;
    private View.OnClickListener mErrorButtonClickListener;

    /**
     * emptyView的content
     */
    private ViewGroup viewContentView;
    // ---------------------------
    // static variables
    // ---------------------------
    /**
     * The empty state
     */
    public final static int TYPE_EMPTY = 1;
    /**
     * The loading state
     */
    public final static int TYPE_LOADING = 2;
    /**
     * The error state
     */
    public final static int TYPE_ERROR = 3;

    // ---------------------------
    // default values
    // ---------------------------
    private int mEmptyType = TYPE_LOADING;
    private String mErrorMessage = null;
    private String mEmptyMessage = null;
    private String mLoadingMessage = null;
    private int mLoadingViewButtonId = 0;
    private int mErrorViewButtonId = R.id.buttonError;
    private int mEmptyViewButtonId = R.id.buttonEmpty;
    private boolean mShowEmptyButton = true;
    private boolean mShowLoadingButton = true;
    private boolean mShowErrorButton = true;

    // ---------------------------
    // getters and setters
    // ---------------------------

    /**
     * Gets the loading layout
     *
     * @return the loading layout
     */
    public ViewGroup getLoadingView() {
        return vgLoading;
    }

    /**
     * Sets loading layout
     *
     * @param loadingView the layout to be shown when the list is loading
     */
    public void setLoadingView(ViewGroup loadingView) {
        this.vgLoading = loadingView;
    }

    /**
     * Sets loading layout resource
     *
     * @param res the resource of the layout to be shown when the list is
     *            loading
     */
    public void setLoadingViewRes(int res) {
        this.vgLoading = (ViewGroup) mInflater.inflate(res, null);
    }

    /**
     * Gets the empty layout
     *
     * @return the empty layout
     */
    public ViewGroup getEmptyView() {
        return vgEmpty;
    }

    /**
     * Sets empty layout
     *
     * @param emptyView the layout to be shown when no items are available to load in
     *                  the list
     */
    public void setEmptyView(ViewGroup emptyView) {
        setEmptyMessage(null);//单行message强制设置成空
        this.vgEmpty = emptyView;
    }

    /**
     * Sets empty layout resource
     *
     * @param res the resource of the layout to be shown when no items are
     *            available to load in the list
     */
    public void setEmptyViewRes(int res) {
        this.vgEmpty = (ViewGroup) mInflater.inflate(res, null);
    }

    /**
     * Gets the error layout
     *
     * @return the error layout
     */
    public ViewGroup getErrorView() {
        return vgError;
    }

    /**
     * Sets error layout
     *
     * @param errorView the layout to be shown when list could not be loaded due to
     *                  some error
     */
    public void setErrorView(ViewGroup errorView) {
        this.vgError = errorView;
    }

    /**
     * Sets error layout resource
     *
     * @param res the resource of the layout to be shown when list could not be
     *            loaded due to some error
     */
    public void setErrorViewRes(int res) {
        this.vgError = (ViewGroup) mInflater.inflate(res, null);
    }

    /**
     * Gets the loading animation
     *
     * @return the loading animation
     */
    public Animation getLoadingAnimation() {
        return mLoadingAnimation;
    }

    /**
     * Sets the loading animation
     *
     * @param animation the animation to play when the list is being loaded
     */
    public void setLoadingAnimation(Animation animation) {
        this.mLoadingAnimation = animation;
    }

    /**
     * Sets the resource of loading animation
     *
     * @param animationResource the animation resource to play when the list is being loaded
     */
    public void setLoadingAnimationRes(int animationResource) {
        mLoadingAnimation = AnimationUtils.loadAnimation(mContext,
                animationResource);
    }

    /**
     * Gets the list view for which this library is being used
     *
     * @return the list view
     */
    public View getListView() {
        return mListView;
    }

    /**
     * Sets the list view for which this library is being used
     *
     * @param listView
     */
    public void setListView(ListView listView) {
        this.mListView = listView;
    }

    /**
     * Gets the last set state of the list view
     *
     * @return loading or empty or error
     */
    public int getEmptyType() {
        return mEmptyType;
    }

    /**
     * Sets the state of the empty view of the list view
     *
     * @param emptyType loading or empty or error
     */
    public void setEmptyType(int emptyType) {
        this.mEmptyType = emptyType;
        changeEmptyType();
    }

    /**
     * Gets the message which is shown when the list could not be loaded due to
     * some error
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return mErrorMessage;
    }

    /**
     * Sets the message to be shown when the list could not be loaded due to
     * some error
     *
     * @param errorMessage  the error message
     * @param messageViewId the id of the text view within the error layout whose text
     *                      will be changed into this message
     */
    public void setErrorMessage(String errorMessage, int messageViewId) {
        this.mErrorMessage = errorMessage;
        this.mErrorMessageViewId = messageViewId;
    }

    /**
     * Sets the message to be shown when the list could not be loaded due to
     * some error
     *
     * @param errorMessage the error message
     */
    public void setErrorMessage(String errorMessage) {
        this.mErrorMessage = errorMessage;
    }

    /**
     * Gets the message which will be shown when the list will be empty for not
     * having any item to display
     *
     * @return the message which will be shown when the list will be empty for
     * not having any item to display
     */
    public String getEmptyMessage() {
        return mEmptyMessage;
    }

    /**
     * Sets the message to be shown when the list will be empty for not having
     * any item to display
     *
     * @param emptyMessage the message
     */
    public void setEmptyMessage(String emptyMessage, int messageViewId) {
        this.mEmptyMessage = emptyMessage;
        this.mEmptyMessageViewId = messageViewId;
    }

    /**
     * Sets the message to be shown when the list will be empty for not having
     * any item to display
     *
     * @param emptyMessage the message
     */
    public void setEmptyMessage(String emptyMessage) {
        this.mEmptyMessage = emptyMessage;
    }

    /**
     * Gets the message which will be shown when the list is being loaded
     *
     * @return
     */
    public String getLoadingMessage() {
        return mLoadingMessage;
    }

    /**
     * Sets the message to be shown when the list is being loaded
     *
     * @param loadingMessage the message
     * @param messageViewId  the id of the text view within the loading layout whose text
     *                       will be changed into this message
     */
    public void setLoadingMessage(String loadingMessage, int messageViewId) {
        this.mLoadingMessage = loadingMessage;
        this.mLoadingMessageViewId = messageViewId;
    }

    /**
     * Sets the message to be shown when the list is being loaded
     *
     * @param loadingMessage the message
     */
    public void setLoadingMessage(String loadingMessage) {
        this.mLoadingMessage = loadingMessage;
    }

    /**
     * Gets the view in the loading layout which will be animated when the list
     * is being loaded
     *
     * @return the view in the loading layout which will be animated when the
     * list is being loaded
     */
    public int getLoadingAnimationViewId() {
        return mLoadingAnimationViewId;
    }

    /**
     * Sets the view in the loading layout which will be animated when the list
     * is being loaded
     *
     * @param loadingAnimationViewId the id of the view
     */
    public void setLoadingAnimationViewId(int loadingAnimationViewId) {
        this.mLoadingAnimationViewId = loadingAnimationViewId;
    }

    /**
     * Gets the OnClickListener which perform when LoadingView was click
     *
     * @return
     */
    public View.OnClickListener getLoadingButtonClickListener() {
        return mLoadingButtonClickListener;
    }

    /**
     * Sets the OnClickListener to LoadingView
     *
     * @param loadingButtonClickListener OnClickListener Object
     */
    public void setLoadingButtonClickListener(
            View.OnClickListener loadingButtonClickListener) {
        this.mLoadingButtonClickListener = loadingButtonClickListener;
    }

    /**
     * Gets the OnClickListener which perform when EmptyView was click
     *
     * @return
     */
    public View.OnClickListener getEmptyButtonClickListener() {
        return mEmptyButtonClickListener;
    }

    /**
     * Sets the OnClickListener to EmptyView
     *
     * @param emptyButtonClickListener OnClickListener Object
     */
    public void setEmptyButtonClickListener(
            View.OnClickListener emptyButtonClickListener) {
        this.mEmptyButtonClickListener = emptyButtonClickListener;
    }

    /**
     * Gets the OnClickListener which perform when ErrorView was click
     *
     * @return
     */
    public View.OnClickListener getErrorButtonClickListener() {
        return mErrorButtonClickListener;
    }

    /**
     * Sets the OnClickListener to ErrorView
     *
     * @param errorButtonClickListener OnClickListener Object
     */
    public void setErrorButtonClickListener(
            View.OnClickListener errorButtonClickListener) {
        this.mErrorButtonClickListener = errorButtonClickListener;
        this.mEmptyButtonClickListener = errorButtonClickListener;//通用
        mShowErrorButton = true;
        if (vgError != null) {
            if (mErrorViewButtonId > 0
                    && mErrorButtonClickListener != null) {
                View errorViewButton = vgError.findViewById(mErrorViewButtonId);
                if (errorViewButton != null) {
                    errorViewButton.setOnClickListener(mErrorButtonClickListener);
                    errorViewButton.setVisibility(View.VISIBLE);
                }
            }
        }
        mShowLoadingButton = true;
        if (vgEmpty != null) {
            vgEmpty.setOnClickListener(mEmptyButtonClickListener);
        }
    }

    /**
     * Gets if a button is shown in the empty view
     *
     * @return if a button is shown in the empty view
     */
    public boolean isEmptyButtonShown() {
        return mShowEmptyButton;
    }

    /**
     * Sets if a button will be shown in the empty view
     *
     * @param showEmptyButton will a button be shown in the empty view
     */
    public void setShowEmptyButton(boolean showEmptyButton) {
        this.mShowEmptyButton = showEmptyButton;
    }

    /**
     * Gets if a button is shown in the loading view
     *
     * @return if a button is shown in the loading view
     */
    public boolean isLoadingButtonShown() {
        return mShowLoadingButton;
    }

    /**
     * Sets if a button will be shown in the loading view
     *
     */
    public void setShowLoadingButton(boolean showLoadingButton) {
        this.mShowLoadingButton = showLoadingButton;
    }

    /**
     * Gets if a button is shown in the error view
     *
     * @return if a button is shown in the error view
     */
    public boolean isErrorButtonShown() {
        return mShowErrorButton;
    }

    /**
     * Sets if a button will be shown in the error view
     *
     */
    public void setShowErrorButton(boolean showErrorButton) {
        this.mShowErrorButton = showErrorButton;
    }

    /**
     * Gets the ID of the button in the loading view
     *
     * @return the ID of the button in the loading view
     */
    public int getmLoadingViewButtonId() {
        return mLoadingViewButtonId;
    }

    /**
     * Sets the ID of the button in the loading view. This ID is required if you
     * want the button the loading view to be click-able.
     *
     * @param loadingViewButtonId the ID of the button in the loading view
     */
    public void setLoadingViewButtonId(int loadingViewButtonId) {
        this.mLoadingViewButtonId = loadingViewButtonId;
    }

    /**
     * Gets the ID of the button in the error view
     *
     * @return the ID of the button in the error view
     */
    public int getErrorViewButtonId() {
        return mErrorViewButtonId;
    }

    /**
     * Sets the ID of the button in the error view. This ID is required if you
     * want the button the error view to be click-able.
     *
     * @param errorViewButtonId the ID of the button in the error view
     */
    public void setErrorViewButtonId(int errorViewButtonId) {
        this.mErrorViewButtonId = errorViewButtonId;
    }

    /**
     * Gets the ID of the button in the empty view
     *
     * @return the ID of the button in the empty view
     */
    public int getEmptyViewButtonId() {
        return mEmptyViewButtonId;
    }

    /**
     * Sets the ID of the button in the empty view. This ID is required if you
     * want the button the empty view to be click-able.
     *
     * @param emptyViewButtonId the ID of the button in the empty view
     */
    public void setEmptyViewButtonId(int emptyViewButtonId) {
        this.mEmptyViewButtonId = emptyViewButtonId;
    }

    public ViewGroup getViewContentView() {
        return viewContentView;
    }


    // ---------------------------
    // private methods
    // ---------------------------

    private void changeEmptyType() {

        setDefaultValues();
        refreshMessages();

        // insert views in the root view
        if (!mViewsAdded) {
            RelativeLayout.LayoutParams lp = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            RelativeLayout rl = new RelativeLayout(mContext);
            rl.setLayoutParams(lp);
            if (vgEmpty != null)
                rl.addView(vgEmpty);
            if (vgLoading != null)
                rl.addView(vgLoading);
            if (vgError != null)
                rl.addView(vgError);
            mViewsAdded = true;

            if (mListView != null) {
                ViewGroup parent = (ViewGroup) mListView.getParent();
                parent.addView(rl);
                if (mListView instanceof AbsListView && isShowNormal == false)
                    ((AbsListView) mListView).setEmptyView(rl);//如果是LISTVIEW 就绑定起，且要绑定起来
            } else {
                viewContentView.addView(rl);
            }
        }
        View loadingAnimationView = null;
        if (mLoadingAnimationViewId > 0)
            loadingAnimationView = ((Activity) mContext)
                    .findViewById(mLoadingAnimationViewId);
        switch (mEmptyType) {
            case TYPE_EMPTY:// 数据为空
//				if (drawable != null) {
//					drawable.recycle();
//				}
                if (vgEmpty != null)
                    vgEmpty.setVisibility(View.VISIBLE);
                if (vgError != null)
                    vgError.setVisibility(View.GONE);
                if (vgLoading != null) {
                    vgLoading.setVisibility(View.GONE);
                    if (loadingAnimationView != null
                            && loadingAnimationView.getAnimation() != null)
                        loadingAnimationView.getAnimation().cancel();
                }
                break;
            case TYPE_ERROR:// 网络错误
//				if (drawable != null) {
//					drawable.recycle();
//				}
                if (vgEmpty != null)
                    vgEmpty.setVisibility(View.GONE);
                if (vgError != null)
                    vgError.setVisibility(View.VISIBLE);
                if (vgLoading != null) {
                    vgLoading.setVisibility(View.GONE);
                    if (loadingAnimationView != null
                            && loadingAnimationView.getAnimation() != null)
                        loadingAnimationView.getAnimation().cancel();
                }
                break;
            case TYPE_LOADING:// 正在加载
                if (vgEmpty != null)
                    vgEmpty.setVisibility(View.GONE);
                if (vgError != null)
                    vgError.setVisibility(View.GONE);
                if (vgLoading != null) {
                    vgLoading.setVisibility(View.VISIBLE);
                    if (mLoadingAnimation != null
                            && loadingAnimationView != null) {
                        // loadingAnimationView.startAnimation(mLoadingAnimation);
                    } else if (loadingAnimationView != null) {
                        // loadingAnimationView.startAnimation(getRotateAnimation());
                    }
                }
                break;
            default:
                break;
        }
    }

    private void refreshMessages() {
        if (mEmptyMessageViewId > 0 && mEmptyMessage != null)
            ((TextView) vgEmpty.findViewById(mEmptyMessageViewId))
                    .setText(mEmptyMessage);
        if (mLoadingMessageViewId > 0 && mLoadingMessage != null)
            ((TextView) vgLoading.findViewById(mLoadingMessageViewId))
                    .setText(mLoadingMessage);
        if (mErrorMessageViewId > 0 && mErrorMessage != null)
            ((TextView) vgError.findViewById(mErrorMessageViewId))
                    .setText(mErrorMessage);
    }

    private void setDefaultValues() {
        try {
            if (vgEmpty == null) {
                vgEmpty = (ViewGroup) mInflater.inflate(R.layout.view_empty, null);
                vgEmpty.setOnClickListener(mEmptyButtonClickListener);
                if (!(mEmptyMessageViewId > 0))
                    mEmptyMessageViewId = R.id.tv_message;
//                if (mShowEmptyButton && mEmptyViewButtonId > 0 && mEmptyButtonClickListener != null) {
//                    View emptyViewButton = vgEmpty.findViewById(mEmptyViewButtonId);
//                    if (emptyViewButton != null) {
//                        emptyViewButton.setOnClickListener(mEmptyButtonClickListener);
//                        emptyViewButton.setVisibility(View.VISIBLE);
//                    }
//                } else if (mEmptyViewButtonId > 0) {
//                    View emptyViewButton = vgEmpty
//                            .findViewById(mEmptyViewButtonId);
//                    emptyViewButton.setVisibility(View.GONE);
//                }
            }
            if (vgLoading == null) {
                vgLoading = (ViewGroup) mInflater.inflate(R.layout.view_loading,
                        null);
//				GifImageView gifImageView = new GifImageView(mContext);// 得到一个Gif
//																		// View
//
//				try {
//					drawable = new GifDrawable(mContext.getResources(),
//							R.drawable.loading);// 设置需要播放的图片
//					// drawable.setLoopCount(1);// 设置播放次数
//					gifImageView.setImageDrawable(drawable);// 把图片设置到Gif View中
//					gifImageView.setScaleType(ScaleType.FIT_XY);// 设置缩放模式
//					vgLoading.addView(gifImageView, 0);
//					gifImageView.getLayoutParams().width = (int) mContext
//							.getResources().getDimension(R.dimen._105px);
//					gifImageView.getLayoutParams().height = (int) mContext
//							.getResources().getDimension(R.dimen._224px);
//				} catch (NotFoundException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}


                if (!(mLoadingMessageViewId > 0))
                    mLoadingMessageViewId = R.id.tv_message;
                if (mShowLoadingButton && mLoadingViewButtonId > 0 && mLoadingButtonClickListener != null) {
                    View loadingViewButton = vgLoading.findViewById(mLoadingViewButtonId);
                    if (loadingViewButton != null) {
                        loadingViewButton
                                .setOnClickListener(mLoadingButtonClickListener);
                        loadingViewButton.setVisibility(View.VISIBLE);
                    }
                } else if (mLoadingViewButtonId > 0) {
                    View loadingViewButton = vgLoading.findViewById(mLoadingViewButtonId);
                    loadingViewButton.setVisibility(View.GONE);
                }
            }
            if (vgError == null) {
                vgError = (ViewGroup) mInflater.inflate(R.layout.view_error,
                        null);
                if (!(mErrorMessageViewId > 0))
                    mErrorMessageViewId = R.id.tv_message;
                // LogUitls.print("lxx", mShowErrorButton + "" + mErrorViewButtonId
                // + mErrorButtonClickListener);
                if (mShowErrorButton && mErrorViewButtonId > 0
                        && mErrorButtonClickListener != null) {
                    View errorViewButton = vgError
                            .findViewById(mErrorViewButtonId);
                    if (errorViewButton != null) {
                        errorViewButton
                                .setOnClickListener(mErrorButtonClickListener);
                        errorViewButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private static Animation getRotateAnimation() {
//        final RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
//                Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,
//                .5f);
//        rotateAnimation.setDuration(1500);
//        rotateAnimation.setInterpolator(new LinearInterpolator());
//        rotateAnimation.setRepeatCount(Animation.INFINITE);
//        return rotateAnimation;
//    }

    // ---------------------------
    // public methods
    // ---------------------------

//    /**
//     * Constructor
//     *
//     * @param context the mContext (preferred mContext is any activity)
//     */
//    public EmptyLayout(Context context) {
//        mContext = context;
//        mInflater = (LayoutInflater) mContext
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        setDefaultValues();
//    }

    boolean isShowNormal;//针对LISTVIEW 显示绑定到数据EMPTYLAYOUT 内

    public EmptyLayout(Context context, View listView, boolean isShowNormal) {
        this.isShowNormal = isShowNormal;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = listView;
        setDefaultValues();
    }

    /**
     * Constructor
     *
     * @param context   the mContext (preferred mContext is any activity)
     * @param childView the list view for which this library is being used
     */
    public EmptyLayout(Context context, View childView) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = childView;
        setDefaultValues();
    }

    public EmptyLayout(Context context, RelativeLayout relativeLayout) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewContentView = relativeLayout;
        setDefaultValues();
    }

    /**
     * Shows the empty layout if the list is empty
     */
    public void showEmpty() {
        this.mEmptyType = TYPE_EMPTY;
        changeEmptyType();
    }

    /**
     * Shows loading layout if the list is empty
     */
    public void showLoading() {
        this.mEmptyType = TYPE_LOADING;
        changeEmptyType();
    }

    /**
     * Shows error layout if the list is empty
     */
    public void showError() {
        this.mEmptyType = TYPE_ERROR;
        changeEmptyType();
    }

    /***
     * 设置里面的view看不见
     */
    public void setViewGone() {
        if (vgEmpty != null)
            vgEmpty.setVisibility(View.GONE);
        if (vgError != null)
            vgError.setVisibility(View.GONE);
        if (vgLoading != null) {
            vgLoading.setVisibility(View.GONE);
        }
    }

    public interface IOnEmptyDataChangedListener {
        // 显示错误
        void showError();

        // 显示空
        void showEmpty();

        // 显示加载
        void showLoading();

        // 显示普通
        void showNormal();

        // 得到emptylayout
        EmptyLayout getEmptyLayout();
    }
}
