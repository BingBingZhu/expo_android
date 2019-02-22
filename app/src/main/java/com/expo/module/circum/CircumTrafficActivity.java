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
public class CircumTrafficActivity extends BaseActivity<CircumTrafficContract.Presenter> implements CircumTrafficContract.View
        , AMap.OnMapTouchListener, AMap.OnMarkerClickListener{

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

    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";
    public static final String EXPO_PARK = "expo_park";

    private AMap mAMap;
    private GeoFenceClient mGeoFenceClient;
    private List<Venue> mFacilities;
    private Long mTabId;
    private List<Marker> markers;
    private List<Polyline> polylines;
    private MapUtils mMapUtils;
    private LatLng mLatLng;
    private List<VenuesType> mVenuesTypes;
    private int mTabPosition = 0;
    private List<Venue> mAtVenue;   // 当前tab下的场馆
    private boolean mIsInPark;

    private ClusterOverlay mClusterOverlay;

    ClusterClickListener mClusterClickListener = new ClusterClickListener() {
        @Override
        public void onClick(Marker marker, List<ClusterItem> clusterItems) {
            if (clusterItems == null) {
                //if (marker.getObject() instanceof Venue)
                    //showVenueDialog((Venue) marker.getObject());
            } else {
                if (clusterItems.size() == 1) {
                    //showVenueDialog(((RegionItem) clusterItems.get(0)).actualScene);
                } else if (clusterItems.size() > 1) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (ClusterItem clusterItem : clusterItems) {
                        builder.include(clusterItem.getPosition());
                    }
                    LatLngBounds latLngBounds = builder.build();
                    mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0)
                    );
                }
            }
        }
    };

    MarkerInfoInterface mMarkerInfoInterface = new MarkerInfoInterface() {
        @Override
        public Bitmap getMarkerBitmap(Venue v) {
            return getMarkerBitmap(v);
        }
    };


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
        mMapUtils.settingMap(this, this);
        mAMap.setOnMyLocationChangeListener(mLocationChangeListener);
        mAMap.addTileOverlay(mMapUtils.getTileOverlayOptions(getContext()));
        mAMap.setMaxZoomLevel(19);
        mAMap.getUiSettings().setCompassEnabled(true);//设置指南针是否可见
        mAMap.getUiSettings().setAllGesturesEnabled(false);//设置所有手势是否可用
        // 地理围栏
        mGeoFenceClient = new GeoFenceClient(getContext());
        mGeoFenceClient.setActivateAction(GEOFENCE_IN | GEOFENCE_OUT);
        mGeoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
        registerReceiver(mGeoFenceReceiver, filter);
        mClusterOverlay = new ClusterOverlay(mAMap, null,
                SizeUtils.dp2px(50),
                this);
        mClusterOverlay.setMarkerInfoInterface(mMarkerInfoInterface);
        mClusterOverlay.setOnClusterClickListener(mClusterClickListener);

        mPresenter.loadTrafficData();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context) {
        Intent intent =  new Intent( context, CircumTrafficActivity.class );
        context.startActivity(intent);
    }

    @OnClick({R.id.circum_traffic_navi_parking_lot, R.id.circum_traffic_navi_bus_station})
    public void onClick(View v){
        switch (v.getId()){
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
        int hSpace = getResources().getDimensionPixelSize( R.dimen.dms_50 );
        int vSpace = getResources().getDimensionPixelSize( R.dimen.dms_24 );
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
            mAtVenue = new ArrayList<>();
            markers = new ArrayList<>();
            polylines = new ArrayList<>();
            addActualSceneMarker(mTabId, mFacilities, false);
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

    @Override
    public void loadTabRes(List<VenuesType> venuesTypes, int tabPosition) {
        this.mVenuesTypes = venuesTypes;
    }

    @Override
    public void updatePic(VenuesType vt) {
        for (Marker marker : markers) {
            Venue as = (Venue) marker.getObject();
            if (null != as && as.getType() == vt.getId()) {
                marker.setIcon(mMapUtils.setMarkerIconDrawable(getContext(), vt.getMarkBitmap(),
                        LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption())));
            }
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
        mGeoFenceClient.addGeoFence(mMapUtils.getGeoFencePoints(park), EXPO_PARK);
    }

    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                Bundle bundle = intent.getExtras();
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                String customId = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                if (status == GEOFENCE_IN && customId.equals(EXPO_PARK)) {
                    mIsInPark = true;
                } else if (status == GEOFENCE_OUT && customId.equals(EXPO_PARK)) {
                    mIsInPark = false;
                }
            }
        }
    };

    /**
     * 清除地图覆盖物
     */
    private void clearMap() {
        if (!markers.isEmpty()) {
            for (Marker marker : markers)
                marker.remove();
            markers.clear();
        }
        if (!polylines.isEmpty()) {
            for (Polyline polyline : polylines)
                polyline.remove();
            polylines.clear();
        }
    }

    private void addActualSceneMarker(Long tabId, List<Venue> facilities, boolean flag) {
        mAtVenue.clear();
        if (flag) {
            for (Venue as : facilities) {
                if (as.getType() == tabId) {
                    mAtVenue.add(as);
                }
            }
        } else {
            mAtVenue.addAll(facilities);
        }
        mClusterOverlay.clearClusterItem();
        clearMap();
        for (Venue as : mAtVenue) {
            if (as.getLat() == 0)
                continue;
            LatLng latLng = new LatLng(as.getLat(), as.getLng());
            VenuesType vt = mVenuesTypes.get(mTabPosition);

            //if (mTabPosition == 0) {
                /*RegionItem regionItem = new RegionItem(latLng,
                        LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption()));
                regionItem.venuesType = vt;
                regionItem.actualScene = as;
                mClusterOverlay.addClusterItem(regionItem);*/
            //} else {
                Marker marker = mAMap.addMarker(new MarkerOptions()
                        .icon(mMapUtils.setMarkerIconDrawable(getContext(), vt.getMarkBitmap(),
                                LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption())))
                        .anchor(0.5F, 0.90F).position(latLng));
                marker.setObject(as);
                markers.add(marker);
            //}
        }
        mMapUtils.setCameraZoom(markers);
        mMapUtils.setFollow(mLatLng);
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
    public void onTouch(MotionEvent motionEvent) {
        mMapUtils.setNotFollow();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Venue venue = (Venue) marker.getObject();
        // 显示marker弹窗
        //showVenueDialog(venue);
        return false;
    }

    private AMap.OnMyLocationChangeListener mLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mGeoFenceReceiver);
        if (mMapView != null)
            mMapView.onDestroy();
        super.onDestroy();
    }
}
