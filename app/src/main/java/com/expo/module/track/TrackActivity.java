package com.expo.module.track;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.PrefsHelper;
import com.expo.contract.TrackContract;
import com.expo.entity.Track;
import com.expo.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class TrackActivity extends BaseActivity<TrackContract.Presenter> implements TrackContract.View {

    @BindView(R.id.map_view)
    TextureMapView mMapView;
    @BindView(R.id.update_time)
    TextView tvUpdateTime;

    private AMap mAMap;

    @Override
    protected int getContentView() {
        return R.layout.activity_track;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, "世园足迹");
        String updateTime = PrefsHelper.getString(Constants.Prefs.KEY_TRACK_UPDATE_TIME, "");
        if (updateTime.isEmpty()) {
            tvUpdateTime.setText(getString(R.string.open_the_footprint_record));
        }else{
            initTitleRightTextView(R.string.clear_track, R.color.green_02cd9b, v -> mPresenter.clearTrack());
        }
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mPresenter.queryTrack();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context) {
        Intent in = new Intent(context, TrackActivity.class);
        context.startActivity(in);
    }

    @Override
    public void loadTrackRsp(Map<Long, List<Track>> map) {
        List<LatLng> latLngs = new ArrayList<>();
        for (Long key : map.keySet()) {
            // 画线
            for (Track track : map.get(key)) {
                latLngs.add(new LatLng(track.getLat(), track.getLng()));
            }
            drawLine(latLngs);
            latLngs.clear();
        }
    }

    @Override
    public void clearTrackRes() {
        mAMap.clear();
        PrefsHelper.setString(Constants.Prefs.KEY_TRACK_UPDATE_TIME, null);
        tvUpdateTime.setText(PrefsHelper.getString(Constants.Prefs.KEY_TRACK_UPDATE_TIME, getString(R.string.open_the_footprint_record)));
    }

    private void drawLine(List<LatLng> latLngs) {
        PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1));
        mAMap.addPolyline(polylineOptions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
