package com.expo.module.circum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.expo.R;
import com.expo.adapters.StationAdapter;
import com.expo.base.BaseActivity;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.CircumTrafficContract;
import com.expo.entity.Venue;
import com.expo.map.NaviManager;
import com.expo.widget.SimpleHolder;
import com.expo.widget.decorations.SpaceDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CircumTrafficActivity extends BaseActivity<CircumTrafficContract.Presenter> implements CircumTrafficContract.View {

    @BindView(R.id.circum_traffic_parking_lot)
    RecyclerView mRecyclerViewPark;
    @BindView(R.id.circum_traffic_bus_station)
    RecyclerView mRecyclerViewBus;

    private StationAdapter parkAdapter;
    private StationAdapter busAdapter;
    private Venue parkVenue;
    private Venue busVenue;

    @Override
    protected int getContentView() {
        return R.layout.activity_circum_list_traffic;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, "周边交通");
        mPresenter.loadTrafficData();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context) {
        Intent intent =  new Intent( context, CircumTrafficActivity.class );
        context.startActivity(intent);
    }

    @OnClick({R.id.circum_traffic_navi_parking_lot, R.id.circum_traffic_navi_bus_station})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.circum_traffic_navi_parking_lot:
                if (null != parkVenue)
                    NaviManager.getInstance(getContext()).showSelectorNavi(parkVenue);
                else
                    ToastHelper.showShort("抱歉，暂无停车场数据");
                break;
            case R.id.circum_traffic_navi_bus_station:
                if (null != busVenue)
                    NaviManager.getInstance(getContext()).showSelectorNavi(busVenue);
                else
                    ToastHelper.showShort("抱歉，暂无公交车站数据");
                break;
        }
    }

    @Override
    public void loadTrafficDataRes(List<Venue> venuePark, List<Venue> venueBus) {
        int hSpace = getResources().getDimensionPixelSize( R.dimen.dms_50 );
        int vSpace = getResources().getDimensionPixelSize( R.dimen.dms_24 );
        if (null != venuePark && venuePark.size() > 0) {
            parkVenue = venuePark.get(0);
            parkAdapter = new StationAdapter(venuePark);
            parkAdapter.addOnClickListener(v -> {
                int position = ((SimpleHolder) v.getTag()).getAdapterPosition();
                parkVenue = venuePark.get(position);
            });
            mRecyclerViewPark.addItemDecoration(new SpaceDecoration(hSpace, vSpace));
            mRecyclerViewPark.setAdapter(parkAdapter);
        }
        if (null != venueBus && venueBus.size() > 0) {
            busVenue = venueBus.get(0);
            busAdapter = new StationAdapter(venueBus);
            busAdapter.addOnClickListener(v -> {
                int position = ((SimpleHolder) v.getTag()).getAdapterPosition();
                busVenue = venueBus.get(position);
            });
            mRecyclerViewBus.addItemDecoration(new SpaceDecoration(hSpace, vSpace));
            mRecyclerViewBus.setAdapter(busAdapter);
        }
    }
}
