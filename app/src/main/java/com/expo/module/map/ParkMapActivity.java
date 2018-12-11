package com.expo.module.map;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.blankj.utilcode.util.SizeUtils;
import com.expo.R;
import com.expo.adapters.DownloadData;
import com.expo.adapters.ParkActualSceneAdapter;
import com.expo.adapters.ParkRouteAdapter;
import com.expo.adapters.TouristAdapter;
import com.expo.adapters.TouristTypeAdapter;
import com.expo.base.BaseActivity;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.ToastHelper;
import com.expo.base.utils.ViewUtils;
import com.expo.contract.ParkMapContract;
import com.expo.entity.CustomRoute;
import com.expo.entity.Venue;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Park;
import com.expo.entity.RouteInfo;
import com.expo.entity.TouristType;
import com.expo.entity.VenuesType;
import com.expo.map.ClusterClickListener;
import com.expo.map.ClusterItem;
import com.expo.map.ClusterOverlay;
import com.expo.map.ClusterRender;
import com.expo.map.MapUtils;
import com.expo.map.RegionItem;
import com.expo.map.NaviManager;
import com.expo.module.download.DownloadManager;
import com.expo.module.webview.WebActivity;
import com.expo.module.webview.WebTemplateActivity;
import com.expo.network.Http;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.utils.media.MediaPlayUtil;
import com.expo.widget.RecycleViewDivider;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amap.api.fence.GeoFenceClient.GEOFENCE_IN;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_OUT;

/*
 * 导游导览
 */
