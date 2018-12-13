package com.expo.utils.wifi;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.expo.base.utils.LogUtils;
import com.expo.base.utils.ToastHelper;

public class WifiUtil {

    private static WifiManager wifiManager;

    /**
     * 检查wifi状态
     * @param context
     * @return
     */
    public static boolean isWiFiActive(Context context) {
        initWifiManager(context);
        return wifiManager.isWifiEnabled(); // 返回wifi状态
    }

    public static boolean isAtWifi(Context context, String ssid){
        initWifiManager(context);
//        int wifiState = wifiManager.getWifiState();
        WifiInfo info = wifiManager.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : "";
        LogUtils.e("aaaaaaaaaaa", "当前连接的wifi SSID："+wifiId);
        ssid = "\""+ssid+"\"";
        return wifiId.equals(ssid);
    }

    /**
     * 开启或关闭wifi
     * @param context
     * @param isOpen
     */
    public static void changeWifiState(Context context,boolean isOpen) {
        initWifiManager(context);
        wifiManager.setWifiEnabled(isOpen); //打开或关闭
    }

    public static void disconnectWifi(Context context){
        initWifiManager(context);
        wifiManager.disconnect();
    }

    public static void connectWifi(Context context, String wifiname, String pwd) {//第二个参数是账号名称，也就是我们WiFi列表里所看到的名字
        LogUtils.e("aaaaaaaaaaa", "执行连接wifi "+ wifiname+"  "+pwd);
//        WifiConfiguration config = createConfiguration(wifiname, pwd);
//        int networkId = wifiManager.addNetwork(config);
//        wifiManager.enableNetwork(networkId, true);
        initWifiManager(context);
        try {
            wifiManager.disconnect();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int wifiId = -1;//自己定义的数值，判断用
        WifiConfiguration wifiCong = new WifiConfiguration();//这个类是我们构造wifi对象使用的，具体可以百度
        wifiCong.SSID = "\"" + wifiname + "\"";// \"转义字符，代表"//为成员变量赋值
        wifiCong.preSharedKey = "\"" + pwd + "\"";// WPA-PSK密码
        wifiCong.hiddenSSID = false;
        wifiCong.status = WifiConfiguration.Status.ENABLED;
        wifiId = wifiManager.addNetwork(wifiCong);// 将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
        if ( wifiId!=-1 ) {
            // 添加成功
        }else {
            // 添加失败
        }
        boolean isConected =  wifiManager.enableNetwork(wifiId, true);  // 连接配置好的指定ID的网络 true连接成功
        if ( isConected ) {
            // 连接成功
//            WifiInfo info = wifiManager.getConnectionInfo();
        }else {
            // 连接失败
            if (null != connectListener){
                connectListener.onError();
            }
        }
    }

    private static void initWifiManager(Context context){
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);//得到wifi管理器对象
    }

    public static void setOnConnectListener(OnConnectListener onConnectListener){
        if (onConnectListener != null){
            connectListener = onConnectListener;
        }
    }

    private static OnConnectListener connectListener;

    public interface OnConnectListener {
        void onError();
    }

}
