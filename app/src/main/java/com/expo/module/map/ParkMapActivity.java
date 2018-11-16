package com.expo.module.map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
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
import com.expo.R;
import com.expo.adapters.DownloadData;
import com.expo.adapters.ParkActualSceneAdapter;
import com.expo.adapters.TouristAdapter;
import com.expo.adapters.TouristTypeAdapter;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.base.utils.ViewUtils;
import com.expo.contract.ParkMapContract;
import com.expo.entity.ActualScene;
import com.expo.entity.Park;
import com.expo.entity.TouristType;
import com.expo.entity.VenuesType;
import com.expo.map.MapUtils;
import com.expo.module.download.DownloadManager;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;
import com.expo.widget.RecycleViewDivider;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

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
    private List<ActualScene> mFacilities;
    private Long mTabId;
    private List<Marker> markers;
    private MapUtils mMapUtils;
    private LatLng mLatLng;
    private Dialog mTouristDialog;
    private Dialog mActualSceneDialog;
    private List<TouristType> mTouristTypes;
    private List<VenuesType> mVenuesTypes;
    private TouristAdapter mTouristAdapter;
    private int mTabPosition = 0;
    private List<ActualScene> mAtActualScene;   // 当前tab下的场馆

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
        mPresenter.loadParkMapData();
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
    public static void startActivity(@NonNull Context context, @Nullable Long tabId, @Nullable Long spotId) {
        Intent in = new Intent(context, ParkMapActivity.class);
        in.putExtra(Constants.EXTRAS.EXTRA_SPOT_ID, spotId);
        in.putExtra(Constants.EXTRAS.EXTRA_TAB_ID, tabId);
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
                showSearchPopup();
                break;
        }
    }

    /**
     * 显示搜索菜单
     */
    private void showSearchPopup() {
        imgMenu.setSelected(true);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_popup_park_menu, null);
        EditText searchContent = contentView.findViewById(R.id.popup_search_edit);
        TextView btnSearch = contentView.findViewById(R.id.popup_search);
        RecyclerView recyclerView = contentView.findViewById(R.id.popup_recycler_view);
        final List<ActualScene> actualScenes = new ArrayList<>();
        actualScenes.addAll(mAtActualScene);
        ParkActualSceneAdapter adapter = new ParkActualSceneAdapter(getContext(), actualScenes, mVenuesTypes.get(mTabPosition), mLatLng);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(v -> {
            // 单项点击事件
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            int position = holder.getAdapterPosition();
            ActualScene as = actualScenes.get(position);
            mMapUtils.mapGoto(as.getLat(), as.getLng());
            showActualSceneDialog(as);
        });
        adapter.setOnVoiceClickListener(v -> {
            // 音频点击事件
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
            int position = holder.getAdapterPosition();
            ToastHelper.showShort(actualScenes.get(position).getCaption() + "音频");
        });
        btnSearch.setOnClickListener(v -> {
            // 搜索
            String searchStr = searchContent.getText().toString().trim();
            actualScenes.clear();
            if (!TextUtils.isEmpty(searchStr)) {
                for (ActualScene as : mAtActualScene) {
                    if (LanguageUtil.chooseTest(as.getCaption(), as.getEnCaption()).indexOf(searchStr) >= 0) {
                        actualScenes.add(as);
                    }
                }
            } else {
                actualScenes.addAll(mAtActualScene);
            }
            adapter.notifyDataSetChanged();
        });
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
    public void loadTabRes(List<VenuesType> venuesTypes) {
        this.mVenuesTypes = venuesTypes;
        mTabId = getIntent().getLongExtra(Constants.EXTRAS.EXTRA_TAB_ID, 0);
        mTabId = mTabId <= 0 ? this.mVenuesTypes.get(0).getId() : mTabId;
        initTab(this.mVenuesTypes);
    }

    @Override
    public void loadFacilityRes(List<ActualScene> facilities) {
        this.mFacilities = facilities;
        mAtActualScene = new ArrayList<>();
        markers = new ArrayList<>();
        addActualSceneMarker(mTabId, mFacilities);
        if (getIntent().getLongExtra(Constants.EXTRAS.EXTRA_SPOT_ID, 0) != 0) {
            // 弹出marker提示框
//            showActualSceneDialog();
        }
    }

    /**
     * 设施信息弹出框
     */
    private void showActualSceneDialog(ActualScene actualScene) {
        mActualSceneDialog = new Dialog(getContext(), R.style.TopActionSheetDialogStyle);
        if (mActualSceneDialog.isShowing())
            return;
        View v = LayoutInflater.from(getContext()).inflate(R.layout.layout_as_dialog, null);
        TextView tvTest = v.findViewById(R.id.test_tv);
        tvTest.setText(actualScene.getCaption());
        mActualSceneDialog.setContentView(v);
        ViewUtils.settingDialog(getContext(), mActualSceneDialog);
        mActualSceneDialog.show();//显示对话框
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
            ActualScene as = (ActualScene) marker.getObject();
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

    private void addActualSceneMarker(Long tabId, List<ActualScene> facilities) {
        mAtActualScene.clear();
        for (ActualScene as : facilities) {
            if (as.getType() == tabId) {
                mAtActualScene.add(as);
            }
        }
        if (!markers.isEmpty()) {
            for (Marker marker : markers)
                marker.remove();
            markers.clear();
        }
        for (ActualScene as : mAtActualScene) {
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
    }

    // 获得视图资源
    private View getView(int index) {
        View v = getLayoutInflater().inflate(R.layout.layout_tab_item, null);
        TextView tbaTv = v.findViewById(R.id.tab_tv);
        tbaTv.setText(LanguageUtil.chooseTest(mVenuesTypes.get(index).getTypeName(), mVenuesTypes.get(index).getTypeNameEn()));
        return v;
    }

    private void initTab(List<VenuesType> venuesTypes) {
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
                if (null != mFacilities) {
                    addActualSceneMarker(mTabId, mFacilities);
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
        mMapUtils.mapGoto(marker.getPosition());
        ActualScene actualScene = (ActualScene) marker.getObject();
        // 显示marker弹窗
        showActualSceneDialog(actualScene);
        return false;
    }

    private AMap.OnMyLocationChangeListener mLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
    };
}
