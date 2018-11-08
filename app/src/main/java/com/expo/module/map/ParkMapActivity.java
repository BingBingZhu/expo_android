package com.expo.module.map;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.expo.R;
import com.expo.adapters.DownloadData;
import com.expo.adapters.Tab;
import com.expo.adapters.TouristAdapter;
import com.expo.adapters.TouristTypeAdapter;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.ParkMapContract;
import com.expo.entity.ActualScene;
import com.expo.entity.TouristType;
import com.expo.map.MapUtils;
import com.expo.module.download.DownloadManager;
import com.expo.utils.Constants;
import com.expo.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 推荐路线列表
 */
public class ParkMapActivity extends BaseActivity<ParkMapContract.Presenter> implements
        ParkMapContract.View, AMap.OnMapTouchListener, AMap.OnMarkerClickListener {

    @BindView(R.id.tab_layout)
    TabLayout mTabView;
    @BindView(R.id.map_view)
    TextureMapView mMapView;
    @BindView(R.id.search_view)
    SearchView mSearchView;

    private RecyclerView mTouristListView;

    private AMap mAMap;
    private List<ActualScene> mFacilities;
    private Long mTabId;
    private List<Tab> mTabs;
    private List<Marker> markers;
    private MapUtils mMapUtils;
    private LatLng mLatLng;
    private Dialog mTouristDialog;
    private List<TouristType> mTouristTypes;
    private TouristAdapter mTouristAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_park_map;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        initSearchView();
        mAMap = mMapView.getMap();
        mMapUtils = new MapUtils(mAMap);
        mMapUtils.settingMap(this, this);
        mAMap.setOnMyLocationChangeListener( mLocationChangeListener );
        mPresenter.loadTab();
        mPresenter.loadTouristType();
    }

    private void initSearchView() {
        mSearchView.setOnCloseListener(() -> {
            ToastHelper.showShort("关闭");
            return false;
        });
        mSearchView.setOnSearchClickListener(v -> {
            ToastHelper.showShort("打开");
        });
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
    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, ParkMapActivity.class );
        context.startActivity( in );
    }


    /**
     * 启动ParkMapActivity
     *
     * @param context
     * @param selectedId 导航标签被选中类型的id
     * @param spotId     要查看的景点的id
     */
    public static void startActivity(@NonNull Context context, @Nullable Long selectedId, @Nullable Long spotId) {
        Intent in = new Intent( context, ParkMapActivity.class );
        if (selectedId != null) {
            in.putExtra( Constants.EXTRAS.EXTRA_TAB_ID, selectedId );
        }
        if (spotId != null) {
            in.putExtra( Constants.EXTRAS.EXTRA_SPOT_ID, spotId );
        }
        context.startActivity( in );
    }

    @OnClick({R.id.title_back, R.id.latched_position, R.id.select_tour_guide})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.latched_position:     // 位置锁定
                mMapUtils.setFollow(mLatLng);
                break;
            case R.id.select_tour_guide:      // 导游选择
                showTouristTypeDialog();
                break;
        }
    }

    @Override
    public void loadTabRes(List<Tab> tabs) {
        this.mTabs = tabs;
        mTabId = getIntent().getLongExtra(Constants.EXTRAS.EXTRA_TAB_ID, 0);
        mTabId = mTabId == 0 ? mTabs.get(0).getId() : mTabId;
        initTab(tabs);
        mPresenter.loadFacility();
    }

    @Override
    public void loadFacilityRes(List<ActualScene> facilities) {
        this.mFacilities = facilities;
        markers = new ArrayList<>();
        addGoeFenceAndMarker(mTabId, mFacilities);
//        EXTRA_SPOT_ID
        if (getIntent().getLongExtra(Constants.EXTRAS.EXTRA_SPOT_ID, 0) != 0){
            // 弹出marker提示框
        }
    }

    @Override
    public void loadTouristTypeRes(List<TouristType> touristTypes) {
        this.mTouristTypes = touristTypes;
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

    private TouristType getTouristType(DownloadData info){
        for (TouristType type : mTouristTypes){
            if (info.getId() == type.getId()){
                return type;
            }
        }
        return null;
    }

    private void showTouristTypeDialog(){
        mPresenter.setData(mTouristTypes);
        List<DownloadData> downloadDataList = TouristTypeAdapter.convertToTabList(mTouristTypes);
        mTouristDialog = new Dialog( getContext(), R.style.TopActionSheetDialogStyle);
        if (mTouristDialog.isShowing())
            return;
        View v = LayoutInflater.from( getContext() ).inflate( R.layout.layout_dialog_tourist_type, null );
        ImageView imgClose = v.findViewById(R.id.dialog_tourist_close);
        mTouristListView = v.findViewById(R.id.dialog_tourist_list);
        imgClose.setOnClickListener(v1 -> mTouristDialog.dismiss());
        mTouristAdapter = new TouristAdapter(getContext(), mTouristTypes);
        mTouristAdapter.setUseOnClickListener(v12 -> {
            RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v12.getTag();
            int position = holder.getAdapterPosition();
            for (TouristType touristType : mTouristTypes){
                if (touristType.getId() == mTouristTypes.get(position).getId()){
                    touristType.setUsed(true);
                }else{
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
                mPresenter.startDownloadTask( info );
            } else if (info.getStatus() == DownloadManager.DOWNLOAD_WAITING || info.getStatus() == DownloadManager.DOWNLOAD_STARTED) {
                //停止下载
                mPresenter.stopDownloadTask( info );
            }
        });
        mTouristListView.addItemDecoration(new RecycleViewDivider(
                getContext(), LinearLayoutManager.VERTICAL, 2, getResources().getColor(R.color.color_local_stork)));
        mTouristListView.setAdapter(mTouristAdapter);
        mTouristDialog.setContentView( v );
        Window dialogWindow = mTouristDialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity( Gravity.CENTER );
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = (int)(display.getHeight() * 0.65);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = mTouristDialog.getWindow();
            window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );
            window.getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_FULLSCREEN );
        }
        dialogWindow.setAttributes( lp );
        mTouristDialog.show();//显示对话框
    }


    private void addGoeFenceAndMarker(Long tabId, List<ActualScene> facilities) {
        List<ActualScene> atActualScene = new ArrayList<>();
        for (ActualScene as : facilities){
            if (as.getType().equals(String.valueOf(tabId))){
                atActualScene.add(as);
            }
        }
        if (!markers.isEmpty()) {
            for (Marker marker : markers)
                marker.remove();
            markers.clear();
        }
        for (ActualScene scene : atActualScene) {
            if (scene.getLat() == 0)
                continue;
            LatLng latLng = new LatLng( scene.getLat(), scene.getLng() );
            Marker marker = mAMap.addMarker( new MarkerOptions()
                    .position( latLng ) );/*.title( spot.getCaption() ).snippet( spot.getRemark() )*/
//                    .anchor( 0.5F, 0.65F )
//                    .icon( BitmapDescriptorFactory.fromResource( R.mipmap.ico_home_marker ) ) );
            marker.setObject( scene );
            markers.add( marker );
        }
    }

    private void initTab(List<Tab> tabs){
        mTabView.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tab的下划线颜色,默认是粉红色
        mTabView.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        for (Tab tab : tabs){
            TabLayout.Tab tab1 = mTabView.newTab().setText(tab.getTab());
            mTabView.addTab(tab1);
            if (tab.getId() == mTabId){
                tab1.select();
            }
        }
        mTabView.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mTabId = mTabs.get(mTabView.getSelectedTabPosition()).getId();
                if (null != mFacilities){
                    addGoeFenceAndMarker(mTabId, mFacilities);
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

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private AMap.OnMyLocationChangeListener mLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
    };
}
