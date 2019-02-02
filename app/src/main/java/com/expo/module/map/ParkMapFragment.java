package com.expo.module.map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import com.expo.adapters.LBSMapAdapter;
import com.expo.base.utils.LogUtils;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.*;
import com.blankj.utilcode.util.SizeUtils;
import com.expo.R;
import com.expo.adapters.*;
import com.expo.base.BaseFragment;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.ToastHelper;
import com.expo.base.utils.ViewUtils;
import com.expo.contract.ParkMapContract;
import com.expo.contract.ParkMapFragmentContract;
import com.expo.entity.CustomRoute;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Park;
import com.expo.entity.RouteInfo;
import com.expo.entity.TouristType;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;
import com.expo.map.ClusterClickListener;
import com.expo.map.ClusterItem;
import com.expo.map.ClusterOverlay;
import com.expo.map.MapUtils;
import com.expo.map.NaviManager;
import com.expo.map.RegionItem;
import com.expo.module.download.DownloadManager;
import com.expo.module.routes.RouteDetailActivity;
import com.expo.module.webview.WebTemplateActivity;
import com.expo.network.Http;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.utils.media.MediaPlayUtil;
import com.expo.widget.decorations.RecycleViewDivider;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.amap.api.fence.GeoFenceClient.GEOFENCE_IN;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_OUT;

/*
 * 导游导览
 */
