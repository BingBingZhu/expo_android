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
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.blankj.utilcode.util.AppUtils;
import com.expo.R;
import com.expo.adapters.DownloadData;
import com.expo.adapters.LBSMapAdapter;
import com.expo.adapters.ParkActualSceneAdapter;
import com.expo.adapters.ParkRouteAdapter;
import com.expo.adapters.TouristAdapter;
import com.expo.adapters.TouristTypeAdapter;
import com.expo.base.BaseFragment;
import com.expo.base.utils.PrefsHelper;
import com.expo.base.utils.ToastHelper;
import com.expo.base.utils.ViewUtils;
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
import com.expo.map.MapUtils;
import com.expo.map.NaviManager;
import com.expo.module.ar.ArActivity;
import com.expo.module.download.DownloadManager;
import com.expo.module.routes.RouteDetailActivity;
import com.expo.module.webview.WebTemplateActivity;
import com.expo.network.Http;
import com.expo.services.DownloadListenerService;
import com.expo.services.TrackRecordService;
import com.expo.upapp.UpdateAppManager;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.utils.LocalBroadcastUtil;
import com.expo.utils.media.MediaPlayUtil;
import com.expo.widget.CustomDefaultDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.musicplayerproxy.Player;
import com.musicplayerproxy.PreLoad;

