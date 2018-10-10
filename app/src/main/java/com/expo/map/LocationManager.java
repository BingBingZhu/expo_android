package com.expo.map;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.expo.base.BaseApplication;
import com.expo.base.utils.LogUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * LocationManager:高德地图定位控制
 */
public class LocationManager implements AMapLocationListener {
    private static final String TAG = LocationManager.class.getSimpleName();

    private static LocationManager locationManager;

    /* 定位客服端  */
    private AMapLocationClient mLocationClient;

    /* 声明定位配置参数 */
    private AMapLocationClientOption mLocationOption = null;

    private AtomicInteger listenerCount;

    /**
     * 定位后的数据
     */
    private AMapLocation currentLocation;


    /* 设置定位模式为高精度模式   使用网络与GPS进行定位*/
    private static final AMapLocationMode mLocationMode = AMapLocationMode.Hight_Accuracy;

    /**
     * 对外回调接口 .
     */
    private List<AMap.OnMyLocationChangeListener> listeners;

    private LocationManager() {
        onCreate();
    }

    public static boolean exists;

    /**
     * 获取定位控制实例
     */
    public synchronized static LocationManager getInstance() {
        if (locationManager == null) {
            locationManager = new LocationManager();
        }
        return locationManager;
    }

    /**
     * 初始化定位客服端
     */
    @SuppressWarnings("deprecation")
    private void onCreate() {
        exists = true;
        // 初始化定位
        mLocationClient = new AMapLocationClient( BaseApplication.getApplication() );
        // 设置定位回调监听
        mLocationClient.setLocationListener( this );
        // 配置定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        mLocationOption.setLocationMode( mLocationMode );
        //是否使用传感器
        mLocationOption.setSensorEnable( true );
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress( true );
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation( false );
        // 设置是否强制刷新WIFI，默认为强制刷新
//		mLocationOption.setWifiActiveScan(true);
        // 设置首选gps
        mLocationOption.setGpsFirst( true );
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable( true );
        // 设置定位间隔1秒钟定位一次
        mLocationOption.setInterval( 1000 );
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption( mLocationOption );
        listenerCount = new AtomicInteger();
        listeners = new LinkedList<>();
    }

    /**
     * 启动定位
     */
    private void start() {
        mLocationClient.startLocation();
        LogUtils.d( TAG, "--started LocationServer---" );
    }

    /**
     * 停止定位
     */
    private void stop() {
        mLocationClient.stopLocation();
        LogUtils.d( TAG, "--stoped LocationServer---" );
    }

    /**
     * 销毁定位客服端
     */
    public void destroy() {
        if (mLocationClient != null) {
            stop();
            exists = false;
            mLocationClient.onDestroy();
            mLocationClient = null;
            locationManager = null;
            LogUtils.d( TAG, "--LocationManager destroyed---" );
        }
    }

    /**
     * 定位信息回调接口
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (null != amapLocation) {
            if (0 == amapLocation.getErrorCode()) {
                // 定位成功回调信息，设置相关消息
                currentLocation = amapLocation;
                if (null == listeners || listeners.isEmpty())
                    return;
                for (AMap.OnMyLocationChangeListener listener : listeners) {
                    listener.onMyLocationChange( amapLocation );
                }
            } else {
                // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见高德错误码表
                LogUtils.d( TAG,
                        "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo() );
            }
        }
    }

    /**
     * 注册定位监听
     *
     * @param locationListener
     */
    public void registerLocationListener(AMap.OnMyLocationChangeListener locationListener) {
        if (!this.listeners.contains( locationListener )) {
            this.listeners.add( locationListener );
            if (listenerCount.incrementAndGet() == 1) {
                start();
            }
        }
    }

    /**
     * 取消定位监听注册
     *
     * @param locationListener
     */
    public void unregisterLocationListener(AMap.OnMyLocationChangeListener locationListener) {
        if (this.listeners.remove( locationListener )) {
            if (listenerCount.decrementAndGet() <= 0) {
                stop();
            }
        }
    }

    /**
     * 获取当前定位位置
     *
     * @return
     */
    public AMapLocation getCurrentLocation() {
        return currentLocation;
    }

    /**
     * 获取定位位置经纬度
     *
     * @return
     */
    public LatLng getCurrentLocationLatLng() {
        if (currentLocation != null)
            return new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude() );
        return null;
    }

    /**
     * 获取手机朝向
     *
     * @return
     */
    public String getDirection() {
        if (currentLocation != null) {
            float bearing = currentLocation.getBearing();
            if (bearing < 22.5 || bearing >= 337.5) {
                return "北";
            } else if (bearing >= 22.5 && bearing < 67.5) {
                return "东北";
            } else if (bearing >= 67.5 && bearing < 112.5) {
                return "东";
            } else if (bearing >= 112.5 && bearing < 157.5) {
                return "东南";
            } else if (bearing >= 157.5 && bearing < 202.5) {
                return "南";
            } else if (bearing >= 202.5 && bearing < 247.5) {
                return "西南";
            } else if (bearing >= 247.5 && bearing < 292.5) {
                return "西";
            } else if (bearing >= 292.5 && bearing < 337.5) {
                return "西北";
            }
        }
        return null;
    }
}
