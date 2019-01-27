package com.expo.module.main.scenic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.base.utils.StatusBarUtils;


import butterknife.BindView;
import butterknife.OnClick;

/*
 * 景点页面
 */
public class ScenicFragment extends BaseFragment {

    @BindView(R.id.top_view)
    View mTopView;
    @BindView(R.id.map)
    View mMapView;
    @BindView(R.id.scene)
    View mSceneView;
    @BindView(R.id.view_pager)
    ViewPager mPagerView;

    private ScenicTabPagerAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.fragment_scenic;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mTopView.setPadding(0, StatusBarUtils.getStatusBarHeight(getContext()), 0, 0);
        mAdapter = new ScenicTabPagerAdapter(getFragmentManager());
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
        return false;
    }


    @OnClick(R.id.map)
    public void onClickMap(View v) {
        mPagerView.setCurrentItem(0);
        mMapView.setSelected(true);
        mSceneView.setSelected(false);
    }

    @OnClick(R.id.scene)
    public void onClickScene(View view) {
        mPagerView.setCurrentItem(1);
        mMapView.setSelected(false);
        mSceneView.setSelected(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
