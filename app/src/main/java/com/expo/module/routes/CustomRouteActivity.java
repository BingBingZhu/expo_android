package com.expo.module.routes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.CustomRouteContract;
import com.expo.entity.CustomRoute;
import com.expo.entity.Park;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;
import com.expo.map.MapUtils;
import com.expo.module.map.NaviListener;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class CustomRouteActivity extends BaseActivity<CustomRouteContract.Presenter> implements CustomRouteContract.View, AMap.OnMarkerClickListener {

    @BindView(R.id.map_view)
    TextureMapView mMapView;
    @BindView(R.id.custom_route_bottom_view)
    View mBottomView;
    @BindView(R.id.custom_route_distance)
    TextView mTvDistance;
    @BindView(R.id.custom_route_duration)
    TextView mTvDuration;
    @BindView(R.id.custom_route_save)
    TextView mTvSave;

    private AMap mAMap;
    private MapUtils mMapUtils;
    private List<Marker> markers;
    private List<Venue> mVenues;
    private List<CustomRoute> mCustomRoutes;
    public AMapNavi mAMapNavi;
    private Venue lastVenue, nextVenue, clickVenue;
    private Marker clickMarker;
    private List<VenuesType> venuesTypes = new ArrayList<>();
    private Map<CustomRoute, Polyline> lineMap;
    private Park mPark;
    private LatLng mLatLng;


    @Override
    protected int getContentView() {
        return R.layout.activity_custom_route;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, "自定义路线");
        initTitleRightTextView(R.string.reset_the_route, R.color.green_02cd9b, v -> clearRoute());
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mMapUtils = new MapUtils(mAMap);
        mMapUtils.getTileOverlayOptions(getContext());
        mPresenter.loadParkMapData();
        mAMap.getUiSettings().setRotateGesturesEnabled(false);
        mAMap.getUiSettings().setTiltGesturesEnabled(false);
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        mAMap.setOnMarkerClickListener(this);
        mMapUtils.setNotFollow();
        mAMap.setMyLocationEnabled( true );
        mAMap.setOnMyLocationChangeListener(mLocationChangeListener);
    }

    private AMap.OnMyLocationChangeListener mLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (null != location && location.getLatitude() != 0)
                mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
    };

    private Bitmap getMarkerBitmap(Venue v) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test_1);
        for (VenuesType vt : venuesTypes) {
            if (v.getType() == vt.getId()) {
                bitmap = vt.getMarkBitmap();
                break;
            }
        }
        return bitmap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        clickMarker = marker;
        clickVenue = (Venue) marker.getObject();
        // 算路   成功继续   失败return
        startRoute(!clickVenue.isSelected());
        return false;
    }

    private void updateData(Venue v, boolean isAdd, List<NaviLatLng> latLngs, int distance, int duration) {
        if (isAdd) {
            long routeIndex = PrefsHelper.getLong(Constants.Prefs.KEY_ROUTE_INDEX, 0);
            routeIndex++;
            PrefsHelper.setLong(Constants.Prefs.KEY_ROUTE_INDEX, routeIndex);
            v.setRouteIndex(routeIndex);
            v.setSelected(isAdd);
            selectedVenue.add(v);
        } else {
            v.setRouteIndex(0);
            v.setSelected(isAdd);
            CustomRoute lastCustomRoute = null;
            CustomRoute nextCustomRoute = null;
            for (CustomRoute customRoute : mCustomRoutes) {
                if (customRoute.getStartId() == v.getId()) {
                    nextCustomRoute = customRoute;
                }
                if (customRoute.getEndId() == v.getId()) {
                    lastCustomRoute = customRoute;
                }
            }
            if (lastCustomRoute != null) {
                mCustomRoutes.remove(lastCustomRoute);
                // 删除地图线
                updateMapAndLine(v, lastCustomRoute, false);
            }
            if (nextCustomRoute != null) {
                mCustomRoutes.remove(nextCustomRoute);
                // 删除地图线
                updateMapAndLine(v, nextCustomRoute, false);
            }
            selectedVenue.remove(v);
        }
        if (null != latLngs) {
            CustomRoute customRoute = new CustomRoute();
            customRoute.setStartId(lastVenue.getId());
            customRoute.setEndId(nextVenue.getId());
            customRoute.setDistance(distance);
            customRoute.setDuration(duration);
            customRoute.setPoints(latLngs);
            mCustomRoutes.add(customRoute);
            updateMapAndLine(v, customRoute, true);
        }else{
            updateMapAndLine(v, null, false);
        }
    }

    private void updateMapAndLine(Venue v, CustomRoute customRoute, boolean addLine) {
        if (null != v) {
            Bitmap bitmap = getMarkerBitmap(clickVenue);
            if (v.isSelected()) {      // 选择
                clickMarker.setIcon(mMapUtils.setSelectedMarkerIcon(getContext(), bitmap,
                        LanguageUtil.chooseTest(v.getCaption(), v.getEnCaption())));
            } else {      // 取消
                clickMarker.setIcon(mMapUtils.setMarkerIconDrawable(getContext(), bitmap,
                        LanguageUtil.chooseTest(v.getCaption(), v.getEnCaption())));
            }
        }
        if (addLine){
            addCustomLine(customRoute);
        }else{
            if (customRoute != null){
                CustomRoute customRoute1 = null;
                for (CustomRoute cr : lineMap.keySet()) {
                    if (cr.equals(customRoute)) {
                        customRoute1 = cr;
                        lineMap.get(cr).remove();
                        break;
                    }
                }
                if (null != customRoute1) {
                    lineMap.remove(customRoute1);
                }
            }
        }
        if (selectedVenue.size() > 1) {
            mBottomView.setVisibility(View.VISIBLE);
            setDistanceAndDuration();
        } else {
            mBottomView.setVisibility(View.GONE);
        }
    }

    private void setDistanceAndDuration() {
        int distance = 0, duration = 0;
        for (CustomRoute cr : mCustomRoutes){
            distance += cr.getDistance();
            duration += cr.getDuration();
        }
        mTvDistance.setText(getFormatDistance(distance));
        mTvDuration.setText(getFormatDuration(duration));
    }

    private List<Venue> selectedVenue;

    @OnClick({R.id.custom_route_save, R.id.latched_position})
    public void saveCustomRoute(View view) {
        switch (view.getId()){
            case R.id.custom_route_save:
                mPresenter.saveCustomRoute(mVenues, mCustomRoutes);
                break;
            case R.id.latched_position:
                if (mLatLng != null){
                    if (mMapUtils.ptInPolygon(mLatLng, mPark.getElectronicFenceList())){
                        mMapUtils.mapGoto(mLatLng);
                    }else{
                        ToastHelper.showShort("用户当前不在园区");
                    }
                }else{
                    ToastHelper.showShort("定位中，请稍后...");
                }
                break;
        }
    }

    // 清除路线
    private void clearRoute() {
        for (Marker marker : markers) {
            Venue venue = (Venue) marker.getObject();
            Bitmap bitmap = getMarkerBitmap(venue);
            if (venue.isSelected()) {
                marker.setIcon(mMapUtils.setMarkerIconDrawable(getContext(), bitmap,
                        LanguageUtil.chooseTest(venue.getCaption(), venue.getEnCaption())));
            }
        }
        for (CustomRoute cr : lineMap.keySet()) {
            lineMap.get(cr).remove();
        }
        lineMap.clear();
        selectedVenue.clear();
        mBottomView.setVisibility(View.GONE);
        mPresenter.clearCustomRoute();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent(context, CustomRouteActivity.class);
        context.startActivity(in);
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
        if (mAMapNavi != null) {
            mAMapNavi.stopNavi();
            mAMapNavi.destroy();
        }
    }

    @Override
    public void showParkScope(Park park) {
        mMapUtils.setLimits(park);
        this.mPark = park;
    }

    @Override
    public void loadFacilityRes(List<Venue> facilities) {
        mVenues = facilities;
        markers = new ArrayList<>();
        lineMap = new HashMap<>();
        selectedVenue = new ArrayList<>();
        for (Venue venue : mVenues) {
            if (venue.isSelected())
                selectedVenue.add(venue);
        }
        addActualSceneMarker(mVenues);
    }

    @Override
    public void updateMarkerPic(VenuesType vt) {
        venuesTypes.add(vt);
        for (Marker marker : markers) {
            Venue as = (Venue) marker.getObject();
            if (null != as && as.getType() == vt.getId()) {
                if (as.isSelected())
                    marker.setIcon(mMapUtils.setSelectedMarkerIcon(getContext(), vt.getMarkBitmap(),
                            LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption())));
                else
                    marker.setIcon(mMapUtils.setMarkerIconDrawable(getContext(), vt.getMarkBitmap(),
                            LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption())));
            }
        }
    }

    @Override
    public void loadCustomRoute(List<CustomRoute> customRoutes) {
        this.mCustomRoutes = customRoutes;
        addLines(mCustomRoutes);
    }

    @Override
    public void saveCustomRouteSuccess() {
        mBottomView.setVisibility(View.GONE);
        ToastHelper.showLong("路线保存成功");
    }

    public void addLines(List<CustomRoute> customRoutes) {
        for (CustomRoute cr : customRoutes) {
            addCustomLine(cr);
        }
    }

    public void addCustomLine(CustomRoute customRoute) {
        Polyline p = mAMap.addPolyline(new PolylineOptions().
                addAll(customRoute.getPoints()).setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.ico_route_item)));
        lineMap.put(customRoute, p);
    }

    private void addActualSceneMarker(List<Venue> venues) {
        if (!markers.isEmpty()) {
            for (Marker marker : markers)
                marker.remove();
            markers.clear();
        }
        for (Venue as : venues) {
            if (as.getLat() == 0)
                continue;
            LatLng latLng = new LatLng(as.getLat(), as.getLng());
            Marker marker;
            if (as.isSelected()) {
                marker = mAMap.addMarker(new MarkerOptions()
                        .icon(mMapUtils.setSelectedMarkerIcon(getContext(), null,
                                LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption())))
                        .anchor(0.5F, 0.90F).position(latLng));
            } else {
                marker = mAMap.addMarker(new MarkerOptions()
                        .icon(mMapUtils.setMarkerIconDrawable(getContext(), null,
                                LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption())))
                        .anchor(0.5F, 0.90F).position(latLng));
            }
            marker.setObject(as);
            markers.add(marker);
        }
        mMapUtils.setCameraZoom(markers);
    }

    private boolean isAdd;

    private void startRoute(boolean isAdd) {
        if (isAdd) {
            if (selectedVenue.size() <= 0) {
                updateData(clickVenue, isAdd, null, 0, 0);
                return;
            } else {
                lastVenue = selectedVenue.get(selectedVenue.size() - 1);
                nextVenue = clickVenue;
            }
        } else {
            int index = selectedVenue.indexOf(clickVenue);
            if (index != 0 && index != selectedVenue.size() - 1) {
                lastVenue = selectedVenue.get(index - 1);
                nextVenue = selectedVenue.get(index + 1);
            } else {
                updateData(clickVenue, isAdd, null, 0, 0);
                return;
            }
        }
        this.isAdd = isAdd;
        showLoadingView();
        if (null == mAMapNavi) {
            mAMapNavi = AMapNavi.getInstance(getContext());
            //添加监听回调，用于处理算路成功
            mAMapNavi.addAMapNaviListener(mNaviListener);
        } else {
            pedestrianRoutePlanning();
        }
    }

    NaviLatLng mFrom;
    NaviLatLng mTo;

    private void pedestrianRoutePlanning() {
        mFrom = new NaviLatLng(lastVenue.getLat(), lastVenue.getLng());
        mTo = new NaviLatLng(nextVenue.getLat(), nextVenue.getLng());
        boolean isSuccess = mAMapNavi.calculateWalkRoute(mFrom, mTo);
        if (!isSuccess) {
            hideLoadingView();
            ToastHelper.showShort(R.string.route_planning_failure);
        }
    }

    private NaviListener mNaviListener = new NaviListener() {
        @Override
        public void onCalculateRouteFailure(int i) {
            hideLoadingView();
            // 路线规划失败回调
            ToastHelper.showLong("路线规划失败");
        }

        @Override
        public void onInitNaviSuccess() {
            pedestrianRoutePlanning();
        }

        @Override
        public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
            // 算路返回结果
            AMapNaviPath naviPath = mAMapNavi.getNaviPath();
            List<NaviLatLng> latLngs = naviPath.getCoordList();
            latLngs.add(0, mFrom);
            latLngs.add(mTo);
            hideLoadingView();
            updateData(clickVenue, isAdd, latLngs, naviPath.getAllLength(), naviPath.getAllTime());
        }
    };

    private DecimalFormat mDecimalFormat;
    private String getFormatDistance(int distance){
        String units = "m";
        if (distance >= 1000) {
            units = "km";
            distance = distance / 1000;
        }
        if (mDecimalFormat == null) {
            mDecimalFormat = new DecimalFormat( "#######.00" );
        }
        return mDecimalFormat.format( distance ) + units;
    }
    private String getFormatDuration(int duration){
        String units = "s";
        int min = 0;
        if (duration >= 60){
            min = duration / 60;
            units = "min";
        }
        return min + units;
    }
}
