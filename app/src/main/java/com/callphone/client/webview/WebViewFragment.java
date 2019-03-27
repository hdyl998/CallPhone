package com.callphone.client.webview;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.callphone.client.R;
import com.hd.base.anno.NameAnno;
import com.hd.base.fragment.IBaseTitleBarFragment;
import com.hd.base.maininterface.IComCallBacks;
import com.hd.base.theme.StatusBarColorUtils;
import com.hd.net.AppDataCollectUtils;
import com.hd.utils.log.impl.LogUitls;
import com.hd.view.EmptyLayout;
import com.hd.view.TitleBar;


/**
 * Date:2017/11/7 14:00
 * Author:liugd
 * Modification:
 * ................
 * 佛祖保佑，永无BUG
 **/

@NameAnno(name = "H5页")
public class WebViewFragment extends IBaseTitleBarFragment {

    MyWebView webView;
    EmptyLayout emptyLayout;
    //所需参数
    WebViewManager.ParamsBuilder params;

    public WebViewFragment() {
    }

    public MyWebView getWebView() {
        return webView;
    }


    private IComCallBacks onLoadCompletedCallBacks;


    public void setOnLoadCompletedCallBacks(IComCallBacks onLoadCompletedCallBacks) {
        this.onLoadCompletedCallBacks = onLoadCompletedCallBacks;
    }

    public void setParams(WebViewManager.ParamsBuilder params) {
        this.params = params;
    }

    public WebViewManager.ParamsBuilder getWebParams() {
        return params;
    }

    IComCallBacks onBackCallBacks;


    public void setOnBackCallBacks(IComCallBacks onBackCallBacks) {
        this.onBackCallBacks = onBackCallBacks;
    }

    public void loadUrl(String url) {
        if (isInited()) {
            webView.loadUrl(url);
        }
    }

    /***
     * 创建WEB参数列表，子类实现
     * @return
     */
    protected WebViewManager.ParamsBuilder createWebParams() {
        return null;
    }

//    /***
//     * 是否隐藏了statusBar
//     */
//    protected boolean isHideStatusBar;
//
//
//    public boolean isHideStatusBar() {
//        return isHideStatusBar;
//    }

    @Override
    protected void initTitleBarView() {
        //软键盘的显示方案
        mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        Object obj = mContext.getIntent().getSerializableExtra(ID_NEW_PARAM1);
        //防止mainActivity转换ID_NEW_PARAM1失败的问题
        if (obj instanceof WebViewManager.ParamsBuilder) {
            params = (WebViewManager.ParamsBuilder) obj;
        }
        if (params == null) {
            params = createWebParams();
        }
        webView = findViewByID(R.id.webView);



        //初始化emptylayout
        RelativeLayout rr = findViewById(R.id.rl_root);
        emptyLayout = new EmptyLayout(mContext, rr);
        webView.setEmptyLayout(emptyLayout);
        emptyLayout.setErrorButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyLayout.showLoading();
                webView.reload();
            }
        });

        getTitleBar().setTitleText(params.title);


        webView.loadUrl(params.absoluteUrl);


        if (params.isFragmentMode) {
            getTitleBar().setVisibility(View.GONE);
        }
        if (onLoadCompletedCallBacks != null) {
            onLoadCompletedCallBacks.call(null);
        }
    }



    private static final String TAG = "WebViewFragment";



    final static int DATA = TitleBar.ID_LEFT;


    /**
     * 重新定位到首页
     */
    protected void goFirstPage() {
        webView.loadUrl(getWebParams().absoluteUrl);
        //网页加载完成时,清空历史,且设置当前页不可后退
        webView.setCallBackLoadComplted(obj -> {
            webView.clearHistory();
            webView.setCurentPageCanGoBack(false);
            webView.setCallBackLoadComplted(null);//清空引用,不再回调
        });
    }


    @Override
    public boolean onBackPressed() {
        LogUitls.print("onBackPressed", webView.canGoBack());
        if (webView.canGoBack()) {
            webView.goBack();
            LogUitls.print("onBackPressed", "goBack");
            return true;//返回true由自已处理
        }
        LogUitls.print("onBackPressed", "false");
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_webview;
    }


    @Override
    public void onDestroy() {
        try {
            if (webView != null) {
                webView.onDestory();
                webView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
