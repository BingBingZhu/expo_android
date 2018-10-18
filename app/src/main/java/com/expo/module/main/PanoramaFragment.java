package com.expo.module.main;

import android.os.Bundle;

import com.expo.R;
import com.expo.base.BaseFragment;

/*
 * 全景页
 */
public class PanoramaFragment extends BaseFragment {
    @Override
    public int getContentView() {
        return R.layout.fragment_panorama;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }
}
