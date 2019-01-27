package com.expo.module.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.fence.GeoFenceClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.ExpoApp;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.StatusBarUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.NavigationContract;
import com.expo.entity.Destination;
import com.expo.entity.Encyclopedias;
import com.expo.entity.TouristType;
import com.expo.entity.Venue;
import com.expo.entity.VenuesDistance;
import com.expo.map.LocationManager;
import com.expo.map.MapUtils;
import com.expo.media.MediaPlayerManager;
import com.expo.network.Http;
import com.expo.utils.BitmapUtils;
import com.expo.utils.CameraManager;
import com.expo.utils.CommUtils;
import com.expo.utils.Constants;
import com.expo.utils.DeviceRotateManager;
import com.expo.utils.ICameraManager;
import com.expo.utils.LanguageUtil;
import com.expo.widget.CustomDefaultDialog;
import com.expo.widget.MultiDirectionSlidingDrawer;
import com.expo.widget.X5WebView;
import com.gl.BottomArcsTextureView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 世园会实景导航页
 */
public class NavigationActivity extends BaseActivity<NavigationContract.Presenter> implements NavigationContract.View {

    @BindView(R.id.navi_map)
    TextureMapView mMapView;
    @BindView(R.id.sliding_drawer)
    MultiDirectionSlidingDrawer mSlidingDrawerView;
    @BindView(R.id.handle)
    View handle;
    @BindView(R.id.gt_navi_bar)
    ImageView imgBar;
    @BindView(R.id.web_view)
    X5WebView mWebView;
    @BindView(R.id.texture_view)
    BottomArcsTextureView mTextureView;
    @BindView(R.id.gt_navi_tips)
    TextView mTvTips;
    @BindView(R.id.img_module_show)
    ImageView mImgModuleShow;
    @BindView(R.id.navigation_show)
    ImageView mImgNavigationShow;

    public AMapNavi mAMapNavi;
    private AMap mMap;
    private Venue virtualScene;
    private ICameraManager mCameraManager;

    private GeoFenceClient mGeoFenceClient;
    private NaviLatLng mFrom;
    private NaviLatLng mTo;
    private NaviRouteOverlay mNaviRouteOverlay;
    private float mCalculateDirection;
    private float mRouteDirection;
    private boolean mAutoToCenter;
    private boolean mUseDeviceRotate = true;
    private boolean isFirst = true;
    private int mCenterX;
    private int mCenterY;
    private int mPhotoSize;
    private boolean mJsCanSend;
    private Venue mVenue;

    Map<String, Marker> mMarker;
    List<VenuesDistance> mVenuesDistance;
    MythredFresh mMythredFresh;
    MythredCalculate mMythredcCalculate;

    private MapUtils mMapUtils;

    final Object LOCK = new Object();

    long mStartTime;
    int mRouteTime;
    int mAllLength = 0;

    boolean isBackStage = false;