import java.io.UnsupportedEncodingException;
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
@SuppressLint("ValidFragment")
public class ParkMapFragment extends BaseFragment<ParkMapFragmentContract.Presenter> implements
        ParkMapFragmentContract.View, AMap.OnMapTouchListener, AMap.OnMarkerClickListener {

    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";
    public static final String EXPO_PARK = "expo_park";

    @BindView(R.id.tab_layout)
    TabLayout mTabView;
    @BindView(R.id.map_view)
    TextureMapView mMapView;
    //    @BindView(R.id.park_map_menu)
//    ImageView mImgMenu;
    @BindView(R.id.select_tour_guide)
    SimpleDraweeView mTourGuideImg;
    @BindView(R.id.map_pattern_chanage)
    ImageView mImgChanagePattern;
    @BindView(R.id.ar_download_view)
    View mArDownloadView;
    @BindView(R.id.voice_play_root)
    View mVoicePlayView;
    @BindView(R.id.voice_play_img)
    ImageView mVoicePlayImage;
    @BindView(R.id.voice_play_text)
    TextView mVoicePlayText;

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
    private Dialog mVrDialog;
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
    private String mPlayUrl = "";
    private long mSpotId;
    private int mapOffsetX, mapOffsetY;

//    private ClusterOverlay mClusterOverlay;
//
//    ClusterClickListener mClusterClickListener = new ClusterClickListener() {
//        @Override
//        public void onClick(Marker marker, List<ClusterItem> clusterItems) {
//            if (clusterItems == null) {
//                if (marker.getObject() instanceof Venue) {
//                    marker.showInfoWindow();
//                }
////                    showVenueDialog((Venue) marker.getObject());
//            } else {
//                if (clusterItems.size() == 1) {
//                    marker.showInfoWindow();
//                    mAMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
////                    showVenueDialog(((RegionItem) clusterItems.get(0)).actualScene);
//                } else if (clusterItems.size() > 1) {
//                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                    for (ClusterItem clusterItem : clusterItems) {
//                        builder.include(clusterItem.getPosition());
//                    }
//                    LatLngBounds latLngBounds = builder.build();
//                    mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0)
//                    );
//                }
//            }
//        }
//    };
//
//    MarkerInfoInterface mMarkerInfoInterface = new MarkerInfoInterface() {
//        @Override
//        public Bitmap getMarkerBitmap(Venue v) {
//            return ParkMapFragment.this.getMarkerBitmap(v);
//        }
//    };

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
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mMapUtils = new MapUtils(mAMap);
        mMapUtils.settingMap(this, this);
        mPattern = PrefsHelper.getLong(Constants.Prefs.KEY_MAP_PATTERN, 1);
        mAMap.setOnMyLocationChangeListener(mLocationChangeListener);
        mAMap.addTileOverlay(mMapUtils.getTileOverlayOptions(getContext()));
        mAMap.setMaxZoomLevel(19);
        mAMap.setInfoWindowAdapter(new LBSMapAdapter(getContext(), mInfoWindowListener));
        computeMapOffset();
        setMapCenterPoint(1);
        mPresenter.loadParkMapData(mSpotId, mVenuesTypes);
        // 地理围栏
        mGeoFenceClient = new GeoFenceClient(getContext());
        mGeoFenceClient.setActivateAction(GEOFENCE_IN | GEOFENCE_OUT);
        mGeoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
        getContext().registerReceiver(mGeoFenceReceiver, filter);
        LocalBroadcastUtil.registerReceiver(getContext(), mGeoFenceReceiver, Constants.Action.ACTION_MAP_ON_OFF);
        // 设置音频播放
//        MediaPlayUtil.getInstence().initMediaPlayer(getContext());
//        MediaPlayUtil.getInstence().setOnVoicePlayListener(voicePlayLis);

//        mClusterOverlay = new ClusterOverlay(mAMap, null,
//                SizeUtils.dp2px(50),
//                getContext());
//        mClusterOverlay.setInfoWindowOffset(mapOffsetX, 0);
//        mClusterOverlay.setMarkerInfoInterface(mMarkerInfoInterface);
//        mClusterOverlay.setOnClusterClickListener(mClusterClickListener);
    }

    /**
     * 设置地图中心点
     *
     * @param xy
     */
    private void setMapCenterPoint(int xy) {
        if (xy == 0)
            mAMap.setPointToCenter(mMapView.getDisplay().getWidth() / 2, getResources().getDisplayMetrics().heightPixels / 2);
        else
            mAMap.setPointToCenter(getResources().getDisplayMetrics().widthPixels / 2 - mapOffsetX, getResources().getDisplayMetrics().heightPixels / 2 + mapOffsetY);
    }

    private void computeMapOffset() {
        View v = getActivity().getLayoutInflater().inflate(R.layout.layout_map_window_item, null);
        int spec = View.MeasureSpec.makeMeasureSpec(1 << 30 - 1, View.MeasureSpec.AT_MOST);
        v.measure(spec, spec);
        mapOffsetX = (int) (v.getMeasuredWidth() * 0.316);
        mapOffsetY = v.getMeasuredHeight() / 3;
    }

    @Override
    public boolean isNeedPaddingTop() {
        return false;
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @OnClick({R.id.latched_position, R.id.select_tour_guide, /*R.id.park_map_menu,*/
            R.id.map_pattern_chanage, R.id.ar_down_load, R.id.voice_play_close})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.latched_position:     // 位置锁定
                setMapCenterPoint(0);
                mMapUtils.setFollow(mLatLng);
                break;
            case R.id.select_tour_guide:      // 导游选择
                showTouristTypeDialog();
                break;
//            case R.id.park_map_menu:      // 菜单按钮
//                showPointPopup();
//                break;
            case R.id.map_pattern_chanage:  // 地图模切换
                mPattern++;
                mPattern = mPattern >= 3 ? 1 : mPattern;
                PrefsHelper.setLong(Constants.Prefs.KEY_MAP_PATTERN, mPattern);
                mAMap.setMapType((int) mPattern);
                break;
            case R.id.ar_down_load:     // AR app下载
                Http.getNetworkType().toLowerCase().contains("wifi");
                String url = "http://p.gdown.baidu.com/1894c00d4eb74dae604ec7c747c890e68db0b29f2b380ca1c5e50b8becffc183b1bcb602b9dc0f72dfd0ee97ea8ff77b657758a79f006e8e586edd2fdbb7a467b6099baf4e7dc2cf7c099b33796ea3f798ada29019128e4c50608e8cfe328de413809f8c37646b6b92b858250c91ef3703c1a3de6af6c8132b938981d11afcc8c0d57f25f944f1318421f469426787db543214891727c2998dce6b131dedafccba57f73b964b2ce30b4a291850e8304a7a51bd4fff3459a71bbe6d8959176700";
                if (Http.getNetworkType().toLowerCase().contains("wifi")) {
                    UpdateAppManager.getInstance(getContext()).addArAppDownload(url);
                    new CustomDefaultDialog(getContext()).setContent("已添加至下载列表").setOnlyOK().show();
                    DownloadListenerService.startService(getContext());
                } else {
                    CustomDefaultDialog dialog = new CustomDefaultDialog(getContext());
                    dialog.setContent("当前下载消耗较多数据流量，是否继续？")
                            .setOnOKClickListener(view -> {
                                dialog.dismiss();
                                UpdateAppManager.getInstance().addArAppDownload(url);
                                new CustomDefaultDialog(getContext()).setContent("已添加至下载列表").setOnlyOK().show();
                                DownloadListenerService.startService(getContext());
                            }).show();
                }
                break;
            case R.id.voice_play_close:     // 关闭音频播放
                stopPlay();
                break;
        }
    }

    /**
     * 获得当前tab的中文名称
     *
     * @return
     */
    private boolean isTabByCnName(String name) {
        return mVenuesTypes.get(mTabPosition).getTypeName().equals(name);
    }

