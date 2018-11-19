package com.expo.module.freewifi;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.FreeWiFiContract;
import com.expo.utils.Constants;
import com.expo.utils.wifi.WifiReceiver;
import com.expo.utils.wifi.WifiReceiverActionListener;
import com.expo.utils.wifi.WifiUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class FreeWiFiActivity extends BaseActivity<FreeWiFiContract.Presenter> implements FreeWiFiContract.View {

    @BindView(R.id.wifi_btn)
    ImageView view;

    private String[] wifi;

    @Override
    protected int getContentView() {
        return R.layout.activity_free_wifi;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        wifi = mPresenter.queryWifi();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (WifiUtil.isWiFiActive(getContext())){
            WifiUtil.connectWifi( getContext(), wifi[0], wifi[1] );
        }else{
            WifiUtil.changeWifiState(getContext(), true);
        }
    }

    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction( WifiManager.WIFI_STATE_CHANGED_ACTION );
        filter.addAction( WifiManager.SCAN_RESULTS_AVAILABLE_ACTION );
        filter.addAction( WifiManager.NETWORK_STATE_CHANGED_ACTION );
        wifiReceiver = new WifiReceiver( listener );
        registerReceiver( wifiReceiver, filter );
    }

    private WifiReceiver wifiReceiver;

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, FreeWiFiActivity.class );
        context.startActivity( in );
    }

    @OnClick(R.id.wifi_btn)
    public void onClick(View v) {
        WifiUtil.changeWifiState(this, true);
    }

    private WifiReceiverActionListener listener = new WifiReceiverActionListener() {
        @Override
        public void onWifiConnected(WifiInfo wifiInfo) {
            ToastHelper.showShort( "已连接至 " + wifiInfo.getSSID() );
        }

        @Override
        public void onWifiOpened() {
            // ToastHelper.showShort("wifi已经打开..");
            WifiUtil.connectWifi( getContext(), wifi[0], wifi[1] );
//            if (PrefsHelper.getBoolean( Constants.Prefs.KEY_IS_WIFI_OPEN, false )
//                    && !LucasApp.getApplication().wifi.isEmpty())
//                WifiUtil.connectWifi( getContext(), LucasApp.getApplication().wifi.get( 0 ), LucasApp.getApplication().wifi.get( 1 ) );
        }

        @Override
        public void onWifiOpening() {
            // ToastHelper.showShort("wifi打开中..");
        }

        @Override
        public void onWifiClosed() {
            // ToastHelper.showShort("wifi关闭了..");
        }

        @Override
        public void onWifiClosing() {
            // ToastHelper.showShort("wifi关闭中..");
        }
    };
}
