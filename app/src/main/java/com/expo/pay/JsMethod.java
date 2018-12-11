package com.expo.pay;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.chinaums.pppay.unify.UnifyPayListener;
import com.chinaums.pppay.unify.UnifyPayPlugin;
import com.chinaums.pppay.unify.UnifyPayRequest;
import com.expo.R;
import com.expo.base.utils.ToastHelper;
import com.expo.utils.Constants;
import com.expo.widget.X5WebView;
import com.unionpay.UPPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JsMethod extends Object implements UnifyPayListener {
    private Activity mActivity;

    private static final String PAY_PARAM_URL = "http://47.92.129.164:8890/billsSDK/getPayInfo";
    private static final String TYPE_WECHAT = "wx";
    private static final String TYPE_ALI = "trade";
    private static final String TYPE_UNION = "uac";
    private X5WebView mWebView;


    public JsMethod(Activity mActivity, X5WebView webView) {
        this.mActivity = mActivity;
        this.mWebView = webView;
        UnifyPayPlugin payPlugin = UnifyPayPlugin.getInstance( mActivity );
        payPlugin.setListener( this );
    }

    @JavascriptInterface
    public void wechatPayFunction(String orderId) {
        startPayHttp( TYPE_WECHAT, orderId );
    }

    @JavascriptInterface
    public void aliPayFunction(String orderId) {
        startPayHttp( TYPE_ALI, orderId );
    }

    @JavascriptInterface
    public void unionPayFunction(String orderId) {
        startPayHttp( TYPE_UNION, orderId );
    }

    public void startPayHttp(final String payType, String orderId) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add( "msgType", payType )
                .add( "orderId", orderId )
                .build();
        final Request request = new Request.Builder()
                .url( PAY_PARAM_URL )
                .post( body )
                .build();
        Call call = client.newCall( request );
        try {
            Response response = call.execute();
            final String result = response.body().string();
            Log.e( "MYLOG", result );
            Toast.makeText( mActivity, result, Toast.LENGTH_LONG ).show();
            JSONObject json = new JSONObject( result );
            JSONObject data = json.getJSONObject( "data" );
            final JSONObject respStr = data.getJSONObject( "respStr" );
            String status = respStr.getString( "errCode" );
            // 成功
            if (status.equalsIgnoreCase( "SUCCESS" )) {
                if (TYPE_WECHAT.equals( payType )) {
                    payWX( respStr.getString( "appPayRequest" ) );
                } else if (TYPE_ALI.equals( payType )) {
                    payAli( respStr.getString( "appPayRequest" ) );
                } else if (TYPE_UNION.equals( payType )) {
                    payUnion( respStr.getString( "appPayRequest" ) );
                }
            }
        } catch (IOException e) {
            mWebView.post( new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl( "javascript:setPayResult('请求失败')" );
                }
            } );
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void exitsOrderPay(String orderMsg, String orderType) throws JSONException {
        JSONObject orderData = new JSONObject( orderMsg );
        JSONObject respStr = orderData.getJSONObject( "respStr" );
        if (orderType.equals( TYPE_WECHAT )) {
            payWX( respStr.getString( "appPayRequest" ) );
        } else if (orderType.equals( TYPE_ALI )) {
            payAli( respStr.getString( "appPayRequest" ) );
        } else if (orderType.equals( TYPE_UNION )) {
            payUnion( respStr.getString( "appPayRequest" ) );
        }
    }


    /**
     * 微信
     *
     * @param appPayRequest
     */
    private void payWX(String appPayRequest) {
        if (checkWeixinInstalled( mActivity )) {
            UnifyPayRequest msg = new UnifyPayRequest();
            msg.payChannel = UnifyPayRequest.CHANNEL_WEIXIN;
            msg.payData = appPayRequest;
            UnifyPayPlugin.getInstance( mActivity ).sendPayRequest( msg );
        } else {
            ToastHelper.showLong( R.string.wx_is_not_installed );
        }
    }

    /**
     * 支付宝
     *
     * @param appPayRequest
     */
    private void payAli(String appPayRequest) {
        if (checkAlipaysInstalled( mActivity )) {
            UnifyPayRequest msg = new UnifyPayRequest();
            msg.payChannel = UnifyPayRequest.CHANNEL_ALIPAY;
            msg.payData = appPayRequest;
            UnifyPayPlugin.getInstance( mActivity ).sendPayRequest( msg );
        } else {
            ToastHelper.showLong( R.string.alipay_is_not_installed );
        }
    }

    public static boolean checkWeixinInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages( 0 );// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get( i ).packageName;
                if (pn.equals( "com.tencent.mm" )) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkAlipaysInstalled(Context context) {
        Uri uri = Uri.parse( "alipays://platformapi/startApp" );
        Intent intent = new Intent( Intent.ACTION_VIEW, uri );
        ComponentName componentName = intent.resolveActivity( context.getPackageManager() );
        return componentName != null;
    }

    /**
     * 云闪付
     *
     * @param appPayRequest
     */
    private void payUnion(String appPayRequest) {
        String tn = "空";
        try {
            JSONObject e = new JSONObject( appPayRequest );
            tn = e.getString( "tn" );
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        if (UPPayAssistEx.checkInstalled( mActivity )) {
            UPPayAssistEx.startPay( mActivity, null, null, tn, "00" );
            Log.e( "MYLOG", "云闪付支付 tn = " + tn );
        } else {
            File nativeFile = new File( Environment.getExternalStorageDirectory(), "Android/data/" + mActivity.getPackageName() + "/files/Download/UPPayPluginEx.apk" );
            new AlertDialog.Builder( mActivity )
                    .setMessage( nativeFile.exists() ? "未安装支持插件，是否安装？" : "未安装支持插件，是否下载？" )
                    .setNeutralButton( R.string.cancel, null )
                    .setNegativeButton( R.string.ok, (dialog, which) -> installForNetwork( mActivity, false ) ).show();
        }
    }

    public static void installForNetwork(Context context, boolean onlyInstall) {//Android/data/com.expo/files/Download/
        File nativeFile = new File( Environment.getExternalStorageDirectory(), "Android/data/" + context.getPackageName() + "/files/Download/UPPayPluginEx.apk" );
        if (nativeFile.exists()) {
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile( context, context.getPackageName() + ".fileprovider", nativeFile );
            } else {
                uri = Uri.fromFile( nativeFile );
            }
            Intent in = new Intent( "android.intent.action.VIEW" );
            in.setDataAndType( uri, "application/vnd.android.package-archive" );
            in.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            in.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );
            context.startActivity( in );
        } else if (!onlyInstall) {
            DownloadManager manager = (DownloadManager) context.getSystemService( Context.DOWNLOAD_SERVICE );
            DownloadManager.Request request = new DownloadManager.Request( Uri.parse( Constants.URL.UPPAY_APP_DOWNLOAD_URL ) );
            request.setAllowedNetworkTypes( DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE );
            request.setAllowedOverRoaming( false );
            request.setNotificationVisibility( DownloadManager.Request.VISIBILITY_VISIBLE );
            request.setDestinationInExternalFilesDir( context, Environment.DIRECTORY_DOWNLOADS, "UPPayPluginEx.apk" );
            request.setMimeType( "application/vnd.android.package-archive" );
            request.allowScanningByMediaScanner();
            request.setVisibleInDownloadsUi( true );
            manager.enqueue( request );
            new Handler( Looper.getMainLooper() ).post( () -> ToastHelper.showShort( R.string.start_download ) );
        }
    }


    @Override
    public void onResult(String resultCode, final String resultInfo) {
        Log.e( "MYLOG", "onResult resultCode=" + resultCode + ", resultInfo=" + resultInfo );
        mWebView.post( new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl( "javascript:setPayResult('" + resultInfo + "')" );
            }
        } );
        Toast.makeText( mActivity, resultInfo, Toast.LENGTH_LONG ).show();
    }
}