public class ParkMapActivity extends BaseActivity<ParkMapContract.Presenter> implements
        ParkMapContract.View, AMap.OnMapTouchListener, AMap.OnMarkerClickListener {

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
    private TouristAdapter mTouristAdapter;
    private int mTabPosition = 0;
    private int mOldTabPosition = 0;
    private List<Venue> mAtVenue;   // 当前tab下的场馆
    private boolean mIsInPark;
    private long mPattern = 1;

    private ClusterOverlay mClusterOverlay;
    ClusterRender mClusterRender = new ClusterRender() {
        @Override
        public Drawable getDrawAble(int clusterNum) {
            return null;
//            int radius = SizeUtils.dp2px(80);
//            if (clusterNum == 1) {
//                Drawable bitmapDrawable = new b
//                if (bitmapDrawable == null) {
//                    bitmapDrawable =
//                            getApplication().getResources().getDrawable(
//                                    R.mipmap.ico_park_map_marker_main_bg);
//                }
//
//                return bitmapDrawable;
//            } else {
//
//                Drawable bitmapDrawable = mBackDrawAbles.get(2);
//                if (bitmapDrawable == null) {
//                    bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
//                            Color.argb(159, 210, 154, 6)));
//                }
//
//                return bitmapDrawable;
//            }
        }
    };

    ClusterClickListener mClusterClickListener = new ClusterClickListener() {
        @Override
        public void onClick(Marker marker, List<ClusterItem> clusterItems) {
            if(clusterItems.size() == 1){
                showActualSceneDialog(((RegionItem)clusterItems.get(0)).actualScene);
            } else if(clusterItems.size() > 1){
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (ClusterItem clusterItem : clusterItems) {
                    builder.include(clusterItem.getPosition());
                }
                LatLngBounds latLngBounds = builder.build();
                mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0)
                );
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_park_map;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, R.string.home_func_item_navigation);
        if (!PrefsHelper.getBoolean(Constants.Prefs.KEY_MAP_ON_OFF, false))
            mImgChanagePattern.setVisibility(View.GONE);
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mMapUtils = new MapUtils(mAMap);
        mMapUtils.settingMap(this, this);
        mPattern = PrefsHelper.getLong(Constants.Prefs.KEY_MAP_PATTERN, 1);
        mAMap.setOnMyLocationChangeListener(mLocationChangeListener);
        mPresenter.loadParkMapData(getIntent().getLongExtra(Constants.EXTRAS.EXTRA_SPOT_ID, 0));
        // 地理围栏
        mGeoFenceClient = new GeoFenceClient( getContext() );
        mGeoFenceClient.setActivateAction( GEOFENCE_IN | GEOFENCE_OUT );
        mGeoFenceClient.createPendingIntent( GEOFENCE_BROADCAST_ACTION );
        IntentFilter filter = new IntentFilter( ConnectivityManager.CONNECTIVITY_ACTION );
        filter.addAction( GEOFENCE_BROADCAST_ACTION );
        registerReceiver( mGeoFenceReceiver , filter );
        mClusterOverlay = new ClusterOverlay(mAMap, null,
                SizeUtils.dp2px(50),
                this);
        mClusterOverlay.setClusterRenderer(mClusterRender);
        mClusterOverlay.setOnClusterClickListener(mClusterClickListener);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动ParkMapActivity
     *
     * @param context
     */
    public static void startActivity(@NonNull Context context, @Nullable Long spotId) {
        Intent in = new Intent(context, ParkMapActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_SPOT_ID, spotId);
        context.startActivity(in);
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
                if (isTabByCnName("路线")) {
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
        for (RouteInfo routeInfo : routeInfos){
            if (routeInfo.typeId.equals("1"))
                atRouteInfos.add(routeInfo);
        }
        mImgMenu.setSelected(true);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_popup_park_menu, null);
        RecyclerView recyclerView = contentView.findViewById(R.id.popup_recycler_view);
        ParkRouteAdapter parkRouteAdapter = new ParkRouteAdapter(getContext(), atRouteInfos, mVenuesTypes.get(mTabPosition));
        recyclerView.setAdapter(parkRouteAdapter);
        parkRouteAdapter.setOnItemClickListener(v -> {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            int position = holder.getAdapterPosition();
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
        if (isTabByCnName("景点")) {
            searchRoot.setVisibility(View.VISIBLE);
        }
        EditText searchContent = contentView.findViewById(R.id.popup_search_edit);
        TextView btnSearch = contentView.findViewById(R.id.popup_search);
        RecyclerView recyclerView = contentView.findViewById(R.id.popup_recycler_view);
        final List<Venue> venues = new ArrayList<>();
        venues.addAll( mAtVenue );
        ParkActualSceneAdapter adapter = new ParkActualSceneAdapter(getContext(), venues, mVenuesTypes.get(mTabPosition), mLatLng);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(v -> {
            // 单项点击事件
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            int position = holder.getAdapterPosition();
            Venue as = venues.get(position);
            showActualSceneDialog(as);
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
                if (searchStr.isEmpty()){
                    venues.addAll( mAtVenue );
                }else {
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
                venues.addAll( mAtVenue );
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
        showActualSceneDialog(as);
    }

    /**
     * 设施信息弹出框
     */
    private void showActualSceneDialog(Venue venue) {
        if (null == venue) {
            return;
        }
        mMapUtils.mapGoto( venue.getLat(), venue.getLng());
        mActualSceneDialog = new Dialog(getContext(), R.style.TopActionSheetDialogStyle);
        if (mActualSceneDialog.isShowing())
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
        asName.setText(LanguageUtil.chooseTest( venue.getCaption(), venue.getEnCaption()));
        if (isTabByCnName("美食")){
            pic.setImageResource(R.mipmap.ico_food_def_img);
        }else if (isTabByCnName("卫生间")){
            pic.setImageResource(R.mipmap.ico_toilet_def_img);
        } else if (isTabByCnName("导览车")){
            pic.setImageResource(R.mipmap.ico_car_def_img);
        } else if (isTabByCnName("治安亭")) {
            pic.setImageResource(R.mipmap.ico_public_security_def_img);
        }
        Encyclopedias wiki = mPresenter.getEncy( venue.getWikiId());
        if (wiki != null) {
            pic.setImageURI(Constants.URL.FILE_BASE_URL + wiki.getPicUrl());
        }
        voiceRoot.setOnClickListener(v14 -> {
            String voiceUrl = LanguageUtil.chooseTest(wiki.getVoiceUrl(),
                    wiki.getVoiceUrlEn().isEmpty() ? wiki.getVoiceUrl() : wiki.getVoiceUrlEn());
            if (voiceUrl.isEmpty()){
                ToastHelper.showShort(R.string.there_is_no_audio_at_this_scenic_spot);
                return;
            }
            play(voiceUrl);
        });
        asInfo.setOnClickListener(v12 -> {
            if (null == wiki) {
                ToastHelper.showShort(R.string.no_details_are_available);
                return;
            }
            WebTemplateActivity.startActivity(getContext(), wiki.getId());
            mActualSceneDialog.dismiss();
        });
        asLine.setOnClickListener(v13 ->{
            if (mIsInPark)
                NavigationActivity.startActivity(getContext(), venue, LanguageUtil.chooseTest( venue.getVoiceUrl(), venue.getVoiceUrlEn()));
            else
                NaviManager.getInstance( getContext() ).showSelectorNavi( venue );
        });
        dialogClose.setOnClickListener(v1 -> mActualSceneDialog.dismiss());
        mActualSceneDialog.setContentView(v);
        ViewUtils.settingDialog(getContext(), mActualSceneDialog);
        mActualSceneDialog.show();//显示对话框
    }

    private void play(String url){
//        url = "4b74742e9ff342e8a870cc6268b6be78.mp3";
        MediaPlayUtil.getInstence().stopMusic();
        MediaPlayUtil.getInstence().initMediaPlayer();
        MediaPlayUtil.getInstence().startPlay(url);
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
        pic.setImageURI( Constants.URL.FILE_BASE_URL + routeInfo.picUrl );
        asName.setText(LanguageUtil.chooseTest(routeInfo.caption, routeInfo.captionen));
        voiceRoot.setOnClickListener(v14 -> {
            String voiceUrl = LanguageUtil.chooseTest(routeInfo.voiceUrl,
                    routeInfo.voiceUrlEn.isEmpty() ? routeInfo.voiceUrl : routeInfo.voiceUrlEn);
            if (voiceUrl.isEmpty()){
                ToastHelper.showShort(R.string.there_is_no_audio_at_this_scenic_spot);
                return;
            }
            play(voiceUrl);
        });
        asInfo.setOnClickListener(v12 -> {
            WebActivity.startActivity(getContext(), routeInfo.linkH5Url, LanguageUtil.chooseTest(routeInfo.caption, routeInfo.captionen));
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
                if (status == GEOFENCE_IN  && customId.equals(EXPO_PARK) ){
                    mIsInPark = true;
                }else if (status == GEOFENCE_OUT  && customId.equals(EXPO_PARK) ) {
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
        for (CustomRoute cr : customRoutes) {
            mAMap.addPolyline(new PolylineOptions().
                    addAll(cr.getPoints()).setCustomTexture(BitmapDescriptorFactory.fromResource(R.mipmap.ico_route_item)));
        }
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
        mTouristAdapter.setUseOnClickListener(v12 -> {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v12.getTag();
            int position = holder.getAdapterPosition();
            for (TouristType touristType : mTouristTypes) {
                if (touristType.getId() == mTouristTypes.get(position).getId()) {
                    touristType.setUsed(true);
                    mTourGuideImg.setImageURI(Constants.URL.FILE_BASE_URL + touristType.getPicSmallUrl());
                } else {
                    touristType.setUsed(false);
                }
            }
            mPresenter.saveUsed(mTouristTypes);
            mTouristAdapter.notifyDataSetChanged();
        });
        mTouristAdapter.setDownloadOnClickListener(v13 -> {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v13.getTag();
            int position = holder.getAdapterPosition();
            DownloadData info = downloadDataList.get(position);
            if (info.getStatus() == DownloadManager.DOWNLOAD_IDLE || info.getStatus() == DownloadManager.DOWNLOAD_STOPPED
                    || info.getStatus() == DownloadManager.DOWNLOAD_ERROR) {
                //开始下载
                mPresenter.startDownloadTask(info);
            } else if (info.getStatus() == DownloadManager.DOWNLOAD_WAITING || info.getStatus() == DownloadManager.DOWNLOAD_STARTED) {
                //停止下载
                mPresenter.stopDownloadTask(info);
            }
        });
        mTouristListView.addItemDecoration(new RecycleViewDivider(
                getContext(), LinearLayoutManager.VERTICAL, 2, getResources().getColor(R.color.color_local_stork)));
        mTouristListView.setAdapter(mTouristAdapter);
        mTouristDialog.setContentView(v);
        ViewUtils.settingDialog(getContext(), mTouristDialog);
        mTouristDialog.show();//显示对话框
    }

    /**
     * 清除地图覆盖物
     */
    private void clearMap(){
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
        }else{
            mAtVenue.addAll(facilities);
        }
        clearMap();
        for (Venue as : mAtVenue) {
            if (as.getLat() == 0)
                continue;
            LatLng latLng = new LatLng(as.getLat(), as.getLng());
            VenuesType vt = mVenuesTypes.get(mTabPosition);
//            Marker marker = mAMap.addMarker(new MarkerOptions()
//                    .icon(mMapUtils.setMarkerIconDrawable(getContext(), vt.getMarkBitmap(),
//                            LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption())))
//                    .anchor(0.5F, 0.90F).position(latLng));
//            marker.setObject(as);
//            markers.add(marker);

            RegionItem regionItem = new RegionItem(latLng,
                    LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption()));
            regionItem.venuesType = vt;
            regionItem.actualScene = as;
            mClusterOverlay.addClusterItem(regionItem);
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
                if (mTabPosition == mOldTabPosition){
                    return;
                }
                mOldTabPosition = mTabPosition;
                clearMap();
                mTabId = ParkMapActivity.this.mVenuesTypes.get(mTabPosition).getId();
                if (isTabByCnName("路线")) {
                    drawLineToMap("1", 0);
                } else {
                    if (null != mFacilities) {
                        if (isTabByCnName("导览车")) {
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
        }else{
            for (RouteInfo routeInfo : atRouteInfos){
                drawLine(routeInfo);
            }
        }
    }

    /**
     * 游玩路线
     * @param atRouteInfo
     */
    private void drawLineFacilityToMap(RouteInfo atRouteInfo){
        ArrayList<Integer> ids = Http.getGsonInstance().fromJson(atRouteInfo.idsList, new TypeToken<ArrayList<Integer>>() {
        }.getType());
        List<Venue> ass = mPresenter.getActualScenes(ids);
        addActualSceneMarker(mTabId, ass, false);
    }

    /**
     * 画线
     * @param atRouteInfo
     */
    private void drawLine(RouteInfo atRouteInfo){
        ArrayList<MyLatLng> myLatLngs = Http.getGsonInstance().fromJson(atRouteInfo.linesList, new TypeToken<ArrayList<MyLatLng>>() {
        }.getType());
        List<LatLng> latLngs = new ArrayList<>();
        for (MyLatLng mll : myLatLngs) {
            latLngs.add(mll.getLatLng());
        }
        polylines.add(mAMap.addPolyline(new PolylineOptions().addAll(latLngs).width(10).
                color(Color.argb(255, getRandColor(), getRandColor(), getRandColor()))));
    }

    /**
     * 随机数生成（用于随机颜色）
     * @return
     */
    public int getRandColor() {
        Random random = new Random();
        return random.nextInt(256);
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        mPresenter.registerDownloadListener();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        mPresenter.unregisterDownloadListener();
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
        showActualSceneDialog( venue );
        return false;
    }

    private AMap.OnMyLocationChangeListener mLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
    };
}
