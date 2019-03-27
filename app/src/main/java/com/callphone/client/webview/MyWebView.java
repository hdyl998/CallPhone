package com.callphone.client.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hd.base.dialog.SimpleDialog;
import com.hd.base.maininterface.IComCallBacks;
import com.hd.utils.DensityUtils;
import com.hd.utils.GoUtils;
import com.hd.utils.Network;
import com.hd.utils.log.impl.LogUitls;
import com.hd.view.EmptyLayout;

/**
 * Date:2017/11/7 14:00
 * Author:liugd
 * Modification:
 * ................
 * 佛祖保佑，永无BUG
 **/

public class MyWebView extends WebView {


    private final static String TAG = MyWebView.class.getSimpleName();
    public final static String NATIVE_API = "nativeApi";

    private final static String JS_STRING;
    private MyWebChromeClient chromeClient;

    static {
        String temp = null;
        JS_STRING = temp;
    }

    IComCallBacks<Boolean> h5CloseCallBacks;
    //当前页是否能回退
    boolean isCurentPageCanGoBack = false;


    public void setCurentPageCanGoBack(boolean curentPageCanGoBack) {
        isCurentPageCanGoBack = curentPageCanGoBack;
        if (h5CloseCallBacks != null) {
            h5CloseCallBacks.call(curentPageCanGoBack);
        }
    }


    public MyWebView(Context context) {
        super(context);
        this.mContext = context;
        initViews();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initViews();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initViews();
    }

    @TargetApi(21)
    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        initViews();
    }


    IComCallBacks<Integer> onScrollChangedCallback;


    /***
     * 滚动的callBack
     * @param onScrollChangedCallback
     */
    public void setOnScrollChangedCallback(IComCallBacks<Integer> onScrollChangedCallback) {
        this.onScrollChangedCallback = onScrollChangedCallback;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (onScrollChangedCallback != null) {
            onScrollChangedCallback.call(t);
        }
    }


    private EmptyLayout emptyLayout;

    public void setEmptyLayout(EmptyLayout emptyLayout) {
        this.emptyLayout = emptyLayout;
    }

    //当前页回退改变的通用回调
    public void setH5CloseCallBacks(IComCallBacks<Boolean> h5CloseCallBacks) {
        this.h5CloseCallBacks = h5CloseCallBacks;
    }

    IComCallBacks<View> pageFinishCallBacks;

    public void setPageFinishCallBacks(IComCallBacks<View> pageFinishCallBacks) {
        this.pageFinishCallBacks = pageFinishCallBacks;
    }

    private WebViewProgressBar progressBar;// 等待对话框
    private Context mContext;


    private void initViews() {


        progressBar = new WebViewProgressBar(mContext);
        this.addView(progressBar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(1)));
        WebSettings webSettings = getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);        //支持插件
        webSettings.setNeedInitialFocus(true);//当webview调用requestFocus时为webview设置节点
        webSettings.setSupportMultipleWindows(true);//设置支持多窗口
        webSettings.setTextZoom(100);//禁止系统缩放字体大小
