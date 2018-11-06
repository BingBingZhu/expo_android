package com.expo.module.main;

import android.os.Bundle;
import android.view.View;

import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.contract.HomeContract;
import com.expo.module.map.ParkMapActivity;

import butterknife.OnClick;

/*
 * 首页
 */
public class HomeFragment extends BaseFragment<HomeContract.Presenter> implements HomeContract.View {
    @Override
    public int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mPresenter.startHeartService(getContext());
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    public void freshHeartMessage() {

    }

    @Override
    public void onDestroy() {
        mPresenter.stopHeartService(getContext());
        super.onDestroy();
    }

    @OnClick(R.id.home_test_tv)
    public void onClick(View v) {
        ParkMapActivity.startActivity(getContext(), 12L, null);
    }
}
