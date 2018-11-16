package com.expo.module.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;

import com.amap.api.fence.GeoFenceClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.ExpoApp;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.StatusBarUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.NavigationContract;
import com.expo.entity.ActualScene;
import com.expo.map.LocationManager;
import com.expo.utils.BitmapUtils;
import com.expo.utils.CameraManager;
import com.expo.utils.Constants;
import com.expo.utils.DeviceRotateManager;
import com.expo.utils.ICameraManager;
import com.expo.widget.MultiDirectionSlidingDrawer;
import com.expo.widget.X5WebView;

import java.util.Locale;

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
    TextureView mTextureView;
    public AMapNavi mAMapNavi;
    private AMap mMap;
    private ActualScene virtualScene;
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

    @Override
    protected int getContentView() {
        return R.layout.activity_navigation;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        StatusBarUtils.setStatusBarLight( this, true );
        mMapView.onCreate( savedInstanceState );
        mCenterX = getResources().getDisplayMetrics().widthPixels / 2;
        mCenterY = (int) (getResources().getDisplayMetrics().heightPixels * 0.85f);
        mPhotoSize = getResources().getDimensionPixelSize( R.dimen.dms_54 );
        mSlidingDrawerView.getLayoutParams().height = (int) (getResources().getDisplayMetrics().heightPixels * 0.666667f);
        mCameraManager = new CameraManager();
        mCameraManager.setFacing( true );
        mCameraManager.setDisplayView( mTextureView );
        initMapNaviView();
        long spotId = getIntent().getLongExtra( Constants.EXTRAS.EXTRA_SPOT_ID, -1 );
        virtualScene = mPresenter.loadSceneById( spotId );
        initSlidingDrawer();
        initWebView();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    private void initWebView() {
        mWebView.loadUrl( "file:///android_asset/newWebAr/liveNav.html" );
        mWebView.addJavascriptInterface( new TerminalInterface(), "Terminal_Interface" );
    }

    private void endNavigation() {
        ToastHelper.showShort( "到达目的地附近喽，导航结束" );
        if (mSlidingDrawerView.isOpened()) {
            changeAnimation( "endPoint" );
        } else {
            new AlertDialog.Builder( this )
                    .setTitle( "临时提示" )
                    .setMessage( "到达终点喽，目标接在附近！" )
                    .setNegativeButton( "知道了", (dialog, which) -> finish() )
                    .show();
        }
    }

    private void initSlidingDrawer() {
        if (PrefsHelper.getBoolean( Constants.Prefs.KEY_IS_OPEN_SLIDINGDRAWER, true )) {
            mSlidingDrawerView.open();
            mCameraManager.startPreview();
            updateMapStatue( true );
        } else {
            updateMapStatue( false );
        }
        mSlidingDrawerView.setOnDrawerCloseListener( () -> {
            mCameraManager.stopPreview();
            PrefsHelper.setBoolean( Constants.Prefs.KEY_IS_OPEN_SLIDINGDRAWER, false );
            updateMapStatue( false );
        } );
        mSlidingDrawerView.setOnDrawerOpenListener( () -> {
            mCameraManager.startPreview();
            PrefsHelper.setBoolean( Constants.Prefs.KEY_IS_OPEN_SLIDINGDRAWER, true );
            updateMapStatue( true );
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
        mMap.getUiSettings().setZoomControlsEnabled( false );
        mMap.getUiSettings().setTiltGesturesEnabled( false );
        DeviceRotateManager.getInstance().registerOrientationChangedListener( mOnOrientationChangedListener );
        LocationManager.getInstance().registerLocationListener( mOnLocationChangeListener );
        showLoadingView();
    }

    private AMap.OnMyLocationChangeListener mOnLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
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
        }
    };
    private DeviceRotateManager.OnOrientationChangedListener mOnOrientationChangedListener = new DeviceRotateManager.OnOrientationChangedListener() {
        @Override
        public void onChanged(float azimuth, float pitch, float roll) {
            if (!mUseDeviceRotate) return;
            //计算角度以通知HTML页面使用
            float degrees;
            if (mCalculateDirection >= 0) {
                degrees = azimuth - mCalculateDirection;
            } else {
                degrees = azimuth - mRouteDirection;
            }
            if (mJsCanSend && mSlidingDrawerView.isOpened()) {
                mWebView.loadUrl( String.format( Locale.getDefault(), "javascript:getAzimuthInfo(%.2f,%.2f)", degrees, azimuth ) );
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
        mMapView.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        DeviceRotateManager.getInstance().unregisterOrientationChangedListener( mOnOrientationChangedListener );
        LocationManager.getInstance().unregisterLocationListener( mOnLocationChangeListener );
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        mMapView.onDestroy();
        super.onDestroy();
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
    }

    private void startCalculateTheRoad(LatLng startLatLng, ActualScene virtualScene) {
        mFrom = new NaviLatLng( startLatLng.latitude, startLatLng.longitude );
        mTo = new NaviLatLng( virtualScene.getLat(), virtualScene.getLng() );
        boolean isSuccess = mAMapNavi.calculateWalkRoute( mFrom, mTo );
        if (!isSuccess) {
            ToastHelper.showShort( "路线规划失败" );
            Log.d( "NaviManager=======", "路线计算失败,检查参数情况" );
        }
    }

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
            ToastHelper.showLong( "启动导航" );
        }

        @Override
        public void onGetNavigationText(String s) {
            if (s == null) return;
            ToastHelper.showShort( s );
            if (s.startsWith( "左转" )) {
                changeAnimation( "turnLeft" );
            } else if (s.startsWith( "右转" )) {
                changeAnimation( "turnRight" );
            } else if (s.startsWith( "前方直行" )) {
                changeAnimation( "forward" );
            }
        }

        @Override
        public void onGpsOpenStatus(boolean b) {
            // 用户手机GPS设置是否开启的回调函数。
            ToastHelper.showLong( "手机GPS设置是否开启" + b );
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
            // 算路返回结果
            AMapNaviPath naviPath = mAMapNavi.getNaviPath();
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
//            mAMapNavi.startNavi( NaviType.EMULATOR );     // 模拟导航
            hideLoadingView();
        }

        @Override
        public void onNaviInfoUpdate(NaviInfo naviInfo) {
            mRouteDirection = naviInfo.getDirection();
        }
    };

    @OnClick(R.id.gt_navi_back)
    public void onClick(View v) {
        finish();
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
    public static void startActivity(@NonNull Context context, long spotId) {
        if (spotId <= 0) {
            ToastHelper.showShort( R.string.error_params );
            return;
        }
        Intent in = new Intent( context, NavigationActivity.class );
        in.putExtra( Constants.EXTRAS.EXTRA_SPOT_ID, spotId );
        context.startActivity( in );
    }
}
