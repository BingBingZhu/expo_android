package com.expo.module.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.WebTemplateContract;
import com.expo.entity.CommonInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Venue;
import com.expo.module.map.NavigationActivity;
import com.expo.module.map.ParkMapActivity;
import com.expo.utils.ShareUtil;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.utils.LocalBroadcastUtil;
import com.expo.widget.AppBarView;
import com.expo.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class WebTemplateActivity extends BaseActivity<WebTemplateContract.Presenter> implements WebTemplateContract.View {

    private static final String TAG = "WebTemplateActivity";

    @BindView(R.id.common_x5)
    X5WebView mX5View;
    @BindView(R.id.common_progress)
    ProgressBar mProgressView;
    private String mUrl;
    private long id;
    private Venue mVenue;
    private Encyclopedias mEncyclopedias;
    private List<Encyclopedias> mRecommendEncyclopedias;
    private ShareUtil mShareUtil;

    PlatformActionListener mPlatformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            mPresenter.scoreChange(Constants.ScoreType.TYPE_SHARE, id + "");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(Platform platform, int i) {

        }
    };

    @Override
    protected int getContentView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        return R.layout.activity_web;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        id = getIntent().getLongExtra( Constants.EXTRAS.EXTRA_DATA_ID, 0 );
        mUrl = mPresenter.loadCommonInfo( CommonInfo.ENCYCLOPEDIAS_DETAIL_URL );
        mUrl += "?id=" + id + "&lan=" + LanguageUtil.chooseTest( "zh", "en" );
        mEncyclopedias = mPresenter.loadEncyclopediaById( id );
        if (mEncyclopedias==null){
            ToastHelper.showShort( R.string.error_params );
            finish();
        }
        mVenue = mPresenter.loadSceneByWikiId( id );
        loadRecommends();
        setTitle(BaseActivity.TITLE_COLOR_STYLE_WHITE, mEncyclopedias.caption);
        initTitleRightTextView();

        mX5View.setWebChromeClient(webChromeClient);
        mX5View.removeTencentAd(this);
        mX5View.addJavascriptInterface(new JsHook(), "hook");
        loadUrl(mUrl + (mVenue == null ? "&data_type=0" : "&data_type=1"));
        mShareUtil = new ShareUtil();
    }

    public void initTitleRightTextView() {
        ImageView shareView = new ImageView(this);
        ((AppBarView) getTitleView()).setRightView(shareView);
        shareView.setImageResource(R.mipmap.share_icon);
        shareView.setOnClickListener(v -> {
//            ShareUtil.doShare("", mUrl);
            ShareUtil.showShare(WebTemplateActivity.this,
                    LanguageUtil.chooseTest(mEncyclopedias.caption, mEncyclopedias.captionEn),
                    LanguageUtil.chooseTest(mEncyclopedias.remark, mEncyclopedias.remarkEn),
                    CommUtils.getFullUrl(mEncyclopedias.picUrl),
                    mUrl + "&data_type=0",
                    mPlatformActionListener);
//            mEncyclopedias
//            mShareUtil.setTitle(LanguageUtil.chooseTest(mEncyclopedias.caption, mEncyclopedias.captionEn));
//            mShareUtil.setImageUrl(CommUtils.getFullUrl(mEncyclopedias.picUrl));
//            mShareUtil.setText(LanguageUtil.chooseTest(mEncyclopedias.remark, mEncyclopedias.remarkEn));
//            mShareUtil.setUrl(mUrl + "&data_type=0");
//            mShareUtil.showPanel("image/");
        });
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
        context.startActivity(in);
    }

    @Override
    public void onBackPressed() {
        if (mX5View.canGoBack()) {
            mX5View.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mX5View.loadUrl("javascript:leavePage()");
        mX5View.loadUrl("about:blank");
        super.onDestroy();
    }

    @Override
    public void getActualSceneDataByIdRes(Venue venue) {
        ParkMapActivity.startActivity(getContext(), venue.getId());
    }

    @Override
    public void addScore() {
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRAS.EXTRA_USER_POINTS, 0);
        LocalBroadcastUtil.sendBroadcast(getContext(), intent, Constants.Action.ACTION_REDUCE_USER_POINTS);
    }

    private void loadRecommends() {
        if (mVenue != null) {
            mRecommendEncyclopedias = mPresenter.loadNeayByVenues( mVenue );
        } else {
            mRecommendEncyclopedias = mPresenter.loadRandomData( mEncyclopedias.getTypeId(), mEncyclopedias.getId() );
        }
    }

    /**
     * JavascriptInterface
     */
    public class JsHook {
        @JavascriptInterface
        public String getDataById(int id) {
            if (id == mEncyclopedias.id) {
                return mPresenter.toJson(mEncyclopedias);
            }
            return mPresenter.toJson(mPresenter.loadEncyclopediaById(id));
        }

        @JavascriptInterface
        public String loadNearByVenues(int id) {
            if (id != mEncyclopedias.id)
                loadRecommends();
            return mPresenter.toJson( mRecommendEncyclopedias );
        }

        @JavascriptInterface
        public void gotoDataLocation(int id) {
            runOnUiThread(() -> {
                NavigationActivity.startActivity(WebTemplateActivity.this, mVenue);
            });
        }

        @JavascriptInterface
        public void setTitle(String titleText) {
            runOnUiThread( () -> {
                WebTemplateActivity.this.setTitle( BaseActivity.TITLE_COLOR_STYLE_WHITE, titleText );
            } );
        }
    }
}
