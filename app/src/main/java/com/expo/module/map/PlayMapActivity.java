package com.expo.module.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.ExpoApp;
import com.expo.base.utils.LogUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.PlayMapContract;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;
import com.expo.map.LocationManager;
import com.expo.map.MapUtils;
import com.expo.network.Http;
import com.expo.services.TrackRecordService;
import com.expo.utils.BitmapUtils;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayMapActivity extends BaseActivity<PlayMapContract.Presenter> implements PlayMapContract.View, AMap.OnMarkerClickListener {

    @BindView(R.id.play_map_destination)
    TextView mTvDestination;       // 目的地
    @BindView(R.id.play_map_distance)
    TextView mTvDistance;       // 距离
    @BindView(R.id.play_map_duration)
    TextView mTvDuration;       // 时长
    @BindView(R.id.play_map_view)
    TextureMapView mMapView;
    @BindView(R.id.play_map_info_view)
    View mInfoView;

    private AMap mAMap;
    private MapUtils mMapUtils;
    public AMapNavi mAMapNavi;

    private Bitmap mBitmap;
    private List<Venue> mVenueList;
    private Venue mClickVenue;
    private NaviRouteOverlay mNaviRouteOverlay;
    private int mPhotoSize;
    private List<Marker> mMarkers;
    private String mVenueTypeString;

    private int onlyPreview = 0;    // 默认 0，    仅预览（活动） 1，  需切换（百科） 2
    NaviLatLng mFrom;
    NaviLatLng mTo;

    @Override
    protected int getContentView() {
        return R.layout.activity_play_map;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(0, "智玩地图");
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mMapUtils = new MapUtils(mAMap);
        mAMap.getUiSettings().setRotateGesturesEnabled(false);
        mAMap.getUiSettings().setTiltGesturesEnabled(false);
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        mAMap.setOnMarkerClickListener(this);
        mPhotoSize = getResources().getDimensionPixelSize(R.dimen.dms_54);
        mVenueTypeString = getIntent().getStringExtra(Constants.EXTRAS.EXTRA_VENUE_TYPE_NAME);
        onlyPreview = getIntent().getIntExtra(Constants.EXTRAS.EXTRA_ONLY_PREVIEW, 0);
        switch (onlyPreview){
            case 0:
                if (!TextUtils.isEmpty(mVenueTypeString)) {
                    mPresenter.queryVenueAndNearByType(mVenueTypeString);
                } else {
                    mClickVenue = Http.getGsonInstance().fromJson(getIntent().getStringExtra(Constants.EXTRAS.EXTRA_JSON_DATA), Venue.class);
                    mBitmap = byteArrayToBitmap(getIntent().getByteArrayExtra(Constants.EXTRAS.EXTRA_BITMAP_BYTE_ARRAY));
                    mVenueList = getIntent().getParcelableArrayListExtra(Constants.EXTRAS.EXTRA_AT_VENUE_LIST);
                    initMapMarker();
                    startNavi();
                }
                break;
            case 1:         // 仅预览（活动） 1
                mClickVenue = Http.getGsonInstance().fromJson(getIntent().getStringExtra(Constants.EXTRAS.EXTRA_JSON_DATA), Venue.class);
                startNavi();
                break;
            case 2:         // 需切换（百科） 2
                mClickVenue = Http.getGsonInstance().fromJson(getIntent().getStringExtra(Constants.EXTRAS.EXTRA_JSON_DATA), Venue.class);
                mPresenter.queryVenuesByVenue(mClickVenue);
                break;
        }
    }

    @OnClick(R.id.play_map_start_navi)
    public void onClick(View v) {
        if (mAMapNavi != null) {
            mAMapNavi.stopNavi();
            mAMapNavi.destroy();
        }
        NavigationActivity.startActivity(getContext(), mClickVenue);
        finish();
    }

    private void initMapMarker() {
        mMarkers = new ArrayList<>();
        for (Venue as : mVenueList) {
            if (as.getLat() == 0)
                continue;
            LatLng latLng = new LatLng(as.getLat(), as.getLng());
            Marker marker = mAMap.addMarker(new MarkerOptions()
                    .icon(mMapUtils.setMarkerIconDrawable(getContext(), mBitmap,
                            LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption())))
                    .anchor(0.5F, 0.90F).position(latLng));
            marker.setObject(as);
            mMarkers.add(marker);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mClickVenue.getId() == ((Venue) marker.getObject()).getId()) {
            return false;
        }
        mClickVenue = (Venue) marker.getObject();
        startNavi();
        return false;
    }

    private void startNavi() {
        showLoadingView();
        if (null == mAMapNavi) {
            mAMapNavi = AMapNavi.getInstance(getContext());
            //添加监听回调，用于处理算路成功
            mAMapNavi.addAMapNaviListener(mNaviListener);
        } else {
            pedestrianRoutePlanning();
        }
    }

    private void pedestrianRoutePlanning() {
        mTo = new NaviLatLng(mClickVenue.getLat(), mClickVenue.getLng());
        mFrom = new NaviLatLng(TrackRecordService.getLocation().getLatitude(), TrackRecordService.getLocation().getLongitude());
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
            ToastHelper.showShort(R.string.route_planning_failure);
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
            if (mNaviRouteOverlay == null) {
                mNaviRouteOverlay = new NaviRouteOverlay(mAMap, naviPath, mFrom, mTo);
                if (ExpoApp.getApplication().getUserHandBitmap() == null) {
                    mNaviRouteOverlay.setCarMarkerIcon(BitmapUtils.circleBitmap(
                            BitmapFactory.decodeResource(getResources(), R.mipmap.ico_mine_def_photo), mPhotoSize, mPhotoSize));
                } else {
                    mNaviRouteOverlay.setCarMarkerIcon(BitmapUtils.circleBitmap(ExpoApp.getApplication().getUserHandBitmap(), mPhotoSize, mPhotoSize));
                }
                mNaviRouteOverlay.setRouteCustomTexture(R.mipmap.ico_route_item);
                mNaviRouteOverlay.setRouteWidth(40);
                mNaviRouteOverlay.setPassedRouteCustomTexture(R.mipmap.ico_route_item_passed);
                mNaviRouteOverlay.addToMap();
            } else {
                LatLng startLatLng = LocationManager.getInstance().getCurrentLocationLatLng();
                mFrom = new NaviLatLng(startLatLng.latitude, startLatLng.longitude);
                mNaviRouteOverlay.resetRouteLineData(naviPath, mFrom, mTo);
            }
            setViewContent(naviPath);
            hideLoadingView();
        }
    };

    private void setViewContent(AMapNaviPath naviPath) {
        mInfoView.setVisibility(View.VISIBLE);
        mTvDestination.setText(LanguageUtil.chooseTest(mClickVenue.getCaption(), mClickVenue.getEnCaption()));       // 目的地
        mTvDistance.setText("实际距离：" + getFormatDistance(naviPath.getAllLength()));    // 距离
        mTvDuration.setText("预计全程时长：" + getFormatDuration(naviPath.getAllTime()));    // 时长
    }

    private DecimalFormat mDecimalFormat;

    private String getFormatDistance(int distance) {
        String units = "m";
        if (distance >= 1000) {
            units = "km";
            distance = distance / 1000;
        }
        if (mDecimalFormat == null) {
            mDecimalFormat = new DecimalFormat("#######.00");
        }
        return mDecimalFormat.format(distance) + units;
    }

    private String getFormatDuration(int duration) {
        String units = "秒";
        int min = 0;
        int h = 0;
        if (duration >= 60) {
            min = duration / 60;
            units = "分钟";
        }
        if (min >= 60) {
            h = (int) Math.floor(min / 60);
            return h + "小时" + min % 60 + units;
        } else {
            return min + units;
        }
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context, List<Venue> venues, Venue venue, Bitmap bitmap) {
        Intent in = new Intent(context, PlayMapActivity.class);
        in.putParcelableArrayListExtra(Constants.EXTRAS.EXTRA_AT_VENUE_LIST, (ArrayList<Venue>) venues);
        in.putExtra(Constants.EXTRAS.EXTRA_JSON_DATA, Http.getGsonInstance().toJson(venue));
        in.putExtra(Constants.EXTRAS.EXTRA_BITMAP_BYTE_ARRAY, bitmapToByteArray(bitmap));
        context.startActivity(in);
    }

    public static void startActivity(Context context, String typeName) {
        Intent in = new Intent(context, PlayMapActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_VENUE_TYPE_NAME, typeName);
        context.startActivity(in);
    }


    public static void startActivity(Context context, Venue venue, int onlyPreview) {
        Intent in = new Intent(context, PlayMapActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_JSON_DATA, Http.getGsonInstance().toJson(venue));
        in.putExtra(Constants.EXTRAS.EXTRA_ONLY_PREVIEW, onlyPreview);
        context.startActivity(in);
    }

    @Override
    public void onBackPressed() {
        if (mAMapNavi != null) {
            mAMapNavi.stopNavi();
            mAMapNavi.destroy();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mAMapNavi != null) {
                mAMapNavi.stopNavi();
                mAMapNavi.destroy();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private Bitmap byteArrayToBitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
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

    @Override
    public void queryVenueRes(List<Venue> venues) {
        // 根据类型获取分类设施，首条为距离用户最近的设施
        if (null == venues || venues.size() == 0) {
            ToastHelper.showShort(R.string.no_service_agencies);
            return;
        }
        mClickVenue = venues.get(0);
        mVenueList = venues;
        initMapMarker();
        startNavi();
    }

    @Override
    public void updateMarkerPic(Bitmap bmp) {
        // 更新Marker点的中心图标
        mBitmap = bmp;
        for (Marker marker : mMarkers) {
            Venue as = (Venue) marker.getObject();
            marker.setIcon(mMapUtils.setMarkerIconDrawable(getContext(), mBitmap,
                    LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption())));
        }
    }
}