//    /**
//     * 显示搜索菜单
//     */
//    private void showPointPopup() {
////        mImgMenu.setSelected(true);
//        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_popup_park_menu, null);
//        View searchRoot = contentView.findViewById(R.id.popup_search_root);
//        if (isTabByCnName("\u666f\u70b9")) {
//            searchRoot.setVisibility(View.VISIBLE);
//        }
//        EditText searchContent = contentView.findViewById(R.id.popup_search_edit);
//        TextView btnSearch = contentView.findViewById(R.id.popup_search);
//        RecyclerView recyclerView = contentView.findViewById(R.id.popup_recycler_view);
//        final List<Venue> venues = new ArrayList<>();
//        venues.addAll(mAtVenue);
//        ParkActualSceneAdapter adapter = new ParkActualSceneAdapter(getContext(), venues, mVenuesTypes.get(mTabPosition), mLatLng);
//        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(v -> {
//            // 单项点击事件
//            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
//            int position = holder.getAdapterPosition();
//            Venue as = venues.get(position);
//            showVenueDialog(as);
//        });
//        searchContent.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String searchStr = s.toString().trim();
//                venues.clear();
//                if (searchStr.isEmpty()) {
//                    venues.addAll(mAtVenue);
//                } else {
//                    venues.addAll(mPresenter.selectVenueByCaption(searchStr));
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });
//        btnSearch.setOnClickListener(v -> {
//            // 搜索
//            String searchStr = searchContent.getText().toString().trim();
//            venues.clear();
//            if (!TextUtils.isEmpty(searchStr)) {
//                venues.addAll(mPresenter.selectVenueByCaption(searchStr));
//            } else {
//                venues.addAll(mAtVenue);
//            }
//            adapter.notifyDataSetChanged();
//        });
//        setPopup(contentView);
//    }
//
//    private void setPopup(View contentView) {
//        final PopupWindow popupWindow = new PopupWindow(contentView,
//                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
//        popupWindow.setAnimationStyle(R.style.TopPopupAnimation);
//        popupWindow.setTouchable(true);
//        popupWindow.setBackgroundDrawable(new ColorDrawable());
//        popupWindow.setOnDismissListener(() -> {
////            mImgMenu.setSelected(false);
//        });
//        popupWindow.showAsDropDown(mTabView);
//    }

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
            play(voiceUrl, LanguageUtil.chooseTest(wiki.getCaption(), wiki.getCaptionEn()));
        }

        @Override
        public void onStopPlay(Venue venue) {
            stopPlay();
        }
    };

