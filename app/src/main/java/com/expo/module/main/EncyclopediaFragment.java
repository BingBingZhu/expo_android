package com.expo.module.main;

import android.os.Bundle;

import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.contract.EncyclopediasContract;

/*
 * 百科页
 */
public class EncyclopediaFragment extends BaseFragment<EncyclopediasContract.Presenter> implements EncyclopediasContract.View {
    @Override
    public int getContentView() {
        return R.layout.fragment_encyclopedia;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }
}
