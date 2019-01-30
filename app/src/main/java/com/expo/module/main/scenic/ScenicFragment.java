package com.expo.module.main.scenic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.base.utils.StatusBarUtils;
import com.expo.contract.SceneContract;
import com.expo.contract.ScenicContract;
import com.expo.entity.VenuesType;
import com.expo.module.main.encyclopedia.EncyclopediaSearchActivity;
import com.expo.module.routes.CustomRouteActivity;


import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 景点页面
 */
public class ScenicFragment extends BaseFragment<ScenicContract.Presenter> implements ScenicContract.View {

    @BindView(R.id.top_view)
    View mTopView;
    @BindView(R.id.map)
    View mMapView;
    @BindView(R.id.scene)
    View mSceneView;
    @BindView(R.id.view_pager)
    ViewPager mPagerView;
    @BindView(R.id.scenic_search)
    View mSearch;
    @BindView(R.id.scenic_map)
    View mMap;

    private ScenicTabPagerAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.fragment_scenic;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mTopView.setPadding(0, StatusBarUtils.getStatusBarHeight(getContext()), 0, 0);
        List<VenuesType> list = mPresenter.getTabs();
        mAdapter = new ScenicTabPagerAdapter(getFragmentManager(), list);
        mPagerView.setAdapter(mAdapter);

        onClickMap(null);
    }

    @Override
    public boolean isNeedPaddingTop() {
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }


    @OnClick(R.id.map)
    public void onClickMap(View v) {
        mPagerView.setCurrentItem(0);
        mMapView.setSelected(true);
        mSceneView.setSelected(false);

        mSearch.setVisibility(View.GONE);
        mMap.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.scenic_search)
    public void onScenicSearch(View view) {
        EncyclopediaSearchActivity.startActivity(getContext());
    }

    @OnClick({R.id.scenic_map})
    public void onScenicMap(View view) {
        CustomRouteActivity.startActivity(getContext());
    }


    @OnClick(R.id.scene)
    public void onClickScene(View view) {
        mPagerView.setCurrentItem(1);
        mMapView.setSelected(false);
        mSceneView.setSelected(true);

        mSearch.setVisibility(View.VISIBLE);
        mMap.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
