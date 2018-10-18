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
import com.expo.utils.Constants;
import com.expo.widget.AppBarView;
import com.expo.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 加载HTML页面的通用页
 */
public class WebActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.common_title)
    AppBarView mTitleView;
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
        mTitle = getIntent().getStringExtra( Constants.EXTRAS.EXTRA_TITLE );
        mUrl = getIntent().getStringExtra( Constants.EXTRAS.EXTRA_URL );
        mTitleView.setTitle( mTitle );
        mX5View.setWebChromeClient( webChromeClient );
        loadUrl( mUrl );
    }

    private void loadUrl(String url) {
        if (!url.startsWith( "http" ) && !url.startsWith( "https" )
                && !url.startsWith( "file" ) && !url.startsWith( "javascript:" )) {
            url = Constants.URL.FILE_BASE_URL + url;
        }
        mX5View.loadUrl( url );
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
        return false;
    }

    public static void startActivity(@NonNull Context context, @NonNull String url, @Nullable String title) {
        Intent in = new Intent( context, WebActivity.class );
        in.putExtra( Constants.EXTRAS.EXTRA_TITLE, title == null ? "" : title );
        in.putExtra( Constants.EXTRAS.EXTRA_URL, url );
        context.startActivity( in );
    }

    @Override
    @OnClick(R.id.title_back)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }
}