    Location mLocation;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );
            switch (msg.what) {
                case Constants.HandlerMsg.NAVI_NEAR_WIKI:
                    String nearWikeStr = (String) msg.obj;
                    mWebView.loadUrl( "javascript:nearWiki('" + nearWikeStr + "')" );
                    break;
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_navigation;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        TouristType touristType = mPresenter.getTourist();
        if (touristType == null || StringUtils.isEmpty( touristType.getUnZipPath() )) {
            ToastHelper.showShort( R.string.set_tourist_first );
            finish();
            return;
        }

        if (!PrefsHelper.getBoolean( Constants.Prefs.KEY_MAP_ON_OFF, false )) {
            mImgModuleShow.setVisibility( View.GONE );
        }
        mImgModuleShow.setImageResource( PrefsHelper.getBoolean( Constants.Prefs.KEY_MODULE_ON_OFF,
                true ) ? R.mipmap.ico_module_on : R.mipmap.ico_module_off );
        StatusBarUtils.setStatusBarLight( this, true );
        mMapView.onCreate( savedInstanceState );
        //地图中心点
        mCenterX = getResources().getDisplayMetrics().widthPixels / 2;
        mCenterY = (int) (getResources().getDisplayMetrics().heightPixels * 0.95f);
        //头像
        mPhotoSize = getResources().getDimensionPixelSize( R.dimen.dms_54 );
        int previewHeight = computePreviewHeight();
        mSlidingDrawerView.getLayoutParams().height = previewHeight;
        mCameraManager = new CameraManager();
        mCameraManager.setFacing( true );
        mTextureView.setCornerRadius( getResources().getDisplayMetrics().widthPixels * 1.5f );
        mCameraManager.setDisplayView( mTextureView );
        initMapNaviView();
        initPoiSearch();
        mVenue = getIntent().getParcelableExtra( Constants.EXTRAS.EXTRAS );
        virtualScene = mPresenter.loadSceneById( mVenue.getId() );
        initSlidingDrawer();
        initWebView();
        MediaPlayerManager.getInstence().setListener( null );
    }

    private int computePreviewHeight() {
        int previewHeight;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        float screenRatio = (float) screenHeight / screenWidth;
        if (screenRatio >= 1.9) {
            previewHeight = (int) (screenWidth * 1.666667f);
        } else if (screenRatio >= 1.6) {
            previewHeight = (int) (screenWidth * 1.222223f);
        } else {
            previewHeight = screenWidth;
        }
        return previewHeight;
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    private void initWebView() {
        mWebView.loadUrl( "file:///android_asset/web/merge.html" );
        mWebView.loadUrl( "javascript:toggleImg(" + (PrefsHelper.getBoolean( Constants.Prefs.KEY_MODULE_ON_OFF, true ) ? 1 : 0) + ")" );
//        mWebView.loadUrl("http://192.168.1.143:8080/dist1/index.html#/navigation");
        mWebView.addJavascriptInterface( new TerminalInterface(), "Terminal_Interface" );
    }

    private void endNavigation() {
        ToastHelper.showShort( R.string.end_of_the_navigation );
        mTvTips.setText( R.string.end_of_the_navigation );
        if (mSlidingDrawerView.isOpened()) {
            changeAnimation( "endPoint" );
        } else {
//            new AlertDialog.Builder(this)
//                    .setTitle("临时提示")
//                    .setMessage("到达终点喽，目标接在附近！")
//                    .setNegativeButton("知道了", (dialog, which) -> finish())
//                    .show();
            mWebView.loadUrl( String.format( "javascript:tipTips('%s', '%s')", Constants.NaviTip.TO_JS_NAVI_TIP_TYPE, Constants.NaviTip.TO_JS_NAVI_TIP_END ) );
            showEndView();
        }
    }

    /**
     * 下拉窗体
     */
    private void initSlidingDrawer() {
        if (PrefsHelper.getBoolean( Constants.Prefs.KEY_IS_OPEN_SLIDINGDRAWER, true )) {
            mSlidingDrawerView.open();
            mCameraManager.startPreview();
            updateMapStatue( true );
            mImgNavigationShow.setVisibility( View.GONE );
        } else {
            updateMapStatue( false );
            mImgNavigationShow.setVisibility( View.VISIBLE );
        }
        mSlidingDrawerView.setOnDrawerCloseListener( () -> {
            PrefsHelper.setBoolean( Constants.Prefs.KEY_IS_OPEN_SLIDINGDRAWER, false );
            updateMapStatue( false );
            mImgNavigationShow.setVisibility( View.VISIBLE );
            mImgNavigationShow.startAnimation( AnimationUtils.loadAnimation( getContext(), R.anim.slide_in_bottom ) );
        } );
        mSlidingDrawerView.setOnDrawerOpenListener( () -> {
            PrefsHelper.setBoolean( Constants.Prefs.KEY_IS_OPEN_SLIDINGDRAWER, true );
            updateMapStatue( true );
            mImgNavigationShow.startAnimation( AnimationUtils.loadAnimation( getContext(), R.anim.slide_out_bottom ) );
            mImgNavigationShow.setVisibility( View.GONE );
        } );
    }

    /*
     * 设置是否自动移动到中心状态
     */
    private void updateMapStatue(boolean auto) {
        if (mAutoToCenter != auto) {//因初始化SlidingDrawer时还未创建出路线，所以不能直接更改mNaviRouteOverlay.setAutoToCenter
            mAutoToCenter = auto;
            if (auto) {
                //禁止操作
                mMap.getUiSettings().setAllGesturesEnabled( false );
                mMap.setPointToCenter( mCenterX, mCenterY );
                if (mNaviRouteOverlay != null) {
                    mNaviRouteOverlay.moveCarToCenter();
                }
            } else {
                //解除禁止操作
                mMap.getUiSettings().setAllGesturesEnabled( true );
                mMap.setPointToCenter( mCenterX, getResources().getDisplayMetrics().heightPixels / 2 );
                mMap.moveCamera( CameraUpdateFactory.changeTilt( 0 ) );
                if (mNaviRouteOverlay != null) {
                    mNaviRouteOverlay.showBounds();
                }
            }
            if (mNaviRouteOverlay != null) {
                mNaviRouteOverlay.setAutoToCenter( mAutoToCenter );
            }
        }
        mUseDeviceRotate = auto;
    }

    private void initMapNaviView() {
        mMap = mMapView.getMap();
        mMarker = new HashMap<>();
        mMapUtils = new MapUtils( mMap );
        mMap.getUiSettings().setZoomControlsEnabled( false );
        mMap.getUiSettings().setTiltGesturesEnabled( false );
        DeviceRotateManager.getInstance().registerOrientationChangedListener( mOnOrientationChangedListener );//设备旋转
        LocationManager.getInstance().registerLocationListener( mOnLocationChangeListener );//定位
        mMap.setOnMarkerClickListener( mMarkerClickListener );
        mMap.setOnMapClickListener( mapClickListener );
        showLoadingView();
    }

    private AMap.OnMapClickListener mapClickListener = new AMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if (mSlidingDrawerView.isOpened()) {
                mCameraManager.stopPreview();
                mSlidingDrawerView.animateClose();
            }
        }
    };

    private void initPoiSearch() {
        mVenuesDistance = mPresenter.getVenues();
    }

    private AMap.OnMyLocationChangeListener mOnLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (mLocation == null) {
                mPresenter.submitTouristRecord( location.getLatitude(), location.getLongitude(), mVenue.getLat(), mVenue.getLng(), location.getSpeed() );
            }
            mLocation = location;
            if (isFirst) {//第一次定位成功后移动到地图中心
                isFirst = false;
                mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( new LatLng( location.getLatitude(), location.getLongitude() ), 20 ) );
            }
            //设置导航使用的位置数据
            mAMapNavi.setExtraGPSData( 2, location );
            //更新走过的路线
            if (mNaviRouteOverlay != null) {
                mCalculateDirection = mNaviRouteOverlay.updatePassedRoute( new LatLng( location.getLatitude(), location.getLongitude() ) );
            }
            searchPoi( location );
        }
    };
    private DeviceRotateManager.OnOrientationChangedListener mOnOrientationChangedListener = new DeviceRotateManager.OnOrientationChangedListener() {
        @Override
        public void onChanged(float azimuth, float pitch, float roll) {
            //azimuth 手机与正北方夹角
            if (mLocation == null) return;
            Destination des = new Destination();
            des.content = LanguageUtil.chooseTest( mVenue.getCaption(), mVenue.getEnCaption() );
            des.distance = AMapUtils.calculateLineDistance(
                    new LatLng( mVenue.getLat(), mVenue.getLng() ),
                    new LatLng( mLocation.getLatitude(), mLocation.getLongitude() ) );
            double angle2P = mMapUtils.getAngle( new MapUtils.MyLatLng( mLocation.getLongitude(), mLocation.getLatitude() ), new MapUtils.MyLatLng( mVenue.getLng(), mVenue.getLat() ) );
            double angle = azimuth + 360 - angle2P;
            if (angle <= 180 && angle >= 0) {
                des.angle = angle + "";
            } else if (angle > 180 && angle >= 0) {
                des.angle = angle - 360 + "";
            } else if (angle > -180 && angle < 0) {
                des.angle = -angle + "";
            } else {
                des.angle = 360 - angle + "";
            }
            des.routeTime = mRouteTime;
            if (!mUseDeviceRotate) {
                des.angle = "";
                mWebView.loadUrl( String.format( Locale.getDefault(), "javascript:setMarker(1,'" + Http.getGsonInstance().toJson( des ) + "')" ) );
                return;
            }
            //计算角度以通知HTML页面使用
//            float degrees; // 当前方向与路的夹角
//            if (mCalculateDirection >= 0) {
//                degrees = azimuth - mCalculateDirection;
//            } else {
//                degrees = azimuth - mRouteDirection;
//            }
            if (mJsCanSend && mSlidingDrawerView.isOpened()) {
                //degrees 当前方向和路的夹角
                //azimuth 陀螺仪和正北方夹角
//                mWebView.loadUrl(String.format(Locale.getDefault(), "javascript:getAzimuthInfo(%.2f,%.2f)", degrees, azimuth));
                mWebView.loadUrl( "javascript:setMarker('1','" + Http.getGsonInstance().toJson( des ) + "')" );
            }
            if (mNaviRouteOverlay == null || !mNaviRouteOverlay.isAnimating()) {
                if (pitch < 0 && pitch > -60) {
                    mMap.moveCamera( CameraUpdateFactory.changeTilt( Math.abs( pitch ) ) );
                }
                mMap.moveCamera( CameraUpdateFactory.changeBearing( azimuth ) );
            }
        }
    };

    private void changeAnimation(String action) {
        if (mJsCanSend && mSlidingDrawerView.isOpened()) {
            mWebView.loadUrl( String.format( "javascript:getActionState('%s')", action ) );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isBackStage)
            mWebView.loadUrl( String.format( "javascript:tipTips('%s', '%s')", Constants.NaviTip.TO_JS_NAVI_TIP_TYPE, Constants.NaviTip.TO_JS_NAVI_TIP_WAKE ) );
        isBackStage = false;
        mMapView.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        isBackStage = true;
        mMapView.onPause();
        mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        DeviceRotateManager.getInstance().unregisterOrientationChangedListener( mOnOrientationChangedListener );
        LocationManager.getInstance().unregisterLocationListener( mOnLocationChangeListener );
        if (mAMapNavi != null) {
            mAMapNavi.stopNavi();
            mAMapNavi.destroy();
        }
        mMapView.onDestroy();
        MediaPlayerManager.getInstence().onDestory();
        if (mMythredFresh != null)
            mMythredFresh.exit();
        if (mMythredcCalculate != null)
            mMythredcCalculate.exit();
        mHandler.removeCallbacksAndMessages( null );
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        mWebView.loadUrl(String.format("javascript:tipTips('%s', '%s')", Constants.NaviTip.TO_JS_NAVI_TIP_TYPE, Constants.NaviTip.TO_JS_NAVI_TIP_LEAVE_START));
        CustomDefaultDialog dialog = new CustomDefaultDialog(getContext());
        dialog.setContent(R.string.leave_navi)
                .setOnOKClickListener(v1 -> {
                    mWebView.loadUrl("javascript:leavePage()");
                    mWebView.loadUrl("");
                    finish();
                    dialog.dismiss();
                } )
                .setOnCancelClickListener( v -> {
                    mWebView.loadUrl( String.format( "javascript:tipTips('%s', '%s')", Constants.NaviTip.TO_JS_NAVI_TIP_TYPE, Constants.NaviTip.TO_JS_NAVI_TIP_LEAVE_END ) );
                    dialog.dismiss();
                } ).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        mMapView.onSaveInstanceState( outState );
    }

    public void startNavi() {
        //获取AMapNavi实例
        mAMapNavi = AMapNavi.getInstance( getContext() );
        mAMapNavi.setUseInnerVoice( false );       // 使用内部语音播报
        //添加监听回调，用于处理算路成功
        mAMapNavi.addAMapNaviListener( mNaviListener );
        TouristType touristType = mPresenter.getTourist();
        if (touristType != null)
            mWebView.loadUrl( String.format( "javascript:setTourist('%s')", touristType.getUnZipPath() ) );
    }

    private void startCalculateTheRoad(LatLng startLatLng, Venue virtualScene) {
        mFrom = new NaviLatLng( startLatLng.latitude, startLatLng.longitude );
        mTo = new NaviLatLng( virtualScene.getLat(), virtualScene.getLng() );
        boolean isSuccess = mAMapNavi.calculateWalkRoute( mFrom, mTo );
        if (!isSuccess) {
            ToastHelper.showShort( R.string.route_planning_failure );
            Log.d( "NaviManager=======", "路线计算失败,检查参数情况" );
        }
    }

    /**
     * 搜索周边
     */
    private void searchPoi(Location location) {
        if (mMythredcCalculate == null) {
            mMythredcCalculate = new MythredCalculate();
            mMythredcCalculate.setLatLon( location.getLatitude(), location.getLongitude() );
            mMythredcCalculate.start();
        } else {
            mMythredcCalculate.setLatLon( location.getLatitude(), location.getLongitude() );
        }
        if (mMythredFresh == null) {
            mMythredFresh = new MythredFresh();
            mMythredFresh.setLatLon( location.getLatitude(), location.getLongitude() );
            mMythredFresh.start();
        } else {

            mMythredFresh.setLatLon( location.getLatitude(), location.getLongitude() );
        }

    }

    public void freshMarker() {
        List<VenuesDistance> venuesDistanceList = new ArrayList<>();
        if (mMap != null) {
            Map<String, Marker> tem = new HashMap<>();
            for (int i = 0; i < mVenuesDistance.size(); i++) {
                VenuesDistance vd = mVenuesDistance.get( i );
                if (vd.distance >= 20.0f) break;
//                vd.voice = "http://39.105.120.171/res/0339f8f92e8a483fb6ef5aad9b1b7b6e.mp3";
                vd.angle = mMapUtils.getAngle( new MapUtils.MyLatLng( mLocation.getLatitude(), mLocation.getLongitude() ), new MapUtils.MyLatLng( vd.lat, vd.lon ) );
                venuesDistanceList.add( vd );
                if (mMarker.containsKey( vd.id )) {
                    tem.put( vd.id, mMarker.get( vd.id ) );
                    mMarker.remove( vd.id );
                    continue;
                }
                LatLng latLng = new LatLng( vd.lat, vd.lon );
                MarkerOptions markerOptions = new MarkerOptions().position( latLng ).icon( mMapUtils.setMarkerIconDrawable( this, null, "警察局" ) );
                Marker marker = mMap.addMarker( markerOptions );
                marker.setObject( vd.id );
                tem.put( vd.id, mMarker.get( vd.id ) );
                mMarker.remove( vd.id );

            }
            for (Marker marker : mMarker.values()) {
                marker.remove();
            }

            Message message = new Message();
            message.what = Constants.HandlerMsg.NAVI_NEAR_WIKI;
            message.obj = Http.getGsonInstance().toJson( venuesDistanceList );
            mHandler.sendMessage( message );

            mMarker.clear();
            mMarker.putAll( tem );
        }
    }

    AMap.OnMarkerClickListener mMarkerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
            Encyclopedias encyclopedias = mPresenter.getEncyclopedias( (String) marker.getObject() );
            if (!StringUtils.isEmpty( LanguageUtil.chooseTest( encyclopedias.voiceUrl, encyclopedias.voiceUrlEn ) ))
                MediaPlayerManager.getInstence().start( NavigationActivity.this, CommUtils.getFullUrl( LanguageUtil.chooseTest( encyclopedias.voiceUrl, encyclopedias.voiceUrlEn ) ) );
            return true;
        }
    };


    private NaviListener mNaviListener = new NaviListener() {
        @Override
        public void onInitNaviFailure() {
            // 导航初始化失败时的回调函数。
//        ToastHelper.showLong("导航初始化失败");
        }

        @Override
        public void onInitNaviSuccess() {
            // 导航初始化成功时的回调函数。
            startCalculateTheRoad(
                    LocationManager.getInstance().getCurrentLocationLatLng(), virtualScene );
        }

        @Override
        public void onStartNavi(int i) {
            // 启动导航后的回调函数
            ToastHelper.showLong( R.string.start_the_navigation );
            mStartTime = TimeUtils.getNowMills();
            mWebView.loadUrl( String.format( "javascript:tipTips('%s', '%s')", Constants.NaviTip.TO_JS_NAVI_TIP_TYPE, Constants.NaviTip.TO_JS_NAVI_TIP_START ) );
        }

        @Override
        public void onGetNavigationText(String s) {
            if (s == null) return;
            ToastHelper.showShort( s );
            mTvTips.setText( s );
            if (s.startsWith( "左转" )) {
                changeAnimation( "turnLeft" );
            } else if (s.startsWith( "右转" )) {
                changeAnimation( "turnRight" );
            } else if (s.startsWith( "前方直行" )) {
                changeAnimation( "forward" );
            } else if (s.indexOf( "偏离" ) != -1) {
                mWebView.loadUrl( String.format( "javascript:tipTips('%s', '%s')", Constants.NaviTip.TO_JS_NAVI_TIP_TYPE, Constants.NaviTip.TO_JS_NAVI_TIP_GPS_DIVERGE ) );
            }
        }

        @Override
        public void onGpsOpenStatus(boolean b) {
            // 用户手机GPS设置是否开启的回调函数。
            if (!b) {
                ToastHelper.showLong( R.string.the_phone_is_gps_settings_are_not_on );
            }
        }

        @Override
        public void onArriveDestination() {
            endNavigation();
        }

        @Override
        public void onCalculateRouteFailure(int i) {
            // 路线规划失败回调
            Log.e( "NaviManager=======", "路径规划出错" + i );
            ToastHelper.showLong( "路线规划失败" );
        }


        @Override
        public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
            mTvTips.setVisibility( View.VISIBLE );
            // 算路返回结果
            AMapNaviPath naviPath = mAMapNavi.getNaviPath();
            mAllLength = naviPath.getAllLength();
            if (mNaviRouteOverlay == null) {
                mNaviRouteOverlay = new NaviRouteOverlay( mMap, naviPath, mFrom, mTo );
                if (ExpoApp.getApplication().getUserHandBitmap() == null) {
                    mNaviRouteOverlay.setCarMarkerIcon( BitmapUtils.circleBitmap(
                            BitmapFactory.decodeResource( getResources(), R.mipmap.ico_mine_def_photo ), mPhotoSize, mPhotoSize ) );
                } else {
                    mNaviRouteOverlay.setCarMarkerIcon( BitmapUtils.circleBitmap( ExpoApp.getApplication().getUserHandBitmap(), mPhotoSize, mPhotoSize ) );
                }
                mNaviRouteOverlay.setRouteCustomTexture( R.mipmap.ico_route_item );
                mNaviRouteOverlay.setRouteWidth( 40 );
                mNaviRouteOverlay.setPassedRouteCustomTexture( R.mipmap.ico_route_item_passed );
                mNaviRouteOverlay.setAutoToCenter( mAutoToCenter );
                mNaviRouteOverlay.addToMap();
                if (!mAutoToCenter)
                    mNaviRouteOverlay.showBounds();
                if (naviPath == null) {
                    return;
                }
                mAMapNavi.startNavi( NaviType.GPS );
                mAMapNavi.startNavi( NaviType.EMULATOR );     // 模拟导航
                hideLoadingView();

            } else {
                LatLng startLatLng = LocationManager.getInstance().getCurrentLocationLatLng();
                mFrom = new NaviLatLng( startLatLng.latitude, startLatLng.longitude );
                mNaviRouteOverlay.resetRouteLineData( naviPath, mFrom, mTo );
            }
        }

        @Override
        public void onNaviInfoUpdate(NaviInfo naviInfo) {
            mRouteDirection = naviInfo.getDirection();
            mRouteTime = naviInfo.getPathRetainTime();
        }
    };

    @OnClick({R.id.gt_navi_back, R.id.img_module_show, R.id.navigation_show})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gt_navi_back:
                onBackPressed();
                break;
            case R.id.img_module_show:
                PrefsHelper.setBoolean( Constants.Prefs.KEY_MODULE_ON_OFF, !PrefsHelper.getBoolean( Constants.Prefs.KEY_MODULE_ON_OFF, true ) );
                mImgModuleShow.setImageResource( PrefsHelper.getBoolean( Constants.Prefs.KEY_MODULE_ON_OFF,
                        true ) ? R.mipmap.ico_module_on : R.mipmap.ico_module_off );
                mWebView.loadUrl( "javascript:toggleImg(" + (PrefsHelper.getBoolean( Constants.Prefs.KEY_MODULE_ON_OFF, true ) ? 1 : 0) + ")" );
                break;
            case R.id.navigation_show:
                mCameraManager.startPreview();
                mSlidingDrawerView.animateOpen();
                break;
        }
    }

    //提供给网络端的回调方法
    public class TerminalInterface {
        /**
         * 网页加载完成，通知可以开始执行js时调用
         *
         * @param state
         */
        @JavascriptInterface
        public void canCallJS(boolean state) {
            mJsCanSend = state;
            runOnUiThread( NavigationActivity.this::startNavi );
        }
    }

    /**
     * 启动NavigationActivity
     *
     * @param context
     */
    public static void startActivity(@NonNull Context context, Venue venue) {
        if (venue.getId() <= 0) {
            ToastHelper.showShort( R.string.error_params );
            return;
        }
        Intent in = new Intent( context, NavigationActivity.class );
        in.putExtra( Constants.EXTRAS.EXTRAS, venue );
        context.startActivity( in );
    }

    private void showEndView() {
        int time = (int) ((TimeUtils.getNowMills() - mStartTime) / (60 * 1000));

        View view = LayoutInflater.from( this ).inflate( R.layout.dialog_navigation_end, null );

        CommUtils.setImgPic( this, mVenue.getPicUrl(), view.findViewById( R.id.dialog_navigation_end_img ) );
        CommUtils.setText( view.findViewById( R.id.dialog_navigation_end_name ), mVenue.getCaption(), mVenue.getEnCaption() );
        CommUtils.setText( view.findViewById( R.id.dialog_navigation_end_content ), mVenue.getRemark(), mVenue.getEnRemark() );
        ((TextView) view.findViewById( R.id.dialog_navigation_end_duration )).setText( getResources().getString( R.string.time_leng ) + getResources().getString( R.string.within_minutes, time ) );
        if (mAllLength != 0 && mAllLength != -1)
            ((TextView) view.findViewById( R.id.dialog_navigation_end_distance )).setText( getString( R.string.navi_distance, mAllLength ) );

        ViewHolder viewHolder = new ViewHolder( view );
        DialogPlus dialog = DialogPlus.newDialog( this )
                .setContentHolder( viewHolder )
                .setGravity( Gravity.BOTTOM )
                .setContentBackgroundResource( 0 )
                .setMargin( (int) getResources().getDimension( R.dimen.dms_50 ), 0, (int) getResources().getDimension( R.dimen.dms_50 ), 0 )
                .setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.dialog_navigation_end_finish)
                            finish();
                    }
                } )
                .create();
        dialog.show();
    }

    class MythredCalculate extends Thread {
        boolean isExit = false;
        double lat, lon;

        @Override
        public void run() {
            super.run();
            while (!isExit) {
                synchronized (LOCK) {
                    for (VenuesDistance vd : mVenuesDistance) {
                        vd.distance = AMapUtils.calculateLineDistance(
                                new LatLng( lat, lon ), new LatLng( vd.lat, vd.lon ) );
                        vd.distance = mMapUtils.getDistance( new LatLng( lat, lon ), new LatLng( vd.lat, vd.lon ) );
                        vd.angle = mMapUtils.getAngle( new MapUtils.MyLatLng( mLocation.getLongitude(), mLocation.getLatitude() ), new MapUtils.MyLatLng( lon, lat ) );
                    }
                    Collections.sort( mVenuesDistance, new Comparator<VenuesDistance>() {
                        @Override
                        public int compare(VenuesDistance o1, VenuesDistance o2) {
                            return o1.distance <= o2.distance ? 0 : 1;
                        }
                    } );
                }
                SystemClock.sleep( 60 * 1000 );
            }
        }

        public void exit() {
            isExit = true;
        }

        public void setLatLon(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }

    class MythredFresh extends Thread {

        boolean isExit = false;
        double lat, lon;

        @Override
        public void run() {
            super.run();
            while (!isExit) {
                SystemClock.sleep( 5 * 1000 );
                synchronized (LOCK) {
                    freshMarker();
                }
            }
        }

        public void exit() {
            isExit = true;
        }

        public void setLatLon(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }

}
