package com.expo.utils.wifi;

import android.net.wifi.WifiInfo;

public interface WifiReceiverActionListener {

    void onWifiConnected(WifiInfo wifiInfo);
    void onWifiOpened();
    void onWifiOpening();
    void onWifiClosed();
    void onWifiClosing();
}
