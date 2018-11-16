package com.expo.module.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.WebTemplateContract;
import com.expo.entity.ActualScene;
import com.expo.entity.Encyclopedias;
import com.expo.module.map.NavigationActivity;
import com.expo.module.map.ParkMapActivity;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;

public class WebTemplateActivity extends BaseActivity<WebTemplateContract.Presenter> implements WebTemplateContract.View {

    private static final String TAG = "WebTemplateActivity";

    @BindView(R.id.common_x5)
    X5WebView mX5View;
    @BindView(R.id.common_progress)
    ProgressBar mProgressView;
    private String mUrl;
    private String mTitle;
    private long id;
    private ActualScene mActualScene;
    private Encyclopedias mEncyclopedias;

    @Override
    protected int getContentView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        return R.layout.activity_web;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mTitle = getIntent().getStringExtra(Constants.EXTRAS.EXTRA_TITLE);
        mUrl = getIntent().getStringExtra(Constants.EXTRAS.EXTRA_URL);
        id = getIntent().getLongExtra(Constants.EXTRAS.EXTRA_DATA_ID, 0);
        mEncyclopedias = mPresenter.loadEncyclopediaById(id);
        mActualScene = mPresenter.loadSceneByWikiId(id);
        setTitle(0, LanguageUtil.chooseTest(mEncyclopedias.caption, mEncyclopedias.captionEn));
        mX5View.setWebChromeClient(webChromeClient);
        mX5View.removeTencentAd(this);
        mX5View.addJavascriptInterface(new JsHook(), "hook");
        loadUrl(mUrl + (mActualScene == null ? "&data_type=0" : "&data_type=1"));
    }

    private void loadUrl(String url) {
        if (!url.startsWith("http") && !url.startsWith("https")
                && !url.startsWith("file") && !url.startsWith("javascript:")
                && !url.startsWith("www")) {
            url = Constants.URL.FILE_BASE_URL + url;
        }
        mX5View.loadUrl(url);
    }


    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
            ToastHelper.showShort(s1);
            jsResult.confirm();
            return true;
        }

        @Override
        public void onProgressChanged(WebView webView, int i) {                                     //加载进度条处理
            super.onProgressChanged(webView, i);
            if (i == 100) {
                mProgressView.setVisibility(View.GONE);
            } else {
                mProgressView.setVisibility(View.VISIBLE);
                mProgressView.setProgress(i);
            }
        }
    };


    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context, long id) {
        if (id <= 0) {
            ToastHelper.showShort(R.string.error_params);
            return;
        }
        Intent in = new Intent(context, WebTemplateActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_DATA_ID, id);
        String param = "?id=" + id + "&lan=" + LanguageUtil.chooseTest("zh", "en");
        String url = "http://192.168.1.143:8080/dist/index.html#/introduce" + param;
        in.putExtra(Constants.EXTRAS.EXTRA_URL, url);
        context.startActivity(in);
    }

    @Override
    protected void onDestroy() {
        mX5View.loadUrl("javascript:leavePage()");
        super.onDestroy();
    }

    @Override
    public void getActualSceneDataByIdRes(ActualScene actualScene) {
        ParkMapActivity.startActivity(getContext(), actualScene.getType(), actualScene.getId());
    }

    /**
     * JavascriptInterface
     */
    public class JsHook {
        @JavascriptInterface
        public String getDataById(int id) {
            return mPresenter.toJson(mEncyclopedias);
        }

        @JavascriptInterface
        public void gotoDataLocation() {
            runOnUiThread(() -> {
                NavigationActivity.startActivity(WebTemplateActivity.this, mActualScene, LanguageUtil.chooseTest(mEncyclopedias.voiceUrl, mEncyclopedias.voiceUrlEn));
            });
        }
    }
}
