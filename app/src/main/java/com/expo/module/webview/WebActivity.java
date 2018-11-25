package com.expo.module.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.WebContract;
import com.expo.entity.RichText;
import com.expo.utils.Constants;
import com.expo.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;

/*
 * 加载HTML页面的通用页
 */
public class WebActivity extends BaseActivity<WebContract.Presenter> implements WebContract.View {

    @BindView(R.id.common_x5)
    X5WebView mX5View;
    @BindView(R.id.common_progress)
    ProgressBar mProgressView;
    private String mUrl;
    private String mTitle;

    @Override
    protected int getContentView() {
        getWindow().setFormat( PixelFormat.TRANSLUCENT );
        return R.layout.activity_web;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mUrl = getIntent().getStringExtra( Constants.EXTRAS.EXTRA_URL );
        if (getIntent().getBooleanExtra( Constants.EXTRAS.EXTRA_SHOW_TITLE, true )) {
            mTitle = getIntent().getStringExtra( Constants.EXTRAS.EXTRA_TITLE );
            setTitle( 0, mTitle );
        } else {
            setTitle( 0, "" );
            setTitleVisibility( View.GONE );
        }
        mX5View.setWebChromeClient( webChromeClient );
        loadUrl( mUrl );
    }

    private void loadUrl(String url) {
        try {
            int rulId = Integer.parseInt( url );
            mPresenter.getUrlById( rulId );
        } catch (Exception e) {
            if (!url.startsWith( "http" ) && !url.startsWith( "https" )
                    && !url.startsWith( "file" ) && !url.startsWith( "javascript:" )
                    && !url.startsWith( "www" )) {
                url = Constants.URL.FILE_BASE_URL + url;
            }
            mX5View.loadUrl( url );
        }
    }


    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
            ToastHelper.showShort( s1 );
            jsResult.confirm();
            return true;
        }

        @Override
        public void onProgressChanged(WebView webView, int i) {                                     //加载进度条处理
            super.onProgressChanged( webView, i );
            if (i == 100) {
                mProgressView.setVisibility( View.GONE );
            } else {
                mProgressView.setVisibility( View.VISIBLE );
                mProgressView.setProgress( i );
            }
        }
    };


    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context, @NonNull String url, @Nullable String title) {
        Intent in = new Intent( context, WebActivity.class );
        in.putExtra( Constants.EXTRAS.EXTRA_TITLE, title == null ? "" : title );
        in.putExtra( Constants.EXTRAS.EXTRA_URL, url );
        context.startActivity( in );
    }

    public static void startActivity(@NonNull Context context, @NonNull String url, @Nullable String title, boolean showTitle) {
        Intent in = new Intent( context, WebActivity.class );
        in.putExtra( Constants.EXTRAS.EXTRA_TITLE, title == null ? "" : title );
        in.putExtra( Constants.EXTRAS.EXTRA_URL, url );
        in.putExtra( Constants.EXTRAS.EXTRA_SHOW_TITLE, showTitle );
        context.startActivity( in );
    }

    @Override
    public void returnRichText(RichText richText) {
        mX5View.loadData( richText.getContent(), "text/html;charset=utf8", "UTF-8" );
    }
}