@SuppressLint("ValidFragment")
public class ParkMapFragment extends BaseFragment<ParkMapFragmentContract.Presenter> implements
        ParkMapFragmentContract.View, AMap.OnMapTouchListener, AMap.OnMarkerClickListener {

    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";
    public static final String EXPO_PARK = "expo_park";

    @BindView(R.id.tab_layout)
    TabLayout mTabView;
    @BindView(R.id.map_view)
    TextureMapView mMapView;
    @BindView(R.id.park_map_menu)
    ImageView mImgMenu;
    @BindView(R.id.select_tour_guide)
    SimpleDraweeView mTourGuideImg;
    @BindView(R.id.map_pattern_chanage)
    ImageView mImgChanagePattern;

    private RecyclerView mTouristListView;

    private AMap mAMap;
    private GeoFenceClient mGeoFenceClient;
    private List<Venue> mFacilities;
    private Long mTabId;
    private List<Marker> markers;
    private List<Polyline> polylines;
    private MapUtils mMapUtils;
    private LatLng mLatLng;
    private Dialog mTouristDialog;
    private Dialog mActualSceneDialog;
    private Dialog mRouteInfoDialog;
    private List<TouristType> mTouristTypes;
    private List<VenuesType> mVenuesTypes;
    private List<RouteInfo> mRouteInfos;
    private List<CustomRoute> mCustomRoutes;
    private TouristAdapter mTouristAdapter;
    private int mTabPosition = 0;
    private int mOldTabPosition = 0;
    private List<Venue> mAtVenue;   // 当前tab下的场馆
    private boolean mIsInPark;
    private long mPattern = 1;
    private int playState;     // 音频播放状态
    private final static int TYPE_ROUTE = 1;    // 音频播放类型
    private final static int TYPE_VENUE = 2;    // 音频播放类型
    private int lastType;
    private long lastId;
    private String mPlayUrl;
    private long mSpotId;

    private ClusterOverlay mClusterOverlay;

    ClusterClickListener mClusterClickListener = new ClusterClickListener() {
        @Override
        public void onClick(Marker marker, List<ClusterItem> clusterItems) {
            marker.getOptions().setInfoWindowOffset(250, 0);
            if (clusterItems == null) {
                if (marker.getObject() instanceof Venue) {
                    marker.setInfoWindowEnable(true);
                    marker.showInfoWindow();
                }
//                    showVenueDialog((Venue) marker.getObject());
            } else {
                if (clusterItems.size() == 1) {
                    marker.setInfoWindowEnable(true);
                    marker.showInfoWindow();
//                    showVenueDialog(((RegionItem) clusterItems.get(0)).actualScene);
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
            return ParkMapFragment.this.getMarkerBitmap(v);
        }
    };

    public ParkMapFragment(List<VenuesType> list) {
        mSpotId = 0L;
        mVenuesTypes = list;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_park_map;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        if (!PrefsHelper.getBoolean(Constants.Prefs.KEY_MAP_ON_OFF, false))
            mImgChanagePattern.setVisibility(View.GONE);
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mMapUtils = new MapUtils(mAMap);
        mMapUtils.settingMap(this, this);
        mPattern = PrefsHelper.getLong(Constants.Prefs.KEY_MAP_PATTERN, 1);
        mAMap.setOnMyLocationChangeListener(mLocationChangeListener);
        mAMap.addTileOverlay(mMapUtils.getTileOverlayOptions(getContext()));
        mAMap.setMaxZoomLevel(19);
        mAMap.setInfoWindowAdapter(new LBSMapAdapter(getContext(), mInfoWindowListener));
        mPresenter.loadParkMapData(mSpotId, mVenuesTypes);
        // 地理围栏
        mGeoFenceClient = new GeoFenceClient(getContext());
        mGeoFenceClient.setActivateAction(GEOFENCE_IN | GEOFENCE_OUT);
        mGeoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
        getContext().registerReceiver(mGeoFenceReceiver, filter);
        mClusterOverlay = new ClusterOverlay(mAMap, null,
                SizeUtils.dp2px(50),
                getContext());
        mClusterOverlay.setMarkerInfoInterface(mMarkerInfoInterface);
        mClusterOverlay.setOnClusterClickListener(mClusterClickListener);

    }

    @Override
    public boolean isNeedPaddingTop() {
        return false;
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @OnClick({R.id.latched_position, R.id.select_tour_guide, R.id.park_map_menu, R.id.map_pattern_chanage})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.latched_position:     // 位置锁定
                mMapUtils.setFollow(mLatLng);
                break;
            case R.id.select_tour_guide:      // 导游选择
                showTouristTypeDialog();
                break;
            case R.id.park_map_menu:      // 菜单按钮
                if (isTabByCnName("\u8def\u7ebf")) {
                    showLinePopup(mRouteInfos);
                } else {
                    showPointPopup();
                }
                break;
            case R.id.map_pattern_chanage:
                // 地图模切换
                mPattern++;
                mPattern = mPattern >= 3 ? 1 : mPattern;
                PrefsHelper.setLong(Constants.Prefs.KEY_MAP_PATTERN, mPattern);
                mAMap.setMapType((int) mPattern);
                break;
        }
    }

    private void showLinePopup(List<RouteInfo> routeInfos) {
        List<RouteInfo> atRouteInfos = new ArrayList<>();
        for (RouteInfo routeInfo : routeInfos) {
            if (routeInfo.typeId.equals("1"))
                atRouteInfos.add(routeInfo);
        }
        mImgMenu.setSelected(true);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_popup_park_menu, null);
        RecyclerView recyclerView = contentView.findViewById(R.id.popup_recycler_view);
        if (mCustomRoutes.size() > 1) {
            RouteInfo routeInfo = new RouteInfo();
            routeInfo.caption = getString(R.string.my_route);
            routeInfo.voiceUrlEn = "";
            routeInfo.voiceUrl = "";
            routeInfo.typeId = atRouteInfos.get(0).typeId;
            int duration = 0;
            for (CustomRoute cr : mCustomRoutes) {
                duration += cr.getDuration();
            }
            routeInfo.playTime = String.valueOf((duration / 60));
            atRouteInfos.add(0, routeInfo);
        }
        ParkRouteAdapter parkRouteAdapter = new ParkRouteAdapter(getContext(), atRouteInfos, mVenuesTypes.get(mTabPosition));
        recyclerView.setAdapter(parkRouteAdapter);
        parkRouteAdapter.setOnItemClickListener(v -> {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            int position = holder.getAdapterPosition();
            if (mCustomRoutes.size() > 1 && position == 0)
                drawCustomRoute();
            else
                showRouteInfoDialog(atRouteInfos.get(position));
        });
        setPopup(contentView);
    }

    /**
     * 获得当前tab的中文名称
     *
     * @return
     */
    private boolean isTabByCnName(String name) {
        return mVenuesTypes.get(mTabPosition).getTypeName().equals(name);
    }

    /**
     * 显示搜索菜单
     */
    private void showPointPopup() {
        mImgMenu.setSelected(true);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_popup_park_menu, null);
        View searchRoot = contentView.findViewById(R.id.popup_search_root);
        if (isTabByCnName("\u666f\u70b9")) {
            searchRoot.setVisibility(View.VISIBLE);
        }
        EditText searchContent = contentView.findViewById(R.id.popup_search_edit);
        TextView btnSearch = contentView.findViewById(R.id.popup_search);
        RecyclerView recyclerView = contentView.findViewById(R.id.popup_recycler_view);
        final List<Venue> venues = new ArrayList<>();
        venues.addAll(mAtVenue);
        ParkActualSceneAdapter adapter = new ParkActualSceneAdapter(getContext(), venues, mVenuesTypes.get(mTabPosition), mLatLng);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(v -> {
            // 单项点击事件
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            int position = holder.getAdapterPosition();
            Venue as = venues.get(position);
            showVenueDialog(as);
        });
        searchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchStr = s.toString().trim();
                venues.clear();
                if (searchStr.isEmpty()) {
                    venues.addAll(mAtVenue);
                } else {
                    venues.addAll(mPresenter.selectVenueByCaption(searchStr));
                }
                adapter.notifyDataSetChanged();
            }
        });
        btnSearch.setOnClickListener(v -> {
            // 搜索
            String searchStr = searchContent.getText().toString().trim();
            venues.clear();
            if (!TextUtils.isEmpty(searchStr)) {
                venues.addAll(mPresenter.selectVenueByCaption(searchStr));
            } else {
                venues.addAll(mAtVenue);
            }
            adapter.notifyDataSetChanged();
        });
        setPopup(contentView);
    }

    private void setPopup(View contentView) {
        final PopupWindow popupWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.TopPopupAnimation);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOnDismissListener(() -> {
            mImgMenu.setSelected(false);
        });
        popupWindow.showAsDropDown(mTabView);
    }

    @Override
    public void loadTabRes(List<VenuesType> venuesTypes, int tabPosition) {
        this.mVenuesTypes = venuesTypes;
        initTab(this.mVenuesTypes, tabPosition);
    }

    @Override
    public void loadFacilityRes(List<Venue> facilities, Venue as) {
        this.mFacilities = facilities;
        mAtVenue = new ArrayList<>();
        markers = new ArrayList<>();
        polylines = new ArrayList<>();
        addActualSceneMarker(mTabId, mFacilities, true);
        // 弹出marker提示框
//        showVenueDialog(as);
    }

    private InfoWindowListener mInfoWindowListener = new InfoWindowListener() {
        @Override
        public void onToInfo(Venue venue) {
            Encyclopedias wiki = mPresenter.getEncy(venue.getWikiId());
            if (null == wiki) {
                ToastHelper.showShort(R.string.no_details_are_available);
                return;
            }
            WebTemplateActivity.startActivity(getContext(), wiki.getId());
        }

        @Override
        public void onNavigation(Venue venue) {
            if (mIsInPark) {
                boolean isInVenue = mPresenter.checkInVenue(mLatLng, venue);
                if (!isInVenue) {
                    boolean haveSelected = false;
                    for (TouristType type : mTouristTypes) {
                        if (type.isUsed()) {
                            haveSelected = true;
                        }
                    }
                    if (haveSelected) {
//                        NavigationActivity.startActivity(getContext(), venue);
                        PlayMapActivity.startActivity(getContext(), mAtVenue, venue, mVenuesTypes.get(mTabPosition).getMarkBitmap());
                    } else {
                        ToastHelper.showLong(R.string.please_select_tourist);
                        showTouristTypeDialog();
                    }
                } else {
                    ToastHelper.showLong(String.format(getString(R.string.no_navigation_required),
                            LanguageUtil.chooseTest(venue.getCaption(), venue.getEnCaption())));
                }
            } else
                NaviManager.getInstance(getContext()).showSelectorNavi(venue);
        }

        @Override
        public void onPlayVoice(Venue venue) {
            Encyclopedias wiki = mPresenter.getEncy(venue.getWikiId());
            if (null == wiki) {
                ToastHelper.showShort(R.string.there_is_no_audio_at_this_scenic_spot);
                return;
            }
            String voiceUrl = LanguageUtil.chooseTest(wiki.getVoiceUrl(),
                    wiki.getVoiceUrlEn().isEmpty() ? wiki.getVoiceUrl() : wiki.getVoiceUrlEn());
            if (voiceUrl.isEmpty()) {
                ToastHelper.showShort(R.string.there_is_no_audio_at_this_scenic_spot);
                return;
            }
            play(voiceUrl, TYPE_VENUE, venue.getId(), null);
        }

        @Override
        public void onStopPlay(Venue venue) {
            stopPlay(TYPE_VENUE, venue.getId(), null);
        }

        @Override
        public void onSetPic(Venue venue, SimpleDraweeView simpleDraweeView) {
            if (isTabByCnName("\u7f8e\u98df")) {
                simpleDraweeView.setImageResource(R.mipmap.ico_food_def_img);
            } else if (isTabByCnName("\u536b\u751f\u95f4")) {
                simpleDraweeView.setImageResource(R.mipmap.ico_toilet_def_img);
            } else if (isTabByCnName("\u5bfc\u89c8\u8f66")) {
                simpleDraweeView.setImageResource(R.mipmap.ico_car_def_img);
            } else if (isTabByCnName("\u6cbb\u5b89\u4ead")) {
                simpleDraweeView.setImageResource(R.mipmap.ico_public_security_def_img);
            }
            Encyclopedias wiki = mPresenter.getEncy(venue.getWikiId());
            if (wiki != null) {
                simpleDraweeView.setImageURI(Constants.URL.FILE_BASE_URL + wiki.getPicUrl());
            }
        }
    };

    /**
     * 设施信息弹出框
     */
    private void showVenueDialog(Venue venue) {
        if (null == venue) {
            return;
        }
        mMapUtils.mapGoto(venue.getLat(), venue.getLng());
        boolean isInVenue = mPresenter.checkInVenue(mLatLng, venue);
        mActualSceneDialog = new Dialog(getContext(), R.style.TopActionSheetDialogStyle);
        if (mActualSceneDialog.isShowing())
            return;
        View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_map_window_item/*layout_as_dialog*/, null);
        TextView voiceRoot = v.findViewById(R.id.park_mark_dialog_voice_img);
        ImageView imgVoice = /*v.findViewById(R.id.park_mark_dialog_voice_img)*/null;   // 音频图片
