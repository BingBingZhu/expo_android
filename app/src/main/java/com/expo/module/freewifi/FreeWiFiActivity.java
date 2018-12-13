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
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.FreeWiFiContract;
import com.expo.utils.Constants;
import com.expo.utils.wifi.WifiReceiver;
import com.expo.utils.wifi.WifiReceiverActionListener;
import com.expo.utils.wifi.WifiUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class FreeWiFiActivity extends BaseActivity<FreeWiFiContract.Presenter> implements FreeWiFiContract.View, WifiUtil.OnConnectListener {

    private final static int STATE_UNUNITED = 0;            //未连接
    private final static int STATE_CONNECTING = 1;          //连接中
    private final static int STATE_SUCCESSFU_LCONNECTION = 2;//连接成功
    private final static int STATE_CONNECTION_FAIL = 3;    //连接失败

    @BindView(R.id.wifi_btn)
    ImageView imgView;
    @BindView(R.id.wifi_state)
    TextView tvState;

    private String[] wifi;
    private boolean executeConnect;
    private boolean isConnect;

    @Override
    protected int getContentView() {
        return R.layout.activity_free_wifi;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, R.string.free_wifi_at_beijing_world_expo);
        wifi = mPresenter.queryWifi();
        if (null == wifi || wifi[0].isEmpty()) {
            setWifiView(STATE_UNUNITED);
            tvState.setText(R.string.not_free_wifi);
        } else {
            registerBroadcastReceiver();
            if (WifiUtil.isAtWifi(getContext(), wifi[0])) {
                setWifiView(STATE_SUCCESSFU_LCONNECTION);
                isConnect = true;
            } else {
                setWifiView(STATE_UNUNITED);
            }
        }
        WifiUtil.setOnConnectListener(this);
    }

    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        wifiReceiver = new WifiReceiver(listener);
        registerReceiver(wifiReceiver, filter);
    }

    private WifiReceiver wifiReceiver;

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent(context, FreeWiFiActivity.class);
        context.startActivity(in);
    }

    @OnClick(R.id.wifi_btn)
    public void onClick(View v) {
        if (isConnect){
            WifiUtil.disconnectWifi(getContext());
            setWifiView(STATE_UNUNITED);
            isConnect = false;
            return;
        }
        if (WifiUtil.isWiFiActive(getContext())){
            if (WifiUtil.isAtWifi(getContext(), wifi[0])){
                // 连接了世园wifi
//                WifiUtil.connectWifi(getContext(), wifi[0], wifi[1]);
            }else{
                // 执行连接世园wifi
                WifiUtil.connectWifi(getContext(), wifi[0], wifi[1]);
            }
        }else{
            // 打开wifi开关
            executeConnect = true;
            WifiUtil.changeWifiState(getContext(), true);
        }
    }

    public void setWifiView(int state) {
        int imgId = R.mipmap.ico_connect_not;
        boolean isEnable = true;
        String stateStr = getString(R.string.please_click_the_button_to_open_wlan);
        switch (state) {
            case STATE_UNUNITED:
                imgId = R.mipmap.ico_connect_not;
                stateStr = getString(R.string.please_click_the_button_to_open_wlan);
                isEnable = true;
                break;
            case STATE_CONNECTING:
                imgId = R.mipmap.ico_connecting;
                stateStr = getString(R.string.connecting);
                isEnable = false;
                break;
            case STATE_SUCCESSFU_LCONNECTION:
                imgId = R.mipmap.ico_connect_un_success;
                stateStr = getString(R.string.successfu_lconnection);
                isEnable = true;
                break;
            case STATE_CONNECTION_FAIL:
                isConnect = false;
                imgId = R.mipmap.ico_connect_off;
                stateStr = getString(R.string.disconnect);
                isEnable = true;
                break;
        }
        imgView.setImageResource(imgId);
        imgView.setEnabled(isEnable);
        tvState.setText(stateStr);
    }

    private WifiReceiverActionListener listener = new WifiReceiverActionListener() {
        @Override
        public void onWifiConnected(WifiInfo wifiInfo) {
            if (WifiUtil.isAtWifi(getContext(), wifi[0])){
                setWifiView(STATE_SUCCESSFU_LCONNECTION);
                isConnect = true;
            }
        }

        @Override
        public void onWifiOpened() {
            if (executeConnect)
                WifiUtil.connectWifi(getContext(), wifi[0], wifi[1]);
        }

        @Override
        public void onWifiOpening() {
            // ToastHelper.showShort("wifi打开中..");
        }

        @Override
        public void onWifiClosed() {
            setWifiView(STATE_CONNECTION_FAIL);
            // ToastHelper.showShort("wifi关闭了..");
        }

        @Override
        public void onWifiClosing() {
            // ToastHelper.showShort("wifi关闭中..");
        }
    };

    @Override
    public void onError() {
        setWifiView(STATE_CONNECTION_FAIL);
    }
}
