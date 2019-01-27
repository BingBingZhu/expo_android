package com.expo.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.expo.base.ExpoApp;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.PrefsHelper;
import com.expo.db.dao.BaseDao;
import com.expo.db.dao.BaseDaoImpl;
import com.expo.entity.Track;
import com.expo.map.LocationManager;
import com.expo.utils.Constants;
import com.expo.utils.LocalBroadcastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrackRecordService extends Service {

    public static Location LOCATION;        // 供app内部公共调用

    public static Location getLocation(){
        return LOCATION;
    }

    private LBSTraceClient mLbsTraceClient;
    private BaseDao mDao;
    private List<Track> tracks;
    private LatLng latLng;
    private LatLng oldLatLng;
    private List<TraceLocation> mLocationList;
    private boolean isStartTrack;
    public static SimpleDateFormat sdf = new SimpleDateFormat( Constants.TimeFormat.TYPE_ALL, Locale.getDefault() );

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initTrace();
        LocationManager.getInstance().registerLocationListener(locationChangeListener);
        LocalBroadcastUtil.registerReceiver(getApplicationContext(), receiver, Constants.Action.ACTION_TRACK_CHANAGE);
        isStartTrack = PrefsHelper.getBoolean(Constants.Prefs.KEY_TRACK_ON_OFF, false);
    }

    private AMap.OnMyLocationChangeListener locationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LOCATION = location;
            if ( isStartTrack && null != location && location.getLatitude() != 0 ){
                TraceLocation traceLocation = new TraceLocation(location.getLatitude(), location.getLongitude(), location.getSpeed(), location.getBearing(), location.getTime());
                mLocationList.add(traceLocation);
                if ( mLocationList.size() >= 5 ){
                    // 纠偏
                    tracks.clear();
                    mLbsTraceClient.queryProcessedTrace(0, mLocationList, LBSTraceClient.TYPE_AMAP, traceListener);
                }
            }
        }
    };

    // 自有轨迹纠偏回调
    private TraceListener traceListener = new TraceListener() {
        @Override
        public void onRequestFailed(int i, String s) {
            if (/*s.equals(LBSTraceClient.MIN_GRASP_POINT_ERROR) &&*/ !mLocationList.isEmpty()) {
                latLng = new LatLng(mLocationList.get(0).getLatitude(), mLocationList.get(0).getLongitude());
                if (isLocationChanage()) {
                    tracks.add(new Track(mLocationList.get(0).getLatitude(), mLocationList.get(0).getLongitude(),
                            PrefsHelper.getLong(Constants.Prefs.KEY_RUN_UP_COUNT, 0),
                            ExpoApp.getApplication().getUser().getUid()));
                    oldLatLng = latLng;
                }
                if (!tracks.isEmpty()) {
                    PrefsHelper.setString(Constants.Prefs.KEY_TRACK_UPDATE_TIME, "最后更新足迹 " + sdf.format(new Date()));
                    mDao.saveOrUpdateAll(tracks);
                }
            }
            mLocationList.clear();
        }

        @Override
        public void onTraceProcessing(int i, int i1, List<LatLng> list) {
            LogUtils.e("", "");
        }

        @Override
        public void onFinished(int i, List<LatLng> list, int i1, int i2) {
            for (LatLng latLng : list) {
                tracks.add(new Track(latLng.latitude, latLng.longitude,
                        PrefsHelper.getLong(Constants.Prefs.KEY_RUN_UP_COUNT, 0),
                        ExpoApp.getApplication().getUser().getUid()));
            }
            if (!tracks.isEmpty()) {
                PrefsHelper.setString(Constants.Prefs.KEY_TRACK_UPDATE_TIME, "最后更新足迹 " + sdf.format(new Date()));
                mDao.saveOrUpdateAll(tracks);
            }
            mLocationList.clear();
        }
    };

    /**
     * 记录开关
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isStartTrack = intent.getBooleanExtra(Constants.EXTRAS.EXTRA_TRACK_CHANAGE, false);
        }
    };

    /**
     * 初始化轨迹记录
     */
    private void initTrace() {
        tracks = new ArrayList<>();
        mLocationList = new ArrayList<>();
        mDao = new BaseDaoImpl();
        mLbsTraceClient = LBSTraceClient.getInstance(getApplicationContext());
    }

    private boolean isLocationChanage() {
        if (null == oldLatLng) {
            return true;
        } else {
            float distance = AMapUtils.calculateLineDistance(latLng, oldLatLng);
            if (distance >= 3) {
                return true;
            }else{
                return false;
            }
        }
    }


    /**
     * 启动服务
     *
     * @param context
     */
    public static void startService(Context context) {
        Intent intent = new Intent(context, TrackRecordService.class);
        context.startService(intent);
    }

    /**
     * 停止服务
     *
     * @param context
     */
    public static void stopService(Context context) {
        Intent intent = new Intent(context, TrackRecordService.class);
        context.stopService(intent);
    }

    @Override
    public void onDestroy() {
        LocationManager.getInstance().unregisterLocationListener(locationChangeListener);
        LocalBroadcastUtil.unregisterReceiver(getApplicationContext(), receiver);
        super.onDestroy();
    }
}
