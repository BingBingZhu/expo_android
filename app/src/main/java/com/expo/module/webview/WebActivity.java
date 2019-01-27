package com.expo.module.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;

import com.amap.api.maps.AMap;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.ExpoApp;
import com.expo.base.utils.FileUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.WebContract;
import com.expo.entity.Coupon;
import com.expo.entity.RichText;
import com.expo.entity.Venue;
import com.expo.map.LocationManager;
import com.expo.module.contacts.ContactsActivity;
import com.expo.module.login.LoginActivity;
import com.expo.module.map.NavigationActivity;
import com.expo.module.share.ShareUtil;
import com.expo.pay.JsMethod;
import com.expo.utils.Constants;
import com.expo.utils.LocalBroadcastUtil;
import com.expo.widget.CustomDefaultDialog;
import com.expo.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

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
    private Location mLocation;

    Coupon mCoupon;

    Handler mHandler = new Handler();

    @Override
    protected int getContentView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        return R.layout.activity_web;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra(Constants.EXTRAS.EXTRA_IS_START_LOCATION, false)) {
            LocationManager.getInstance().registerLocationListener(barrierLocationChangeListener);
        }
        mUrl = getIntent().getStringExtra(Constants.EXTRAS.EXTRA_URL);
        if (getIntent().getBooleanExtra(Constants.EXTRAS.EXTRA_SHOW_TITLE, true)) {
            mTitle = getIntent().getStringExtra(Constants.EXTRAS.EXTRA_TITLE);
            int titleColorStyle = getIntent().getIntExtra(Constants.EXTRAS.EXTRA_TITLE_COLOR_STYLE, 0);
            setTitle(titleColorStyle, mTitle);
        } else {
            setTitle(0, "");
            setTitleVisibility(View.GONE);
        }
        getTitleView().setOnClickListener((v) -> {
            mX5View.loadUrl("javascript:isclose()");
            if (mX5View.canGoBack()) {
                mX5View.goBack();
                return;
            }
            onBackPressed();
        });
        mX5View.setWebChromeClient(webChromeClient);
        mX5View.addJavascriptInterface(new WebActivity.JsHook(), "hook");
        mX5View.addJavascriptInterface(new JsMethod(this, mX5View), "androidFunction");
        loadUrl(mUrl);
        mShareUtil = new ShareUtil(this);
    }

    private void loadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            mX5View.loadUrl(Constants.URL.HTML_404);
        } else {
            if (url.matches(Constants.Exps.NUMBER)) {
                int rulId = Integer.parseInt(url);
                mPresenter.getUrlById(rulId);
            } else {
                if (!url.startsWith("http") && !url.startsWith("https")
                        && !url.startsWith("file") && !url.startsWith("javascript:")
                        && !url.startsWith("www")) {
                    url = Constants.URL.FILE_BASE_URL + url;

                }
                mX5View.loadUrl(url);
            }
        }
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
        Intent in = new Intent(context, WebActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_TITLE, title == null ? "" : title);
        in.putExtra(Constants.EXTRAS.EXTRA_URL, url);
        context.startActivity(in);
    }

    public static void startActivity(@NonNull Context context, @NonNull String url, @Nullable String title, String jsonData) {
        Intent in = new Intent(context, WebActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_TITLE, title == null ? "" : title);
        in.putExtra(Constants.EXTRAS.EXTRA_URL, url);
        in.putExtra(Constants.EXTRAS.EXTRA_JSON_DATA, jsonData);
        context.startActivity(in);
    }

    public static void startActivity(@NonNull Context context, @NonNull String url, @Nullable String title, boolean showTitle) {
        Intent in = new Intent(context, WebActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_TITLE, title == null ? "" : title);
        in.putExtra(Constants.EXTRAS.EXTRA_URL, url);
        in.putExtra(Constants.EXTRAS.EXTRA_SHOW_TITLE, showTitle);
        context.startActivity(in);
    }

    public static void startActivity(@NonNull Context context, @NonNull String url, @Nullable String title, int titleColorStyle) {
        Intent in = new Intent(context, WebActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_TITLE, title == null ? "" : title);
        in.putExtra(Constants.EXTRAS.EXTRA_URL, url);
        in.putExtra(Constants.EXTRAS.EXTRA_TITLE_COLOR_STYLE, titleColorStyle);
        context.startActivity(in);
    }

    public static void startActivity(@NonNull Context context, @NonNull String url, @Nullable String title, int titleColorStyle, boolean isStartLocation) {
        Intent in = new Intent(context, WebActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_TITLE, title == null ? "" : title);
        in.putExtra(Constants.EXTRAS.EXTRA_URL, url);
        in.putExtra(Constants.EXTRAS.EXTRA_TITLE_COLOR_STYLE, titleColorStyle);
        in.putExtra(Constants.EXTRAS.EXTRA_IS_START_LOCATION, isStartLocation);
        context.startActivity(in);
    }

    @Override
    public void returnRichText(RichText richText) {
        String content = "<html><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head><body><div>" + richText.getContent() + "</div></body>";
        mX5View.loadData(content, "text/html;charset=utf8", "UTF-8");
    }

    @Override
    public void logoutResp() {
        LoginActivity.startActivity(getContext());
    }

    @Override
    public void useCoupon() {
        mX5View.loadUrl("javascript:useCouponId()");
    }

    /**
     * JavascriptInterface
     */
    public class JsHook {
        @JavascriptInterface
        public void weixin() {
            share(Wechat.NAME);
        }

        @JavascriptInterface
        public void qq() {
            share(QQ.NAME);
        }

        @JavascriptInterface
        public void weibo() {
            share(SinaWeibo.NAME);
        }

        @JavascriptInterface
        /**
         * 分享截图二维码
         */
        public void shareQRCode() {
            Bitmap bitmap = captureScreen(WebActivity.this);
            String filePath = FileUtils.saveScreenShot(bitmap);
            ShareUtil.showShare(getContext(), null, null, filePath, null, null);
        }

        @JavascriptInterface
        /**
         * 截图保存本地
         */
        public void printscreen() {
            Bitmap bitmap = captureScreen(WebActivity.this);
            String filePath = FileUtils.saveScreenShot(bitmap);
            new CustomDefaultDialog(getContext())
                    .setContent("已保存至：" + filePath)
                    .setOnlyOK()
                    .setCancelable(false)
                    .show();
        }

        @JavascriptInterface
        public void unLogin() {
            mHandler.post(() -> showForceSingOutDialog());
        }

        @JavascriptInterface
        public void close() {
            finish();
        }

        @JavascriptInterface
        public void selectContext(int count, String ids) {
            ContactsActivity.startActivity(WebActivity.this, true, count, ids);
        }

        @JavascriptInterface
        public void setTitle(String titleText, int color) {
            runOnUiThread(() -> {
                WebActivity.this.setTitle(color, titleText);
            });
        }

        @JavascriptInterface
        public void setTitle(String titleText) {
            runOnUiThread(() -> {
                WebActivity.this.setTitle(BaseActivity.TITLE_COLOR_STYLE_WHITE, titleText);
            });
        }

        @JavascriptInterface
        public void setCouponId(String couponId) {
            if (mCoupon == null) mCoupon = new Coupon();
            mCoupon.couponId = couponId;
            Intent intent = new Intent(WebActivity.this, CaptureActivity.class);
            startActivityForResult(intent, Constants.RequestCode.REQ_TO_QRCODE);
        }

        /**
         * 减少用户积分
         *
         * @param integral 减少的积分数
         */
        @JavascriptInterface
        public void reduceUserPoints(int integral) {
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRAS.EXTRA_USER_POINTS, integral);
            LocalBroadcastUtil.sendBroadcast(getContext(), intent, Constants.Action.ACTION_REDUCE_USER_POINTS);
        }

        /**
         * 用户是否在园区
         *
         * @return
         */
        @JavascriptInterface
        public void isInPark() {
            LocationManager.getInstance().registerLocationListener(buyLocationChangeListener);
        }

        /**
         * 无障碍服务 导航到服务中心
         */
        @JavascriptInterface
        public void goToServiceCenter() {
            if (null == mLocation) {
                ToastHelper.showShort(R.string.trying_to_locate);
                return;
            } else {
                if (mPresenter.checkInPark(mLocation)) {
                    Venue venue = mPresenter.getNearbyServiceCenter(mLocation);
                    if (venue != null) {
                        NavigationActivity.startActivity(getContext(), venue);
                    } else {
                        ToastHelper.showShort(R.string.no_service_agencies);
                    }
                } else {
                    ToastHelper.showShort(R.string.unable_to_provide_service);
                }
            }
        }

        /**
         * 设置横竖屏显示
         *
         * @Param orientation 1:竖屏   2:横屏
         */
        @JavascriptInterface
        public void setOrientation(int orientation) {
            if (orientation == getResources().getConfiguration().orientation) return;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        /**
         * 获取订单信息
         *
         * @return
         */
        @JavascriptInterface
        public String getOrderInfo() {
            return getIntent().getStringExtra(Constants.EXTRAS.EXTRA_JSON_DATA);
        }

    }

    private AMap.OnMyLocationChangeListener barrierLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (null != location && location.getLatitude() != 0) {
                mLocation = location;
            }
        }
    };

    private AMap.OnMyLocationChangeListener buyLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (null != location && location.getLatitude() != 0) {
                mX5View.loadUrl("javascript:gLocation(" + mPresenter.checkInPark(location) + ")");
                LocationManager.getInstance().unregisterLocationListener(buyLocationChangeListener);
            }
        }
    };

    private void showForceSingOutDialog() {
        CustomDefaultDialog dialog = new CustomDefaultDialog(ExpoApp.getApplication().getTopActivity());
        dialog.setContent(R.string.the_account_is_abnormal_please_log_in_again)
                .setOnlyOK()
                .setCancelable(false)
                .setOnOKClickListener(v -> {
                    runOnUiThread(() -> mPresenter.logout());
                    dialog.dismiss();
                }).show();
    }

    private void share(String name) {
        Bitmap bitmap = captureScreen(WebActivity.this);
        String filePath = FileUtils.saveScreenShot(bitmap);
        mShareUtil.setImagePath(filePath);
        mShareUtil.doShare(name, filePath);
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
        cv.setDrawingCacheEnabled(true);
        cv.buildDrawingCache();
        Bitmap bmp = cv.getDrawingCache();
        if (bmp == null) {
            return null;
        }
        bmp.setHasAlpha(false);
        bmp.prepareToDraw();
        return bmp;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.RequestCode.REQ_TO_CONTACTS) {
                String value = data.getStringExtra(Constants.EXTRAS.EXTRAS);
                value = value.replace("\"id\"", "\"value\"");
                mX5View.loadUrl("javascript:setContext('" + value + "')");
            } else if (requestCode == Constants.RequestCode.REQ_TO_QRCODE) {
                String str = data.getStringExtra(Constant.CODED_CONTENT);
                if (str.startsWith("buss:")) {
                    mCoupon.vrCode = str;
                    mPresenter.setUsedCoupon(mCoupon);
                } else {
                    ToastHelper.showShort(R.string.error_qrcode);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        LocationManager.getInstance().unregisterLocationListener(barrierLocationChangeListener);
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
