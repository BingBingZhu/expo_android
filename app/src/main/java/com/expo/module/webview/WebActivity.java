package com.expo.module.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.ExpoApp;
import com.expo.base.utils.FileUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.WebContract;
import com.expo.entity.RichText;
import com.expo.module.login.LoginActivity;
import com.expo.module.share.ShareUtil;
import com.expo.utils.Constants;
import com.expo.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import butterknife.BindView;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

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
    private ShareUtil mShareUtil;

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
            int titleColorStyle = getIntent().getIntExtra( Constants.EXTRAS.EXTRA_TITLE_COLOR_STYLE, 0 );
            setTitle( titleColorStyle, mTitle );
        } else {
            setTitle( 0, "" );
            setTitleVisibility( View.GONE );
        }
        getTitleView().setOnClickListener( (v) -> {
            mX5View.loadUrl("javascript:isclose()");
            if (mX5View.canGoBack()) {
                mX5View.goBack();
                return;
            }
            onBackPressed();
        } );
        mX5View.setWebChromeClient( webChromeClient );
        mX5View.addJavascriptInterface( new WebActivity.JsHook(), "hook" );
        loadUrl( mUrl );
        mShareUtil = new ShareUtil( this );
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

    @Override
    public void onBackPressed() {
        mX5View.loadUrl("javascript:isclose()");
        if (mX5View.canGoBack()) {
            mX5View.goBack();
            return;
        }
        super.onBackPressed();
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

    public static void startActivity(@NonNull Context context, @NonNull String url, @Nullable String title, int titleColorStyle) {
        Intent in = new Intent( context, WebActivity.class );
        in.putExtra( Constants.EXTRAS.EXTRA_TITLE, title == null ? "" : title );
        in.putExtra( Constants.EXTRAS.EXTRA_URL, url );
        in.putExtra( Constants.EXTRAS.EXTRA_TITLE_COLOR_STYLE, titleColorStyle );
        context.startActivity( in );
    }

    @Override
    public void returnRichText(RichText richText) {
        String content = "<html><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head><body><div>" + richText.getContent() + "</div></body>";
        mX5View.loadData(content, "text/html;charset=utf8", "UTF-8");
    }

    @Override
    public void logoutResp() {
        LoginActivity.startActivity( getContext() );
    }

    /**
     * JavascriptInterface
     */
    public class JsHook {
        @JavascriptInterface
        public void weixin() {
            share( Wechat.NAME );
        }

        @JavascriptInterface
        public void qq() {
            share( QQ.NAME );
        }

        @JavascriptInterface
        public void weibo() {
            share( SinaWeibo.NAME );
        }

        @JavascriptInterface
        public void unLogin() {
            showForceSingOutDialog();
        }

        @JavascriptInterface
        public void close(){
            finish();
        }
    }

    private void showForceSingOutDialog() {
        new AlertDialog.Builder(ExpoApp.getApplication().getTopActivity())
            .setMessage(R.string.the_account_is_abnormal_please_log_in_again)
            .setCancelable(false)
            .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.logout();
//                    ExpoApp.getApplication().setUser(null);
//                    LoginActivity.startActivity(ExpoApp.getApplication().getTopActivity());
                }
            })
            .show();
    }

    private void share(String name) {
        Bitmap bitmap = captureScreen( WebActivity.this );
        String filePath = FileUtils.saveScreenShot( bitmap );
        mShareUtil.setImagePath( filePath );
        mShareUtil.doShare( name, filePath );
    }

    /**
     * 获取整个窗口的截图
     *
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    private Bitmap captureScreen(Activity context) {
        View cv = context.getWindow().getDecorView();
        cv.setDrawingCacheEnabled( true );
        cv.buildDrawingCache();
        Bitmap bmp = cv.getDrawingCache();
        if (bmp == null) {
            return null;
        }
        bmp.setHasAlpha( false );
        bmp.prepareToDraw();
        return bmp;
    }

}
