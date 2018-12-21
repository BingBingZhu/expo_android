package com.expo.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceStatusListener;
import com.expo.base.ExpoApp;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.PrefsHelper;
import com.expo.db.dao.BaseDao;
import com.expo.db.dao.BaseDaoImpl;
import com.expo.entity.Track;
import com.expo.module.heart.HeartBeatService;
import com.expo.utils.Constants;
import com.expo.utils.LocalBroadcastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrackRecordService extends Service {

    private LBSTraceClient mLbsTraceClient;
    private BaseDao mDao;
    private List<Track> tracks;
    private LatLng latLng;
    private LatLng oldLatLng;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastUtil.registerReceiver(getApplicationContext(), receiver, Constants.Action.ACTION_TRACK_CHANAGE);
        if (PrefsHelper.getBoolean(Constants.Prefs.KEY_TRACK_ON_OFF, false))
            startTrace();
    }

    /**
     * 记录开关
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isStart = intent.getBooleanExtra(Constants.EXTRAS.EXTRA_TRACK_CHANAGE, false);
            if (isStart)
                startTrace();
            else
                stopTrace();
        }
    };

    /**
     * 开始记录
     */
    private void startTrace() {
        tracks = new ArrayList<>();
        mDao = new BaseDaoImpl();
        mLbsTraceClient = LBSTraceClient.getInstance(getApplicationContext());
        mLbsTraceClient.startTrace(traceStatusListener);
    }

    /**
     * 停止记录
     */
    private void stopTrace() {
        if (null != mLbsTraceClient)
            mLbsTraceClient.stopTrace();
    }

    private TraceStatusListener traceStatusListener = new TraceStatusListener() {
        @Override
        public void onTraceStatus(List<TraceLocation> list, List<LatLng> list1, String s) {
            tracks.clear();
            if (s.equals(LBSTraceClient.TRACE_SUCCESS)) {
                oldLatLng = null;
                for (LatLng latLng : list1) {
                    tracks.add(new Track(latLng.latitude, latLng.longitude,
                            PrefsHelper.getLong(Constants.Prefs.KEY_RUN_UP_COUNT, 0),
                            ExpoApp.getApplication().getUser().getUid()));
                }
            } else if (s.equals(LBSTraceClient.MIN_GRASP_POINT_ERROR) && !list.isEmpty()) {
                latLng = new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude());
                if (isLocationChanage()) {
                    tracks.add(new Track(list.get(0).getLatitude(), list.get(0).getLongitude(),
                            PrefsHelper.getLong(Constants.Prefs.KEY_RUN_UP_COUNT, 0),
                            ExpoApp.getApplication().getUser().getUid()));
                    oldLatLng = latLng;
                }
            }
            if (!tracks.isEmpty()) {
                PrefsHelper.setString(Constants.Prefs.KEY_TRACK_UPDATE_TIME, "最后更新足迹 " + sdf.format(new Date()));
                mDao.saveOrUpdateAll(tracks);
            }
        }
    };

    public static SimpleDateFormat sdf = new SimpleDateFormat( Constants.TimeFormat.TYPE_ALL, Locale.getDefault() );

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
        stopTrace();
        LocalBroadcastUtil.unregisterReceiver(getApplicationContext(), receiver);
        super.onDestroy();
    }
}
