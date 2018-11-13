package com.expo.module.map;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Pair;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.expo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NaviRouteOverlay {
    private final String ROUTE_PASSED = "route_passed";
    private final String ROUTE_NOT_PASSED = "route_not_passed";

    private List<LatLng> allPointsOnRoute;
    private Marker mStartMarker;
    private Marker mEndMarker;
    private Marker mCarMarker;
    private Map<String, Polyline> allPolylines;
    private Map<String, PolylineOptions> allPolylineOptions;
    private AMap mAMap;
    private AMapNaviPath mNaviPath;
    private LatLng mStartPoint;
    private LatLng mEndPoint;
    private LatLngBounds mRouteBounds;

    private float mRouteWidth;
    private int mPassedRouteColor;
    private int mRouteColor;
    private int mDottedLineType;
    private boolean mDottedLine;
    private int mPassedRouteCustomTexture;
    private int mRouteCustomTexture;
    private int mStartMarkerIcon;
    private int mEndMarkerIcon;
    private Bitmap mCarMarkerBitmap;
    private boolean mPassedRouteVisible = true;
    private boolean mAddToMaped;
    private boolean mRemoved;
    private boolean mResetting;
    private boolean mAutoToCenter = true;
    private boolean isAnimating;

    public NaviRouteOverlay(AMap aMap, AMapNaviPath path, NaviLatLng startPoint, NaviLatLng endPoint) {
        this.mAMap = aMap;
        this.mStartPoint = AMapServicesUtil.convertToLatLng( startPoint );
        this.mEndPoint = AMapServicesUtil.convertToLatLng( endPoint );
        this.mNaviPath = path;
    }

    /**
     * 添加路线覆盖物到地图
     */
    public void addToMap() {
        mRouteBounds = null;
        init();
        updatePointOnRouteData();
        setPolylineOptionsData();
        showPolyline();
        addStartAndEndMarker();
        mAddToMaped = true;
    }

    /*
     * 更新路线上的所有点数据
     */
    private void updatePointOnRouteData() {
        if (!allPointsOnRoute.isEmpty()) {
            allPointsOnRoute.clear();
        }
        allPointsOnRoute.add( this.mStartPoint );
        List<LatLng> routes = AMapServicesUtil.convertArrList2( mNaviPath.getCoordList() );
        allPointsOnRoute.add( routes.get( 0 ) );
        allPointsOnRoute.addAll( routes );
        allPointsOnRoute.add( routes.get( routes.size() - 1 ) );
        allPointsOnRoute.add( this.mEndPoint );
    }

    public void setAutoToCenter(boolean auto) {
        mAutoToCenter = auto;
    }

    /*
     * 设置折线覆盖物数据
     */
    private void setPolylineOptionsData() {
        //添加为走过路线的数据
        allPolylineOptions.get( ROUTE_NOT_PASSED )
                .setPoints( allPointsOnRoute );

        //清除规划路段已路过路段数据
        if (!allPolylineOptions.get( ROUTE_PASSED ).getPoints().isEmpty()) {
            allPolylineOptions.get( ROUTE_PASSED ).getPoints().clear();
        }
    }

    /*
     * 更新折线数据数据
     */
    private void updatePolylinesData() {
        //实际规划路段
        allPolylines.get( ROUTE_NOT_PASSED )
                .setPoints( allPointsOnRoute );

        //清除规划路段已路过路段数据
        if (!allPolylines.get( ROUTE_PASSED ).getPoints().isEmpty()) {
            allPolylines.get( ROUTE_PASSED ).getPoints().clear();
        }
    }

    /**
     * 移除路线覆盖物
     */
    public void removeFromMap() {
        mAddToMaped = false;
        mRemoved = true;
        if (this.mStartMarker != null) {
            this.mStartMarker.remove();
            this.mStartMarker.destroy();
            this.mStartMarker = null;
        }
        if (this.mEndMarker != null) {
            this.mEndMarker.remove();
            this.mEndMarker.destroy();
            this.mEndMarker = null;
        }
        if (this.mCarMarker != null) {
            this.mCarMarker.remove();
            this.mCarMarker.destroy();
            this.mCarMarker = null;
        }
        for (Polyline polyline : allPolylines.values()) {
            polyline.remove();
        }
        if (allPolylines != null && !allPolylines.isEmpty()) {
            allPolylines.clear();
        }
        if (allPolylineOptions != null && !allPolylineOptions.isEmpty()) {
            allPolylineOptions.clear();
        }
        if (allPointsOnRoute != null && !allPointsOnRoute.isEmpty()) {
            allPointsOnRoute.clear();
        }
        allPolylineOptions = null;
        allPolylines = null;
        allPointsOnRoute = null;
        mAMap = null;
        mNaviPath = null;
        mRouteBounds = null;
    }

    /**
     * 设置经过的路线是否显示
     *
     * @param visible
     */
    public void setPassedRouteVisible(boolean visible) {
        this.mPassedRouteVisible = visible;
        updatePassedRouteVisible();
    }

    /**
     * 定位点与路线下一拐点的方位角
     *
     * @param location 定位坐标
     * @return 正常值[0-360]  -1未获取角度
     */
    public float updatePassedRoute(LatLng location) {
        if (!mAddToMaped || mResetting || allPolylines.isEmpty() || allPointsOnRoute.isEmpty()) {
            return -1;
        }
        int count = allPointsOnRoute.size();
        Pair<Integer, LatLng> result = SpatialRelationUtil.calShortestDistancePoint( allPointsOnRoute, location );
        if (mAutoToCenter && !isAnimating) {
            if (mAMap.getCameraPosition().zoom != mAMap.getMaxZoomLevel()) {
                //自车移动到地图中心
                mAMap.moveCamera( CameraUpdateFactory.newLatLngZoom( result.second, mAMap.getMaxZoomLevel() ) );
            } else {
                mAMap.moveCamera( CameraUpdateFactory.changeLatLng( result.second ) );
            }
        }
        mCarMarker.setPosition( result.second );
        float angle = -1;
        if (result.first < 2 || result.first > allPointsOnRoute.size() - 3) {
            angle = AMapServicesUtil.calculateAzimuth( location, allPointsOnRoute.get( 1 ) );
        }
        if (result.second.equals( allPointsOnRoute.get( 0 ) )) {
            allPolylines.get( ROUTE_NOT_PASSED ).setPoints( allPointsOnRoute );
            allPolylines.get( ROUTE_PASSED ).setPoints( new ArrayList<>() );
        } else {
            List<LatLng> realPassed = new ArrayList<>();
            LinkedList<LatLng> real = new LinkedList<>();
            for (int i = 0; i < count - 1; i++) {
                if (i < result.first) {
                    realPassed.add( allPointsOnRoute.get( i ) );
                } else if (i == result.first) {
                    realPassed.add( allPointsOnRoute.get( i ) );
                    realPassed.add( result.second );
                    real.add( result.second );
                } else {
                    real.add( allPointsOnRoute.get( i ) );
                }
            }
            allPolylines.get( ROUTE_NOT_PASSED ).setPoints( real );
            allPolylines.get( ROUTE_PASSED ).setPoints( realPassed );
        }
        return angle;
    }

    public void showBounds() {
        if (mRouteBounds == null) {
            LatLngBounds.Builder builder = LatLngBounds.builder();
            for (LatLng ll : allPointsOnRoute) {
                builder.include( ll );
            }
            mRouteBounds = builder.build();
        }
        mAMap.animateCamera( CameraUpdateFactory.newLatLngBounds( mRouteBounds, 60 ) );
    }

    public void resetRouteLineData(AMapNaviPath path, NaviLatLng start, NaviLatLng end) {
        if (mRemoved) {
            throw new RuntimeException( "NaviRouteOverlay was removed" );
        }
        mRouteBounds = null;
        mResetting = true;
        this.mNaviPath = path;
        this.mStartPoint = AMapServicesUtil.convertToLatLng( start );
        this.mEndPoint = AMapServicesUtil.convertToLatLng( end );
        updatePointOnRouteData();
        updatePolylinesData();
        updateMarkerData();
        mResetting = false;
    }

    private void updateMarkerData() {
        this.mStartMarker.setPosition( this.mStartPoint );
        this.mStartMarker.setToTop();
        this.mEndMarker.setPosition( this.mEndPoint );
        this.mEndMarker.setToTop();
    }

    /*
     * 更新已经过路线显示状态
     */
    private void updatePassedRouteVisible() {
        if (allPolylines == null || allPolylines.isEmpty()) return;
        if (allPolylines.containsKey( ROUTE_PASSED )) {
            allPolylines.get( ROUTE_PASSED ).setVisible( mPassedRouteVisible );
        }
    }

    /*
     * 添加折线到地图
     */
    private void showPolyline() {
        allPolylines.put( ROUTE_PASSED,
                mAMap.addPolyline( allPolylineOptions.get( ROUTE_PASSED ) ) );
        allPolylines.put( ROUTE_NOT_PASSED,
                mAMap.addPolyline( allPolylineOptions.get( ROUTE_NOT_PASSED ) ) );
    }

    /*
     * 添加起点和终点图标
     */
    private void addStartAndEndMarker() {
        mStartMarker = mAMap.addMarker( (new MarkerOptions())
                .position( mStartPoint ).icon( getStartMarkerIcon() )
                .title( "\u8D77\u70B9" ) );//起点
        mStartMarker.setToTop();

        mEndMarker = mAMap.addMarker( (new MarkerOptions()).position( mEndPoint )
                .icon( getEndMarkerIcon() ).title( "\u7EC8\u70B9" ) );//终点
        mEndMarker.setToTop();

        mCarMarker = mAMap.addMarker( new MarkerOptions().position( mStartPoint )
                .icon( getCarMarkerIcon() ).title( "car" ).anchor( 0.5f, 0.5f ) );
        mCarMarker.setToTop();
    }

    /*
     * 初始化折线数据
     */
    private void init() {
        allPointsOnRoute = new ArrayList<>();
        allPolylines = new HashMap<>();
        allPolylineOptions = new HashMap<>();
        //正式路线走过的
        PolylineOptions realPassed = new PolylineOptions()
                .width( getRouteWidth() )
                .setDottedLine( mDottedLine )
                .setDottedLineType( getDottedLineType() )
                .visible( mPassedRouteVisible );
        if (mPassedRouteCustomTexture != 0) {
            realPassed.setCustomTexture( getPassedRouteCustomTexture() );
        } else {
            realPassed.color( getPassedRouteColor() );
        }
        allPolylineOptions.put( ROUTE_PASSED, realPassed );
        //正式路线未走过的
        PolylineOptions real = new PolylineOptions()
                .width( getRouteWidth() )
                .setDottedLine( mDottedLine )
                .setDottedLineType( getDottedLineType() );
        if (mRouteCustomTexture != 0) {
            real.setCustomTexture( getRouteCustomTexture() );
        } else {
            real.color( getRouteColor() );
        }
        allPolylineOptions.put( ROUTE_NOT_PASSED, real );
    }

    /*
     * 获取起点Marker图标
     */
    private BitmapDescriptor getStartMarkerIcon() {
        if (mStartMarkerIcon == 0) {
            return BitmapDescriptorFactory.fromResource( R.drawable.navi_start );
        }
        return BitmapDescriptorFactory.fromResource( mStartMarkerIcon );
    }

    /*
     * 获取终点Marker图标
     */
    private BitmapDescriptor getEndMarkerIcon() {
        if (mEndMarkerIcon == 0) {
            return BitmapDescriptorFactory.fromResource( R.drawable.navi_end );
        }
        return BitmapDescriptorFactory.fromResource( mEndMarkerIcon );
    }

    /*
     * 获取自车Marker图标
     */
    private BitmapDescriptor getCarMarkerIcon() {
        if (mCarMarkerBitmap == null) {
            return BitmapDescriptorFactory.fromResource( R.drawable.navi_map_gps_locked );
        }
        return BitmapDescriptorFactory.fromBitmap( mCarMarkerBitmap );
    }

    public void setCarMarkerIcon(Bitmap bmp) {
        if (bmp != null) {
            mCarMarkerBitmap = bmp;
        }
    }

    /*
     * 获取经过路线纹理
     */
    private BitmapDescriptor getPassedRouteCustomTexture() {
        return BitmapDescriptorFactory.fromResource( mPassedRouteCustomTexture );
    }

    /*
     * 获取路线纹理
     */
    private BitmapDescriptor getRouteCustomTexture() {
        return BitmapDescriptorFactory.fromResource( mRouteCustomTexture );
    }

    public void setDottedLine(boolean dottedLine) {
        this.mDottedLine = dottedLine;
    }

    public void setRouteWidth(float routeWidth) {
        this.mRouteWidth = routeWidth;
    }

    public void setPassedRouteColor(int passedRouteColor) {
        this.mPassedRouteColor = passedRouteColor;
    }

    public void setRouteColor(int routeColor) {
        this.mRouteColor = routeColor;
    }

    public void setPassedRouteCustomTexture(int passedRouteCustomTexture) {
        this.mPassedRouteCustomTexture = passedRouteCustomTexture;
    }

    public void setRouteCustomTexture(int routeCustomTexture) {
        this.mRouteCustomTexture = routeCustomTexture;
    }

    public void setStartMarkerIcon(int startMarkerIcon) {
        this.mStartMarkerIcon = startMarkerIcon;
    }

    public void setEndMarkerIcon(int endMarkerIcon) {
        this.mEndMarkerIcon = endMarkerIcon;
    }

    private float getRouteWidth() {
        return mRouteWidth == 0 ? 18f : mRouteWidth;
    }

    private int getPassedRouteColor() {
        return mPassedRouteColor == 0 ? Color.GRAY : mPassedRouteColor;
    }

    private int getRouteColor() {
        return mRouteColor == 0 ? Color.GRAY : mRouteColor;
    }

    public int getDottedLineType() {
        if (mDottedLineType == 0) {
            mDottedLineType = PolylineOptions.DOTTEDLINE_TYPE_SQUARE;
        }
        return mDottedLineType;
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public void moveCarToCenter() {
        if (!isAnimating) {
            isAnimating = true;
            mAMap.animateCamera( CameraUpdateFactory.newLatLngZoom( mCarMarker.getPosition(), mAMap.getMaxZoomLevel() ),
                    new AMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            isAnimating = false;
                        }

                        @Override
                        public void onCancel() {
                            isAnimating = false;
                        }
                    } );
        }
    }
}
