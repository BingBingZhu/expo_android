package com.expo.module.main;

import android.os.Bundle;

import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.contract.MineContract;

/*
 * 我的页
 */
public class MineFragment extends BaseFragment<MineContract.Presenter> implements MineContract.View {
    @Override
    public int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }
}
