package com.expo.module.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.PanoramaContract;
import com.expo.utils.Constants;
import com.expo.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;

/*
 * 全景页
 */
public class PanoramaFragment extends BaseFragment<PanoramaContract.Presenter> implements PanoramaContract.View {
    @BindView(R.id.common_x5)
    X5WebView mX5View;
    @BindView(R.id.common_progress)
    ProgressBar mProgressView;

    @Override
    public int getContentView() {
        return R.layout.fragment_panorama;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mX5View.setWebChromeClient( webChromeClient );
        String url = mPresenter.loadPanoramaUrl();
        mX5View.loadUrl( TextUtils.isEmpty( url ) ? Constants.URL.HTML_404 : url );
    }

    @Override
    public boolean isNeedPaddingTop() {
        return false;
    }

    @Override
    protected boolean hasPresenter() {
        return true;
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
}
