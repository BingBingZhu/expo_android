package com.expo.module.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;

import com.baidu.speech.utils.LogUtil;
import com.blankj.utilcode.util.SizeUtils;
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
import com.expo.widget.AppBarView;
import com.expo.widget.MyScrollView;
import com.expo.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WebTemplateActivity extends BaseActivity<WebTemplateContract.Presenter> implements WebTemplateContract.View {

    private static final String TAG = "WebTemplateActivity";

    @BindView(R.id.app_title)
    AppBarView mAvTitle;
    @BindView(R.id.common_x5)
    X5WebView mX5View;
    @BindView(R.id.common_progress)
    ProgressBar mProgressView;
    private String mUrl;
    private String mTitle;
    private long id;
    private ActualScene mActualScene;
    private Encyclopedias mEncyclopedias;
    private List<Encyclopedias> mNearByVenues;

    MyScrollView.OnScrollListener mScrollListener = new MyScrollView.OnScrollListener() {
        @Override
        public void onScroll(int scrollY) {
//            mAvTitle.setBackgroundColor(Color.argb(Math.min(255, Math.max(scrollY, 0) / 3), 2, 205, 155));
            mAvTitle.setAlpha( Math.min( 1.0f, Math.max( Float.valueOf( scrollY ), 0.0f ) / (255.0f * 2) ) );
        }
    };

    @Override
    protected int getContentView() {
        getWindow().setFormat( PixelFormat.TRANSLUCENT );
        return R.layout.activity_web;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mTitle = getIntent().getStringExtra( Constants.EXTRAS.EXTRA_TITLE );
        mUrl = getIntent().getStringExtra( Constants.EXTRAS.EXTRA_URL );
        id = getIntent().getLongExtra( Constants.EXTRAS.EXTRA_DATA_ID, 0 );
        mEncyclopedias = mPresenter.loadEncyclopediaById( id );
        mActualScene = mPresenter.loadSceneByWikiId( id );
        if (mActualScene != null) {
            mNearByVenues = mPresenter.loadNeayByVenues( mActualScene );
        }

//        setTitle( 0, LanguageUtil.chooseTest( mEncyclopedias.caption, mEncyclopedias.captionEn ) );

        initTitleView();
        mAvTitle.setTitle( R.string.sence_introduction );
        mAvTitle.setTitleColor( getResources().getColor( R.color.black_333333 ) );
        mAvTitle.setBackgroundColor( Color.WHITE );
        mAvTitle.setBackImageResource( R.mipmap.ico_black_back );

        mX5View.setWebChromeClient( webChromeClient );
        mX5View.removeTencentAd( this );
        mX5View.addJavascriptInterface( new JsHook(), "hook" );
        loadUrl( mUrl + (mActualScene == null ? "&data_type=0" : "&data_type=1") );
    }

    private void initTitleView() {
        mAvTitle.setTitleColor( getResources().getColor( R.color.white ) );
        mAvTitle.setTitleSize( TypedValue.COMPLEX_UNIT_PX, SizeUtils.sp2px( 17 ) );
        mAvTitle.setBackImageResource( R.mipmap.ico_white_back );
//        mAvTitle.setAlpha(0);
    }

    private void loadUrl(String url) {
        if (!url.startsWith( "http" ) && !url.startsWith( "https" )
                && !url.startsWith( "file" ) && !url.startsWith( "javascript:" )
                && !url.startsWith( "www" )) {
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
        return true;
    }

    public static void startActivity(@NonNull Context context, long id) {
        if (id <= 0) {
            ToastHelper.showShort( R.string.error_params );
            return;
        }
        Intent in = new Intent( context, WebTemplateActivity.class );
        in.putExtra( Constants.EXTRAS.EXTRA_DATA_ID, id );
        String param = "?id=" + id + "&lan=" + LanguageUtil.chooseTest( "zh", "en" );
        String url = Constants.URL.ENCYCLOPEDIAS_DETAIL_URL + param;
        in.putExtra( Constants.EXTRAS.EXTRA_URL, url );
        context.startActivity( in );
    }

    @OnClick(R.id.app_title)
    protected void clickTitle(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        mX5View.loadUrl( "javascript:leavePage()" );
        mX5View.loadUrl( "about:blank" );
        super.onDestroy();
    }

    @Override
    public void getActualSceneDataByIdRes(ActualScene actualScene) {
        ParkMapActivity.startActivity( getContext(), actualScene.getId() );
    }

    /**
     * JavascriptInterface
     */
    public class JsHook {
        @JavascriptInterface
        public String getDataById(int id) {
            return mPresenter.toJson( mEncyclopedias );
        }

        @JavascriptInterface
        public String loadNearByVenues(int id) {
//            return mPresenter.toJson( mNearByVenues );
            return "[{\"distance\":\"30米\",\"id\":29,\"typeid\":1,\"typename\":\"场馆\",\"typenameen\":\"Venues\",\"caption\":\"植物馆\",\"captionen\":\"Botanical Garden\",\"remark\":\"植物馆建筑面积约10,000平方米，建筑设计理念为“升起的地平”，建筑表面机理以植物根系为灵感，庞大的垂坠根系向下不断蔓延，将植物原本隐藏于地下的强大生命力直观呈现给参观者\",\"remarken\":\"The Botanical Museum covers an area of about 10,000 square meters.\",\"picurl\":\"7aa620f31bc94cd885a10c2e6af3b04c.jpg\",\"py\":\"\",\"linkh5url\":\"246\",\"linkh5urlen\":\"247\",\"voiceurl\":\"3528aba2ead54cd69cb38e426c397059.mp3\",\"voiceurlen\":\"ebe68398e04c4fffbe2b4539cbabff6b.mp3\",\"createtime\":\"2018-11-23 17:42:04\",\"updatetime\":\"2018-11-23 19:43:50\",\"collectioncount\":0,\"isenable\":1,\"isrecommended\":0,\"recommendedidx\":\"\",\"scores\":\"0\"},{\"distance\":\"30米\",\"id\":28,\"typeid\":1,\"typename\":\"场馆\",\"typenameen\":\"Venues\",\"caption\":\"国际馆\",\"captionen\":\"International Pav\",\"remark\":\"国际馆在北京世园会会期承担世界各国、国际组织室内展览以及举办国际园艺竞赛的功能。它由94把花伞构成，如同一片花海飘落在园区里。无论是在高空俯瞰，还是在伞下信步\",\"remarken\":\"The International Pavilion undertakes the functions of indoor exhibitions and international\",\"picurl\":\"296eccfcea76468f837d5a1b1527479a.jpg\",\"py\":\"\",\"linkh5url\":\"243\",\"linkh5urlen\":\"244\",\"voiceurl\":\"9fa12e1406a144c4818a926fced42247.mp3\",\"voiceurlen\":\"416b84e2aef54a47afe7a6789e8652bd.mp3\",\"createtime\":\"2018-11-23 17:35:46\",\"updatetime\":\"2018-11-24 12:35:54\",\"collectioncount\":0,\"isenable\":1,\"isrecommended\":1,\"recommendedidx\":\"\",\"scores\":\"0\"},{\"id\":27,\"typeid\":1,\"typename\":\"场馆\",\"typenameen\":\"Venues\",\"caption\":\"中国馆\",\"captionen\":\"China Pavilion\",\"remark\":\"师法自然，传递生态文明。效仿先人“巢居”、“穴居”的古老智慧，2019北京世园会将中国馆打造成一座会“呼吸”、有“生命”的绿色建筑。\",\"remarken\":\"Learn from nature and pass on ecological civilization. Following\",\"picurl\":\"d8f9d871cad5491bbb4f0af245ec350d.jpg\",\"py\":\"\",\"linkh5url\":\"241\",\"linkh5urlen\":\"242\",\"voiceurl\":\"f8998f7d9be44555bf4ffdcc70803e0c.mp3\",\"voiceurlen\":\"0339f8f92e8a483fb6ef5aad9b1b7b6e.mp3\",\"createtime\":\"2018-11-23 17:32:47\",\"updatetime\":\"2018-11-24 12:50:48\",\"collectioncount\":0,\"isenable\":1,\"isrecommended\":1,\"recommendedidx\":\"\",\"scores\":\"0\",\"distance\":\"30米\"}]";
        }

        @JavascriptInterface
        public void gotoDataLocation(int id) {
            runOnUiThread( () -> {
                NavigationActivity.startActivity( WebTemplateActivity.this, mActualScene, LanguageUtil.chooseTest( mEncyclopedias.voiceUrl, mEncyclopedias.voiceUrlEn ) );
            } );
        }
    }
}
