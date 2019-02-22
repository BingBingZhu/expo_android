package com.expo.module.circum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.blankj.utilcode.util.SizeUtils;
import com.expo.R;
import com.expo.adapters.StationAdapter;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.CircumTrafficContract;
import com.expo.entity.Park;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;
import com.expo.map.ClusterClickListener;
import com.expo.map.ClusterItem;
import com.expo.map.ClusterOverlay;
import com.expo.map.MapUtils;
import com.expo.map.NaviManager;
import com.expo.module.map.MarkerInfoInterface;
import com.expo.utils.LanguageUtil;
import com.expo.widget.SimpleHolder;
import com.expo.widget.decorations.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amap.api.fence.GeoFenceClient.GEOFENCE_IN;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_OUT;

/**
 * 周边交通
 */
public class CircumTrafficActivity extends BaseActivity<CircumTrafficContract.Presenter> implements CircumTrafficContract.View {

    @BindView(R.id.circum_traffic_parking_lot)
    RecyclerView mRecyclerViewPark;
    @BindView(R.id.circum_traffic_bus_station)
    RecyclerView mRecyclerViewBus;
    @BindView(R.id.map_view)
    TextureMapView mMapView;

    private StationAdapter parkAdapter;
    private StationAdapter busAdapter;
    private Venue parkVenue;
    private Venue busVenue;

    private AMap mAMap;
    private List<Venue> mFacilities;
    private MapUtils mMapUtils;


    @Override
    protected int getContentView() {
        return R.layout.activity_circum_list_traffic;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, "周边交通");
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mMapUtils = new MapUtils(mAMap);
        mMapUtils.settingMap(null, null);
        mAMap.addTileOverlay(mMapUtils.getTileOverlayOptions(getContext()));
        mAMap.setMaxZoomLevel(19);
        mAMap.getUiSettings().setCompassEnabled(true);//设置指南针是否可见
        mAMap.getUiSettings().setAllGesturesEnabled(false);//设置所有手势是否可用
        mPresenter.loadTrafficData();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CircumTrafficActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.circum_traffic_navi_parking_lot, R.id.circum_traffic_navi_bus_station})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circum_traffic_navi_parking_lot:
                if (null != parkVenue)
                    NaviManager.getInstance(getContext()).showSelectorNavi(parkVenue);
                else
                    ToastHelper.showShort("抱歉，暂无停车场数据");
                break;
            case R.id.circum_traffic_navi_bus_station:
                if (null != busVenue)
                    NaviManager.getInstance(getContext()).showSelectorNavi(busVenue);
                else
                    ToastHelper.showShort("抱歉，暂无公交车站数据");
                break;
        }
    }

    @Override
    public void loadTrafficDataRes(List<Venue> venuePark, List<Venue> venueBus) {
        int hSpace = getResources().getDimensionPixelSize(R.dimen.dms_50);
        int vSpace = getResources().getDimensionPixelSize(R.dimen.dms_24);
        if (null != venuePark && venuePark.size() > 0) {
            parkVenue = venuePark.get(0);
            parkAdapter = new StationAdapter(venuePark);
            parkAdapter.addOnClickListener(v -> {
                int position = ((SimpleHolder) v.getTag()).getAdapterPosition();
                parkVenue = venuePark.get(position);
            });
            mRecyclerViewPark.addItemDecoration(new SpaceDecoration(hSpace, vSpace));
            mRecyclerViewPark.setAdapter(parkAdapter);

            this.mFacilities = venuePark;
            addActualSceneMarker(mFacilities);
        }
        if (null != venueBus && venueBus.size() > 0) {
            busVenue = venueBus.get(0);
            busAdapter = new StationAdapter(venueBus);
            busAdapter.addOnClickListener(v -> {
                int position = ((SimpleHolder) v.getTag()).getAdapterPosition();
                busVenue = venueBus.get(position);
            });
            mRecyclerViewBus.addItemDecoration(new SpaceDecoration(hSpace, vSpace));
            mRecyclerViewBus.setAdapter(busAdapter);
        }
    }

    /**
     * 显示园区范围
     *
     * @param park
     */
    @Override
    public void showParkScope(Park park) {
        mMapUtils.setLimits(park);
    }

    private void addActualSceneMarker(List<Venue> facilities) {
        for (Venue as : facilities) {
            if (as.getLat() == 0)
                continue;
            LatLng latLng = new LatLng(as.getLat(), as.getLng());
            mAMap.addMarker(new MarkerOptions()
                    .icon(mMapUtils.setMarkerIconDrawable(getContext(), null,
                            LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption())))
                    .anchor(0.5F, 0.90F).position(latLng));
        }
//        mMapUtils.setCameraZoom(markers);
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mMapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        if (mMapView != null)
            mMapView.onDestroy();
        super.onDestroy();
    }
}
