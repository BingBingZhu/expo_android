package com.expo.module.map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.expo.R;
import com.expo.adapters.DownloadData;
import com.expo.adapters.ParkActualSceneAdapter;
import com.expo.adapters.ParkRouteAdapter;
import com.expo.adapters.TouristAdapter;
import com.expo.adapters.TouristTypeAdapter;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.base.utils.ViewUtils;
import com.expo.contract.ParkMapContract;
import com.expo.entity.Venue;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Park;
import com.expo.entity.RouteInfo;
import com.expo.entity.TouristType;
import com.expo.entity.VenuesType;
import com.expo.map.MapUtils;
import com.expo.module.download.DownloadManager;
import com.expo.module.webview.WebActivity;
import com.expo.module.webview.WebTemplateActivity;
import com.expo.network.Http;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.RecycleViewDivider;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 导游导览
 */
public class ParkMapActivity extends BaseActivity<ParkMapContract.Presenter> implements
        ParkMapContract.View, AMap.OnMapTouchListener, AMap.OnMarkerClickListener {

    @BindView(R.id.tab_layout)
    TabLayout mTabView;
    @BindView(R.id.map_view)
    TextureMapView mMapView;
    @BindView(R.id.park_map_menu)
    ImageView imgMenu;
    @BindView(R.id.select_tour_guide)
    SimpleDraweeView mTourGuideImg;

    private RecyclerView mTouristListView;

    private AMap mAMap;
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
    private List<Venue> mAtVenue;   // 当前tab下的场馆

    @Override
    protected int getContentView() {
        return R.layout.activity_park_map;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, R.string.the_guide_tour);
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mMapUtils = new MapUtils(mAMap);
        mMapUtils.settingMap(this, this);
        mAMap.setOnMyLocationChangeListener(mLocationChangeListener);
        mPresenter.loadParkMapData(getIntent().getLongExtra(Constants.EXTRAS.EXTRA_SPOT_ID, 0));
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

    @OnClick({R.id.latched_position, R.id.select_tour_guide, R.id.park_map_menu})
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
                    showLinePopup(mRouteInfos, 1);
                } else {
                    showPointPopup();
                }
                break;
        }
    }

    private void showLinePopup(List<RouteInfo> routeInfos, int type) {
        List<RouteInfo> atRouteInfos = new ArrayList<>();
        for (RouteInfo routeInfo : routeInfos){
            if (routeInfo.typeId.equals("1"))
                atRouteInfos.add(routeInfo);
        }
        imgMenu.setSelected(true);
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
        imgMenu.setSelected(true);
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
        btnSearch.setOnClickListener(v -> {
            // 搜索
            String searchStr = searchContent.getText().toString().trim();
            venues.clear();
            if (!TextUtils.isEmpty(searchStr)) {
                for (Venue as : mAtVenue) {
                    if (LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption()).indexOf(searchStr) >= 0) {
                        venues.add(as);
                    }
                }
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
            imgMenu.setSelected(false);
        });
        int windowPos[] = ViewUtils.calculatePopWindowPos(imgMenu, contentView);
        int yOff = getResources().getDimensionPixelSize(R.dimen.dms_20);
        windowPos[1] += yOff;
        popupWindow.showAtLocation(mTabView, Gravity.TOP, 0, windowPos[1]);
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
        Encyclopedias wiki = mPresenter.getEncy( venue.getWikiId());
        if (wiki != null)
            pic.setImageURI(Constants.URL.FILE_BASE_URL + wiki.getPicUrl());
        voiceRoot.setOnClickListener(v14 -> {
            String voiceUrl = LanguageUtil.chooseTest(venue.getVoiceUrl(),
                    venue.getVoiceUrlEn().isEmpty() ? venue.getVoiceUrl() : venue.getVoiceUrlEn());
            if (voiceUrl.isEmpty()){
                ToastHelper.showShort("该景点暂无音频");
                return;
            }
            play(voiceUrl);
        });
        asInfo.setOnClickListener(v12 -> {
            if (null == wiki) {
                ToastHelper.showShort("暂无详情信息");
                return;
            }
            WebTemplateActivity.startActivity(getContext(), wiki.getId());
            mActualSceneDialog.dismiss();
        });
        asLine.setOnClickListener(v13 ->{
            NavigationActivity.startActivity(getContext(), venue,
                    LanguageUtil.chooseTest( venue.getVoiceUrl(), venue.getVoiceUrlEn()));
        });
        dialogClose.setOnClickListener(v1 -> mActualSceneDialog.dismiss());
        mActualSceneDialog.setContentView(v);
        ViewUtils.settingDialog(getContext(), mActualSceneDialog);
        mActualSceneDialog.show();//显示对话框
    }

    private void play(String url){
        url = Constants.URL.FILE_BASE_URL + url;
        //1 初始化mediaplayer
        final MediaPlayer mediaPlayer = new MediaPlayer();
        //2 设置到播放的资源位置 path 可以是网络 路径 也可以是本地路径

        try {
            mediaPlayer.setDataSource(url);
            //3 准备播放
            mediaPlayer.prepareAsync();
            //3.1 设置一个准备完成的监听
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 4 开始播放
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设施信息弹出框
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
                ToastHelper.showShort("该景点暂无音频");
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
        for (Venue as : mAtVenue) {
            if (as.getLat() == 0)
                continue;
            LatLng latLng = new LatLng(as.getLat(), as.getLng());
            VenuesType vt = mVenuesTypes.get(mTabPosition);
            Marker marker = mAMap.addMarker(new MarkerOptions()
                    .icon(mMapUtils.setMarkerIconDrawable(getContext(), vt.getMarkBitmap(),
                            LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption())))
                    .anchor(0.5F, 0.90F).position(latLng));
            marker.setObject(as);
            markers.add(marker);
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
//        mTabId = mTabId <= 0 ? this.mVenuesTypes.get(0).getId() : mTabId;
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
