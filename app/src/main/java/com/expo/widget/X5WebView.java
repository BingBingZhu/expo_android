package com.expo.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.expo.BuildConfig;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.network.Http;
import com.expo.utils.LanguageUtil;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;

public class X5WebView extends WebView {
    private static final String APP_NAME_UA = "LUCASPARK";
    private String mUrl;
    private WebViewClient client = new WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!TextUtils.isEmpty(url) && (url.startsWith("http") || url.startsWith("javascript"))) {
                view.loadUrl(url);
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            if (!error404(webResourceRequest.getUrl().toString(), webResourceError.getErrorCode())) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }
        }

        @Override
        public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
            if (!error404(webResourceRequest.getUrl().toString(), webResourceResponse.getStatusCode())) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
            }
        }

        private boolean error404(String path, int code) {
            if (code == 404 && path.startsWith(mUrl)) {
                stopLoading();
                clearView();
                load404Page();
                return true;
            }
            return false;
        }
    };

    @Override
    public void loadUrl(String s) {
        if (TextUtils.isEmpty(s)) {
            load404Page();
            return;
        }
        mUrl = s;
        LogUtils.e("aaaa", "-------loadUrl----" + mUrl);
        super.loadUrl(s);
    }

    private void load404Page() {
        loadUrl("file:///android_asset/web/404.html");
    }

    private WebChromeClient chromeClient = new WebChromeClient() {

        @Override
        public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
            ToastHelper.showShort(s1);
            jsResult.confirm();
            return true;
        }
    };

    public X5WebView(Context arg0) {
        this(arg0, null);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        this.setWebViewClient(client);
        this.setWebChromeClient(chromeClient);
        initWebViewSettings();
        this.getView().setClickable(true);
        setBackgroundColor(Color.TRANSPARENT);
    }

    private void initWebViewSettings() {
        setWebContentsDebuggingEnabled(true);

        WebSettings webSetting = this.getSettings();
        webSetting.setDefaultTextEncodingName("UTF-8");
        webSetting.setAllowFileAccessFromFileURLs(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
//        String ua = "Mozilla/5.0 (Linux; Android 9.0; MIX 3 Build/PKQ1.180729.001; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044408 Mobile Safari/537.36 MMWEBID/3385 MicroMessenger/7.0.1380(0x2700003C) Process/tools NetType/WIFI Language/zh_CN Expo/" + BuildConfig.VERSION_NAME;
        webSetting.setUserAgentString(webSetting.getUserAgentString() + "MMWEBID/3385 MicroMessenger/7.0.1380(0x2700003C) Process/tools NetType/" + Http.getNetworkType() + " Language/" + LanguageUtil.chooseTest("zh-CN", "en-US") + " Expo/" + BuildConfig.VERSION_NAME);

        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
    }

    public void removeTencentAd(final Activity activity) {
        //去除X5浏览器再全屏视频时出现腾讯广告
        activity.getWindow().getDecorView().addOnLayoutChangeListener((view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            ArrayList<View> outView = new ArrayList<>();
            activity.getWindow().getDecorView().findViewsWithText(outView, "QQ浏览器", View.FIND_VIEWS_WITH_TEXT);
            if (outView != null && outView.size() > 0) {
                outView.get(0).setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void destroy() {
        clearCache(true);
        clearHistory();
        removeAllViews();
        super.destroy();
    }
}
