package com.expo.module.main;

import android.os.Bundle;

import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.contract.HomeContract;

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
}
