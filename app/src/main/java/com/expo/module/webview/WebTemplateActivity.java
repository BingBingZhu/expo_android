package com.expo.module.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.WebTemplateContract;
import com.expo.entity.ActualScene;
import com.expo.module.map.ParkMapActivity;
import com.expo.utils.Constants;
import com.expo.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import java.net.URLEncoder;

import butterknife.BindView;

public class WebTemplateActivity extends BaseActivity<WebTemplateContract.Presenter> implements WebTemplateContract.View {

    @BindView(R.id.common_x5)
    X5WebView mX5View;
    @BindView(R.id.common_progress)
    ProgressBar mProgressView;
    private String mUrl;
    private String mTitle;

    @Override
    protected int getContentView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        return R.layout.activity_web;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mTitle = getIntent().getStringExtra(Constants.EXTRAS.EXTRA_TITLE);
        mUrl = getIntent().getStringExtra(Constants.EXTRAS.EXTRA_URL);
        setTitle(0, mTitle);
        mX5View.setWebChromeClient(webChromeClient);
        mX5View.removeTencentAd(this);
        mX5View.addJavascriptInterface(new JsHook(), "hook");
        loadUrl(mUrl);
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

    public static void startActivity(@NonNull Context context, @NonNull int templateType, @Nullable Long id, @Nullable String lan, @Nullable String title) {
        Intent in = new Intent(context, WebTemplateActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_TITLE, title == null ? "" : title);
        in.putExtra(Constants.EXTRAS.EXTRA_TEMPLATE_TYPE, templateType);
        in.putExtra(Constants.EXTRAS.EXTRA_DATA_ID, id);
        String url = "";
        String param = "?id=" + id+"&lan="+ lan+"&data_type="+templateType;
        switch (templateType) {
            case 0:
                url = "http://192.168.1.143:8080/dist/index.html#/introduce"+ param;
                LogUtils.e("clickId", "-------introduce--" + id);
                LogUtils.e("clicktype", "-------introduce--" + templateType);
                LogUtils.e("clicklan", "-------introduce--" + lan);
                break;
            case 1:
                url = "file:///android_asset/dist/index.html#/introduce"+ param;
//                url = "file:///android_asset/dist/index.html#/introduce?id="+id;
                break;
            case 2:
//                url = "file:///android_asset/dist/index.html#/introduce?id="+id;
                break;
            case 3:
//                url = "file:///android_asset/dist/index.html#/introduce?id="+id;
                break;
        }
        in.putExtra(Constants.EXTRAS.EXTRA_URL, url);
        context.startActivity(in);
    }

    @Override
    public void getDataJsonByIdRes(String jsonData) {
//        jsonData=jsonData.replace("\\","\\\\");
        jsonData = URLEncoder.encode(jsonData);
        LogUtils.e("getDataJsonByIdRes", "---------" + jsonData);
        mX5View.loadUrl("javascript:dataResult('" + jsonData + "')");
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
            LogUtils.e("getDataJsonById", "---------" + id);
            mPresenter.setDataType(getIntent().getIntExtra(Constants.EXTRAS.EXTRA_TEMPLATE_TYPE, -1));
            String jsonData = mPresenter.getDataJsonById(id);
            LogUtils.e("getDataJsonByIdRes", "---------" + jsonData);
            return jsonData;
        }

        @JavascriptInterface
        public void gotoDataLocation(){
            mPresenter.getDataById(getIntent().getLongExtra(Constants.EXTRAS.EXTRA_DATA_ID, 0));
        }
    }
}