//        TextView appointmentTime = v.findViewById(R.id.park_mark_dialog_appointment_time);  // 预约时间
        SimpleDraweeView pic = v.findViewById(R.id.park_mark_dialog_pic);
        TextView asName = v.findViewById(R.id.park_mark_dialog_name);
        TextView asHint = v.findViewById(R.id.park_mark_dialog_hint);   // 场馆人多提示
        TextView asInfo = v.findViewById(R.id.park_mark_dialog_info);
        ImageView asLine = v.findViewById(R.id.park_mark_dialog_line);
        ImageView dialogClose = v.findViewById(R.id.park_mark_dialog_close);
        stopPlay(TYPE_VENUE, venue.getId(), imgVoice);
        asName.setText(LanguageUtil.chooseTest(venue.getCaption(), venue.getEnCaption()));
        if (isTabByCnName("\u7f8e\u98df")) {
            pic.setImageResource(R.mipmap.ico_food_def_img);
        } else if (isTabByCnName("\u536b\u751f\u95f4")) {
            pic.setImageResource(R.mipmap.ico_toilet_def_img);
        } else if (isTabByCnName("\u5bfc\u89c8\u8f66")) {
            pic.setImageResource(R.mipmap.ico_car_def_img);
        } else if (isTabByCnName("\u6cbb\u5b89\u4ead")) {
            pic.setImageResource(R.mipmap.ico_public_security_def_img);
        }
        Encyclopedias wiki = mPresenter.getEncy(venue.getWikiId());
        if (wiki != null) {
            pic.setImageURI(Constants.URL.FILE_BASE_URL + wiki.getPicUrl());
        }
        voiceRoot.setOnClickListener(v14 -> {
            if (null == wiki) {
                ToastHelper.showShort(R.string.there_is_no_audio_at_this_scenic_spot);
                return;
            }
            String voiceUrl = LanguageUtil.chooseTest(wiki.getVoiceUrl(),
                    wiki.getVoiceUrlEn().isEmpty() ? wiki.getVoiceUrl() : wiki.getVoiceUrlEn());
            if (voiceUrl.isEmpty()) {
                ToastHelper.showShort(R.string.there_is_no_audio_at_this_scenic_spot);
                return;
            }
            play(voiceUrl, TYPE_VENUE, venue.getId(), imgVoice);
        });
        asInfo.setOnClickListener(v12 -> {
            if (null == wiki) {
                ToastHelper.showShort(R.string.no_details_are_available);
                return;
            }
            WebTemplateActivity.startActivity(getContext(), wiki.getId());
            mActualSceneDialog.dismiss();
        });
        asLine.setOnClickListener(v13 -> {
            mActualSceneDialog.dismiss();
            if (mIsInPark) {
                if (!isInVenue) {
                    boolean haveSelected = false;
                    for (TouristType type : mTouristTypes) {
                        if (type.isUsed()) {
                            haveSelected = true;
                        }
                    }
                    if (haveSelected) {
                        NavigationActivity.startActivity(getContext(), venue);
                    } else {
                        ToastHelper.showLong(R.string.please_select_tourist);
                        showTouristTypeDialog();
                    }
                } else {
                    ToastHelper.showLong(String.format(getString(R.string.no_navigation_required),
                            LanguageUtil.chooseTest(venue.getCaption(), venue.getEnCaption())));
                }
            } else
                NaviManager.getInstance(getContext()).showSelectorNavi(venue);
        });
        dialogClose.setOnClickListener(v1 -> mActualSceneDialog.dismiss());
        mActualSceneDialog.setContentView(v);
        ViewUtils.settingDialog(getContext(), mActualSceneDialog);
        mActualSceneDialog.show();//显示对话框
    }

    private void play(String url, int lastType, long lastId, ImageView imgView) {
        this.lastType = lastType;
        this.lastId = lastId;
        MediaPlayUtil.getInstence().startPlay(url, imgView);
    }

    private void stopPlay(int type, long id, ImageView imgView) {
        if (lastType == type && lastId == id) {
            imgView.setImageResource(R.mipmap.ico_audio_play);
            return;
        }
        MediaPlayUtil.getInstence().stopMusic();
        MediaPlayUtil.getInstence().initMediaPlayer(getContext());
    }

    /**
     * 路线信息弹出框
     */
    private void showRouteInfoDialog(RouteInfo routeInfo) {
        if (null == routeInfo) {
            return;
        }
        mRouteInfoDialog = new Dialog(getContext(), R.style.TopActionSheetDialogStyle);
        if (mRouteInfoDialog.isShowing())
            return;
        View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_as_dialog, null);
        View voiceRoot = v.findViewById(R.id.park_mark_dialog_voice_root);
        ImageView imgVoice = v.findViewById(R.id.park_mark_dialog_voice_img);   // 音频图片
        TextView appointmentTime = v.findViewById(R.id.park_mark_dialog_appointment_time);  // 预约时间
        SimpleDraweeView pic = v.findViewById(R.id.park_mark_dialog_pic);
        TextView asName = v.findViewById(R.id.park_mark_dialog_name);
        TextView asHint = v.findViewById(R.id.park_mark_dialog_hint);   // 场馆人多提示
        ImageView asInfo = v.findViewById(R.id.park_mark_dialog_info);
        ImageView asLine = v.findViewById(R.id.park_mark_dialog_line);
        ImageView dialogClose = v.findViewById(R.id.park_mark_dialog_close);
        stopPlay(TYPE_ROUTE, routeInfo.id, imgVoice);
        pic.setImageURI(Constants.URL.FILE_BASE_URL + routeInfo.picUrl);
        asName.setText(LanguageUtil.chooseTest(routeInfo.caption, routeInfo.captionen));
        voiceRoot.setOnClickListener(v14 -> {
            String voiceUrl = LanguageUtil.chooseTest(routeInfo.voiceUrl,
                    routeInfo.voiceUrlEn.isEmpty() ? routeInfo.voiceUrl : routeInfo.voiceUrlEn);
            if (voiceUrl.isEmpty()) {
                ToastHelper.showShort(R.string.there_is_no_audio_at_this_scenic_spot);
                return;
            }
            play(voiceUrl, TYPE_ROUTE, routeInfo.id, imgVoice);
        });
        asInfo.setOnClickListener(v12 -> {
            RouteDetailActivity.startActivity(getContext(), routeInfo.id, LanguageUtil.chooseTest(routeInfo.caption, routeInfo.captionen));
            mRouteInfoDialog.dismiss();
        });
        asLine.setOnClickListener(v13 -> {
            drawLineFacilityToMap(routeInfo);
            drawLine(routeInfo);
            mRouteInfoDialog.dismiss();
        });
        dialogClose.setOnClickListener(v1 -> mRouteInfoDialog.dismiss());
        mRouteInfoDialog.setContentView(v);
        ViewUtils.settingDialog(getContext(), mRouteInfoDialog);
        mRouteInfoDialog.show();//显示对话框
    }

    @Override
    public void loadTouristTypeRes(List<TouristType> touristTypes) {
        this.mTouristTypes = touristTypes;
        for (TouristType touristType : mTouristTypes) {
            if (touristType.isUsed()) {
                mTourGuideImg.setImageURI(Constants.URL.FILE_BASE_URL + touristType.getPicSmallUrl());
            }
        }
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
     * 路线
     *
     * @param routeInfos
     */
    @Override
    public void loadRoute(List<RouteInfo> routeInfos) {
        this.mRouteInfos = routeInfos;
    }

    @Override
    public void loadCustomRoute(List<CustomRoute> customRoutes) {
        mCustomRoutes = customRoutes;
    }

    private void drawCustomRoute() {
        clearMap();
        for (CustomRoute cr : mCustomRoutes) {
            polylines.add(mAMap.addPolyline(new PolylineOptions().addAll(cr.getPoints()).width(10).
                    color(getResources().getColor(R.color.orange_ff7342))));
        }
        for (Venue venue : mFacilities) {
            if (venue.isSelected()) {
                Marker marker = mAMap.addMarker(new MarkerOptions()
                        .setInfoWindowOffset(250, 0)
                        .icon(mMapUtils.setMarkerIconDrawable(getContext(), getMarkerBitmap(venue),
                                LanguageUtil.chooseTest(venue.getCaption(), venue.getEnCaption())))
                        .anchor(0.5F, 0.90F).position(venue.getLatLng()));
                marker.setObject(venue);
                markers.add(marker);
            }
        }
    }

    private Bitmap getMarkerBitmap(Venue v) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test_1);
        for (VenuesType vt : mVenuesTypes) {
            if (v.getType() == vt.getId()) {
                bitmap = vt.getMarkBitmap();
                break;
            }
        }
        return bitmap;
    }

    @Override
    public void updateItemProgress(DownloadData info) {
        TouristType type = getTouristType(info);
        int index = mTouristTypes.indexOf(type);
        mTouristTypes.set(index, type);
        mTouristAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateItemStatus(DownloadData info) {
        TouristType type = getTouristType(info);
        int index = mTouristTypes.indexOf(type);
        mTouristTypes.set(index, type);
        mTouristAdapter.notifyDataSetChanged();
    }

    private TouristType getTouristType(DownloadData info) {
        for (TouristType type : mTouristTypes) {
            if (info.getId() == type.getId()) {
                return type;
            }
        }
        return null;
    }

    private TouristType mTouristType;

    /**
     * 导游类型弹窗
     */
    private void showTouristTypeDialog() {
        mPresenter.setData(mTouristTypes);
        List<DownloadData> downloadDataList = TouristTypeAdapter.convertToTabList(mTouristTypes);
        mTouristDialog = new Dialog(getContext(), R.style.TopActionSheetDialogStyle);
        if (mTouristDialog.isShowing())
            return;
        View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_tourist_type, null);
        ImageView imgClose = v.findViewById(R.id.dialog_tourist_close);
        mTouristListView = v.findViewById(R.id.dialog_tourist_list);
        imgClose.setOnClickListener(v1 -> mTouristDialog.dismiss());
        mTouristAdapter = new TouristAdapter(getContext(), mTouristTypes);
        mTouristAdapter.setUseOnClickListener(v12 -> {      // 使用
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v12.getTag();
            int position = holder.getAdapterPosition();
            TouristType tourist = mTouristTypes.get(position);
            if (tourist.getDownState() == DownloadManager.DOWNLOAD_FINISH){     // 使用
                for (TouristType touristType : mTouristTypes) {
                    if (touristType.getId() == mTouristTypes.get(position).getId()) {
                        touristType.setUsed(true);
                        mTourGuideImg.setImageURI(Constants.URL.FILE_BASE_URL + touristType.getPicSmallUrl());
                    } else {
                        touristType.setUsed(false);
                    }
                }
                mPresenter.saveUsed(mTouristTypes);
            }else{     // 下载
                DownloadData info = downloadDataList.get(position);
                if (info.getStatus() == DownloadManager.DOWNLOAD_IDLE || info.getStatus() == DownloadManager.DOWNLOAD_STOPPED
                        || info.getStatus() == DownloadManager.DOWNLOAD_ERROR) {
                    //开始下载
                    mPresenter.startDownloadTask(info);
                } else if (info.getStatus() == DownloadManager.DOWNLOAD_WAITING || info.getStatus() == DownloadManager.DOWNLOAD_STARTED) {
                    //停止下载
                    mPresenter.stopDownloadTask(info);
                }
            }
            mTouristAdapter.notifyDataSetChanged();
        });
//        mTouristListView.addItemDecoration(new RecycleViewDivider(
//                getContext(), LinearLayoutManager.VERTICAL, 2, getResources().getColor(R.color.color_local_stork)));
        mTouristListView.setAdapter(mTouristAdapter);
        mTouristDialog.setContentView(v);
        ViewUtils.settingDialog(getContext(), mTouristDialog);
        mTouristDialog.show();//显示对话框
    }

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

            if (mTabPosition == 0) {
                RegionItem regionItem = new RegionItem(latLng,
                        LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption()));
                regionItem.venuesType = vt;
                regionItem.actualScene = as;
                mClusterOverlay.addClusterItem(regionItem);
            } else {
                Marker marker = mAMap.addMarker(new MarkerOptions()
                        .icon(mMapUtils.setMarkerIconDrawable(getContext(), vt.getMarkBitmap(),
                                LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption())))
                        .anchor(0.5F, 0.90F).position(latLng));
                marker.setObject(as);
                markers.add(marker);
            }
        }
        mMapUtils.setCameraZoom(markers);
    }

    // 获得视图资源
    private View getView(int index) {
        View v = getLayoutInflater().inflate(R.layout.layout_tab_item, null);
        TextView tbaTv = v.findViewById(R.id.tab_tv);
        tbaTv.setText(LanguageUtil.chooseTest(mVenuesTypes.get(index).getTypeName(), mVenuesTypes.get(index).getTypeNameEn()));
        return v;
    }

    private void initTab(List<VenuesType> venuesTypes, int tabPosition) {
        mTabId = venuesTypes.get(tabPosition).getId();
        mTabView.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tab的下划线颜色,默认是粉红色
        mTabView.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        for (int i = 0; i < venuesTypes.size(); i++) {
            TabLayout.Tab tab1 = mTabView.newTab().setCustomView(getView(i));
            mTabView.addTab(tab1);
            if (venuesTypes.get(i).getId() == mTabId) {
                tab1.select();
            }
        }
        mTabView.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mTabPosition = mTabView.getSelectedTabPosition();
                if (mTabPosition == mOldTabPosition) {
                    return;
                }
                mOldTabPosition = mTabPosition;
                clearMap();
                mClusterOverlay.clearClusterItem();
                mTabId = ParkMapFragment.this.mVenuesTypes.get(mTabPosition).getId();
                if (isTabByCnName("\u8def\u7ebf")) {
                    if (mCustomRoutes.size() > 1)
                        drawCustomRoute();
                    else
                        drawLineToMap("1", 0);
                } else {
                    if (null != mFacilities) {
                        if (isTabByCnName("\u5bfc\u89c8\u8f66")) {
                            addActualSceneMarker(mTabId, mFacilities, true);
                            drawLineToMap("2", 0);
                        } else {
                            addActualSceneMarker(mTabId, mFacilities, true);
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void drawLineToMap(String typeid, int position) {
        List<RouteInfo> atRouteInfos = new ArrayList<>();
        for (RouteInfo routeInfo : mRouteInfos) {
            if (routeInfo.typeId.equals(typeid)) {
                atRouteInfos.add(routeInfo);
            }
        }
        if (atRouteInfos.size() == 0) {
            return;
        }
        if (typeid.equals("1")) {
            drawLineFacilityToMap(atRouteInfos.get(position));
            drawLine(atRouteInfos.get(position));
        } else {
            for (RouteInfo routeInfo : atRouteInfos) {
                drawLine(routeInfo);
            }
        }
    }

    /**
     * 游玩路线
     *
     * @param atRouteInfo
     */
    private void drawLineFacilityToMap(RouteInfo atRouteInfo) {
        ArrayList<Integer> ids = Http.getGsonInstance().fromJson(atRouteInfo.idsList, new TypeToken<ArrayList<Integer>>() {
        }.getType());
        List<Venue> ass = mPresenter.getActualScenes(ids);
        addActualSceneMarker(mTabId, ass, false);
    }

    /**
     * 画线
     *
     * @param atRouteInfo
     */
    private void drawLine(RouteInfo atRouteInfo) {
        ArrayList<MyLatLng> myLatLngs = Http.getGsonInstance().fromJson(atRouteInfo.linesList, new TypeToken<ArrayList<MyLatLng>>() {
        }.getType());
        List<LatLng> latLngs = new ArrayList<>();
        for (MyLatLng mll : myLatLngs) {
            latLngs.add(mll.getLatLng());
        }
        polylines.add(mAMap.addPolyline(new PolylineOptions().addAll(latLngs).zIndex(10).width(15).
                color(Color.argb(255, getRandColor(), getRandColor(), getRandColor()))));
    }

    /**
     * 随机数生成（用于随机颜色）
     *
     * @return
     */
    public int getRandColor() {
        Random random = new Random();
        return random.nextInt(256);
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        mPresenter.registerDownloadListener();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        mPresenter.unregisterDownloadListener();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
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
        marker.showInfoWindow();
//        Venue venue = (Venue) marker.getObject();
//        // 显示marker弹窗
//        showVenueDialog(venue);
        return false;
    }

    private AMap.OnMyLocationChangeListener mLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
    };

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(mGeoFenceReceiver);
        if (mMapView != null)
            mMapView.onDestroy();
        super.onDestroy();
    }
}
