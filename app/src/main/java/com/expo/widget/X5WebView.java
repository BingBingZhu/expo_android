package com.expo.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.expo.base.ExpoApp;
import com.expo.base.utils.ToastHelper;
import com.expo.map.LocationManager;
import com.expo.module.webview.WebTemplateActivity;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;

public class X5WebView extends WebView {
    private static final String APP_NAME_UA = "LUCASPARK";
    private WebViewClient client = new WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl( url );
            return true;
        }
    };

    private WebChromeClient chromeClient = new WebChromeClient() {

        @Override
        public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
            ToastHelper.showShort( s1 );
            jsResult.confirm();
            return true;
        }
    };

    public X5WebView(Context arg0) {
        this( arg0, null );
    }

    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(Context arg0, AttributeSet arg1) {
        super( arg0, arg1 );
        this.setWebViewClient( client );
        this.setWebChromeClient( chromeClient );
        // this.setWebChromeClient(chromeClient);
        // WebStorage webStorage = WebStorage.getInstance();
        initWebViewSettings();
        this.getView().setClickable( true );
        setBackgroundColor( Color.TRANSPARENT );
    }

    private void initWebViewSettings() {
        setWebContentsDebuggingEnabled( true );

        WebSettings webSetting = this.getSettings();
        webSetting.setDefaultTextEncodingName( "UTF-8" );
        webSetting.setUserAgentString( webSetting.getUserAgentString() + APP_NAME_UA );
        webSetting.setAllowFileAccessFromFileURLs( true );
        webSetting.setJavaScriptEnabled( true );
        webSetting.setJavaScriptCanOpenWindowsAutomatically( true );
        webSetting.setAllowFileAccess( true );
        webSetting.setLayoutAlgorithm( WebSettings.LayoutAlgorithm.NARROW_COLUMNS );
        webSetting.setSupportZoom( false );
        webSetting.setBuiltInZoomControls( true );
        webSetting.setUseWideViewPort( true );
        webSetting.setSupportMultipleWindows( true );
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled( true );
         webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled( true );
        webSetting.setGeolocationEnabled( true );
        webSetting.setAppCacheMaxSize( Long.MAX_VALUE );
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState( WebSettings.PluginState.ON_DEMAND );
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode( WebSettings.LOAD_NO_CACHE );

        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
    }

    public void removeTencentAd(final Activity activity) {
        //去除X5浏览器再全屏视频时出现腾讯广告
        activity.getWindow().getDecorView().addOnLayoutChangeListener( (view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            ArrayList<View> outView = new ArrayList<>();
            activity.getWindow().getDecorView().findViewsWithText( outView, "QQ浏览器", View.FIND_VIEWS_WITH_TEXT );
            if (outView != null && outView.size() > 0) {
                outView.get( 0 ).setVisibility( View.GONE );
            }
        } );
    }

    @Override
    public void destroy() {
        clearCache( true );
        clearHistory();
        removeAllViews();
        super.destroy();
    }
}