//    =========================音频播放相关 start===========================================================================
    private Player.VoicePlayListener voicePlayLis = new Player.VoicePlayListener() {
        @Override
        public void playOver() {
            mVoicePlayText.setText("播放完毕！");
            stopPlay();
        }

        @Override
        public void playStart() {
            // 缓冲完毕，开始播放，设置播放图标
            mVoicePlayImage.setImageResource(R.mipmap.ico_audio_play);  // 图标白色，暂无图
        }

        @Override
        public void playError() {
            // 播放错误，资源未找到
            mVoicePlayText.setText("播放错误");
            stopPlay();
        }
    };

    private Player player;
    private boolean isPlayOver;

    private void play(String url, String encyName) {
        if (mPlayUrl.equals(url) && !isPlayOver)
            return;
        if (player != null){
            player.pause();
            player.stop();
        }
        player = new Player();
        player.setOnVoicePlayListener(voicePlayLis);
        mVoicePlayText.setText("正在为你播放“"+ encyName +"”语音导游");
        mVoicePlayView.setVisibility(View.VISIBLE);
        url = Constants.URL.FILE_BASE_URL+url;
        mPlayUrl = url;
//        MediaPlayUtil.getInstence().startPlay(url);
        final String urlEn = urlEncode(url);
        player.playUrl(url);
        isPlayOver = false;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                PreLoad load = new PreLoad(urlEn);
//                load.download(300*1000);
//            }
//        }).start();

        mVoicePlayImage.setImageResource(R.mipmap.ico_audio_play);  //此处加载中图片 暂无图
    }

    /**
     * URL编码
     *
     * @param url
     * @return
     */
    public static String urlEncode(String url) {
        try {
            url = java.net.URLEncoder.encode(url, "UTF-8");
            url = url.replaceAll("%2F", "/");
            url = url.replaceAll("%3A", ":");
            url = url.replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    private void stopPlay() {
        isPlayOver = true;
        mVoicePlayView.setVisibility(View.GONE);
//        MediaPlayUtil.getInstence().stopMusic();
        player.pause();
        player.stop();
        player = null;
//        MediaPlayUtil.getInstence().initMediaPlayer(getContext());
    }
//    =========================音频播放相关  end===========================================================================

    /**
     * AR乐拍
     *
     * @param venue
     */
    private void showArDialog(Venue venue) {
        boolean isIn = mMapUtils.ptInPolygon(new LatLng(TrackRecordService.getLocation().getLatitude(),
                TrackRecordService.getLocation().getLongitude()), venue.getElectronicFenceList());
        if (null == mVrDialog)
            mVrDialog = new Dialog(getContext(), R.style.TopActionSheetDialogStyle);
        if (mVrDialog.isShowing())
            return;
        View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_vr_dialog, null);
        ImageView imgLock = v.findViewById(R.id.vr_dialog_lock_img);
        ImageView imgClose = v.findViewById(R.id.vr_dialog_close);
        TextView tv1 = v.findViewById(R.id.vr_dialog_tv_1);
        TextView tv2 = v.findViewById(R.id.vr_dialog_tv_2);
        TextView function = v.findViewById(R.id.vr_dialog_function);
        if (isIn) {
            imgLock.setImageResource(R.mipmap.ico_vr_no_lock);
            tv1.setText("您已成功解锁该区域");
            tv2.setText("快去体验AR照相的乐趣吧");
            function.setText("立即拍照");
        } else {
            imgLock.setImageResource(R.mipmap.ico_vr_lock);
            tv1.setText("您还没有解锁该区域");
            tv2.setText("需前往改体验区体验");
            function.setText("立即前往");
        }
        function.setOnClickListener(view -> {
            if (isIn) {
                ArActivity.startActivity(getContext(),"AR乐拍");
            } else {
                PlayMapActivity.startActivity(getContext(), mAtVenue, venue, mVenuesTypes.get(mTabPosition).getMarkBitmap());
            }
            mVrDialog.dismiss();
        });
        imgClose.setOnClickListener(v1 -> mVrDialog.dismiss());
        mVrDialog.setContentView(v);
        ViewUtils.settingDialog(getContext(), mVrDialog);
        mVrDialog.show();//显示对话框
    }

    @Override
    public void loadTouristTypeRes(List<TouristType> touristTypes) {
        this.mTouristTypes = touristTypes;
        for (TouristType touristType : mTouristTypes) {
            if (touristType.isUsed()) {
                if (null == touristType.getPicSmallUrl() || touristType.getPicSmallUrl().isEmpty())
                    mTourGuideImg.setImageResource(R.mipmap.ico_default_tour_small);
                else
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
            }else if (intent.getAction().equals(Constants.Action.ACTION_MAP_ON_OFF)){
                if (!PrefsHelper.getBoolean(Constants.Prefs.KEY_MAP_ON_OFF, false))
                    mImgChanagePattern.setVisibility(View.GONE);
                else
                    mImgChanagePattern.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (!PrefsHelper.getBoolean(Constants.Prefs.KEY_MAP_ON_OFF, false))
            mImgChanagePattern.setVisibility(View.GONE);
    }

    /**
     * 路线
     *
     * @param routeInfos
     */
    @Override
    public void loadRoute(List<RouteInfo> routeInfos) {
        this.mRouteInfos = routeInfos;
    }

//    @Override
//    public void loadCustomRoute(List<CustomRoute> customRoutes) {
//        mCustomRoutes = customRoutes;
//    }

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
            if (tourist.getDownState() == DownloadManager.DOWNLOAD_FINISH) {     // 使用
                for (TouristType touristType : mTouristTypes) {
                    if (touristType.getId() == mTouristTypes.get(position).getId()) {
                        touristType.setUsed(true);
                        if (null == touristType.getPicSmallUrl() || touristType.getPicSmallUrl().isEmpty())
                            mTourGuideImg.setImageResource(R.mipmap.ico_default_tour_small);
                        else
                            mTourGuideImg.setImageURI(Constants.URL.FILE_BASE_URL + touristType.getPicSmallUrl());
                    } else {
                        touristType.setUsed(false);
                    }
                }
                mPresenter.saveUsed(mTouristTypes);
            } else {     // 下载
                DownloadData info = downloadDataList.get(position);
                if (info.getStatus() == DownloadManager.DOWNLOAD_IDLE || info.getStatus() == DownloadManager.DOWNLOAD_STOPPED
                        || info.getStatus() == DownloadManager.DOWNLOAD_ERROR) {
                    if (!Http.getNetworkType().toLowerCase().contains("wifi")) {
                        CustomDefaultDialog dialog = new CustomDefaultDialog(getContext());
                        dialog.setContent("下载需要流量"+Math.round(tourist.getModelFileSize() / tourist.getModelFileSize())+"M，是否继续？")
                                .setOkText("下载")
                                .setCancelText("取消")
                                .setOnOKClickListener(view -> {
                                    //开始下载
                                    mPresenter.startDownloadTask(info);
                                    dialog.dismiss();
                                })
                                .show();
                    }
                } else if (info.getStatus() == DownloadManager.DOWNLOAD_WAITING || info.getStatus() == DownloadManager.DOWNLOAD_STARTED) {
                    //停止下载
                    mPresenter.stopDownloadTask(info);
                }
            }
            mTouristAdapter.notifyDataSetChanged();
        });
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
        clearMap();
        for (Venue as : mAtVenue) {
            if (as.getLat() == 0)
                continue;
            LatLng latLng = new LatLng(as.getLat(), as.getLng());
            VenuesType vt = mVenuesTypes.get(mTabPosition);

            Marker marker = mAMap.addMarker(new MarkerOptions().setInfoWindowOffset(mapOffsetX, 0)
                    .icon(mMapUtils.setMarkerIconDrawable(getContext(), vt.getMarkBitmap(),
                            LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption())))
                    .anchor(0.5F, 0.90F).position(latLng));
            marker.setObject(as);
            markers.add(marker);
//            }
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
                mTabId = ParkMapFragment.this.mVenuesTypes.get(mTabPosition).getId();
                if (isTabByCnName("AR乐拍") && AppUtils.getAppInfo("com.casvd.expo_ar") == null)
                    mArDownloadView.setVisibility(View.VISIBLE);
                else
                    mArDownloadView.setVisibility(View.GONE);
                if (null != mFacilities) {
                    if (isTabByCnName("\u7535\u74f6\u8f66\u7ad9")) {
                        addActualSceneMarker(mTabId, mFacilities, true);
                        drawLineToMap("2", 0);
                    } else {
                        addActualSceneMarker(mTabId, mFacilities, true);
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
        if (isTabByCnName("AR乐拍")) {
            marker.setInfoWindowEnable(false);
            showArDialog((Venue) marker.getObject());
        } else {
            setMapCenterPoint(1);
            marker.showInfoWindow();
            mMapUtils.mapGotoAndZoom(marker.getOptions().getPosition(), 17);
        }
        return true;
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
        if (null != player){
            player.pause();
            player.stop();
        }
        super.onDestroy();
    }
}