//        webSettings.setSupportZoom(true);//设置支持缩放
//        webSettings.setBuiltInZoomControls(true);//设置支持缩放
//       webSettings.setUseWideViewPort(true);//用户可以可以网页比例
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webSettings.setJavaScriptEnabled(true);// 可以使用javaScriptEnalsed
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//js可以自动打开窗口
        webSettings.setAllowFileAccess(true);//支持引用文件
        webSettings.setAppCacheEnabled(true);        //设置支持本地存储
        webSettings.setDomStorageEnabled(true);//可以手动开启DOM Storage
        if (Build.VERSION.SDK_INT >= 17) {// 不需要请求控制直接播放媒体文件
            webSettings.setMediaPlaybackRequiresUserGesture(false);
        }
        //可能遇到的问题：有可能会遇到有的图片加载不出来，那是因为webview 从Lollipop(5.0)开始webview默认不允许混合模式，https当中不能加载http资源，需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        setWebViewClient(new MyWebViewClient());
        setWebChromeClient(chromeClient = new MyWebChromeClient());// 设置浏览器可弹窗
        setDownloadListener(new MyWebViewDownLoadListener());//支持下载
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            GoUtils.goDownload(mContext, url);
        }
    }

    /***
     * 加载js
     */
    private void loadJS() {
        if (JS_STRING != null)
            loadUrl(JS_STRING);
    }

    private final class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 处理自定义scheme
            if (!url.startsWith("http")) {//球球是道开头,则为自已的处理的
                LogUitls.print(TAG, "shouldOverrideUrlLoading+处理自定义scheme");
                GoUtils.goSchemeIntent(mContext, url);
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (url != null && url.length() < 500) {
                LogUitls.print("url-新页面开始", url);
            }
            progressBar.startProgress();
            loadJS();
            isErrorNotResolved = false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url != null && url.length() < 500) {
                LogUitls.print("url-新页面", "页面结束" + url);
            }


            progressBar.finishProgress();
            //页面完成时的回调
            if (pageFinishCallBacks != null) {
                pageFinishCallBacks.call(view);
            }
            if (h5CloseCallBacks != null) {
                boolean isCanGoBack = MyWebView.this.canGoBack();
                if (isCanGoBack != isCurentPageCanGoBack) {
                    isCurentPageCanGoBack = isCanGoBack;
                    h5CloseCallBacks.call(isCurentPageCanGoBack);
                    LogUitls.print(TAG, "是否回退改变" + isCurentPageCanGoBack);
                }
            }
            loadJS();

            //加载完成回调
            if (callBackLoadComplted != null) {
                callBackLoadComplted.call(null);
            }


            if (emptyLayout != null) {
                if (isErrorNotResolved || !Network.isConnected(mContext)) {
                    MyWebView.this.setVisibility(View.GONE);
                    emptyLayout.showError();
                } else {
                    MyWebView.this.setVisibility(View.VISIBLE);
                    emptyLayout.setViewGone();
                }
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            progressBar.onError();
            LogUitls.print("onReceivedError", error + " request:" + request);
        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            LogUitls.print("onReceivedError1", failingUrl + " description:" + description + " errorCode " + errorCode);
            //description:net::ERR_NAME_NOT_RESOLVED errorCode -2
            isErrorNotResolved = errorCode == -2;
        }


        //网址是否能解析
        private boolean isErrorNotResolved = false;
    }


//    //    private H5SelectFileDialog fileChooseDialog;
//    private ValueCallback valueCallback;//回调
//    private boolean isHightLevel;//高版本要传数组


    /**
     * 浏览器可弹窗
     *
     * @author Administrator
     */
    private final class MyWebChromeClient extends WebChromeClient {


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.onProgressChanged(newProgress);
        }

        boolean mConfirmed = false;

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 final JsResult result) {
            SimpleDialog.create(mContext).setTvContent(message)
                    .setSingleButton("确定")
                    .setOnClickListener(new SimpleDialog.SimpleDialogClick() {
                        @Override
                        public void onSingleClick(SimpleDialog simpleDialog) {
                            mConfirmed = true;
                            result.confirm();
                        }
                    })
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (!mConfirmed) {
                                result.cancel();
                            }
                        }
                    });
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            SimpleDialog.create(mContext).setTvContent(message)
                    .setBtnLeft("取消")
                    .setBtnRight("确定")
                    .setCancelableDialog(false)
                    .setOnClickListener(new SimpleDialog.SimpleDialogClick() {
                        @Override
                        public void onLeftClick(SimpleDialog simpleDialog) {
                            result.cancel();
                        }

                        @Override
                        public void onRightClick(SimpleDialog simpleDialog) {
                            result.confirm();
                        }
                    });
            return true;
        }
    }


//    public static final int FILE_CHOOSER_RESULT_CODE = 8833;

    private final class JsObject {// @JavascriptInterface是为了支4.2及以上的js交互
    }


    private IComCallBacks onPublishSendPokerCallBack;


    private IComCallBacks callBackLoadComplted;

    //当前页回退改变的通用回调
    public void setCallBackLoadComplted(IComCallBacks callBackLoadComplted) {
        this.callBackLoadComplted = callBackLoadComplted;
    }


    public void setOnPublishSendPokerCallBack(IComCallBacks onPublishSendPokerCallBack) {
        this.onPublishSendPokerCallBack = onPublishSendPokerCallBack;
    }



    /***
     * 当销毁时
     */
    public void onDestory() {
        try {
            this.setPageFinishCallBacks(null);
            this.setH5CloseCallBacks(null);
            this.setCallBackLoadComplted(null);
            this.setOnScrollChangedCallback(null);
            this.clearHistory();
            this.destroy();
            if (this.getParent() != null) {
                ((ViewGroup) this.getParent()).removeView(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
