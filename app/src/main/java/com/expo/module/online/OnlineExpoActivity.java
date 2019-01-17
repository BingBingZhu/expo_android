package com.expo.module.online;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.OnlineHomeContract;
import com.expo.widget.decorations.SpaceDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OnlineExpoActivity extends BaseActivity<OnlineHomeContract.Presenter> implements OnlineHomeContract.View {

    @BindView(R.id.online_expo_recycler_culture)
    RecyclerView mRvCulture;
    @BindView(R.id.online_expo_recycler_guide)
    RecyclerView mRvGuide;

    CommonAdapter mAdapterCulture;
    CommonAdapter mAdapterGuide;

    List mCultureList;
    List mGuideList;

    @Override
    protected int getContentView() {
        return R.layout.activity_online_expo;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        initCultureView();
        initGuideView();
    }

    private void initCultureView() {
        mCultureList = new ArrayList();
        mAdapterCulture = new CommonAdapter(this, R.layout.item_online_culture, mGuideList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {

            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }
        };

        mRvCulture.setLayoutManager(new LinearLayoutManager(this));
        mRvCulture.addItemDecoration(new SpaceDecoration(getResources().getDimensionPixelSize(R.dimen.dms_58)), getResources().getDimensionPixelSize(R.dimen.dms_30));
        mRvCulture.setAdapter(mAdapterCulture);
    }

    private void initGuideView() {
        mGuideList = new ArrayList();
        mAdapterGuide = new CommonAdapter(this, R.layout.item_online_guide, mGuideList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {

            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }
        };

        mRvGuide.setLayoutManager(new GridLayoutManager(this, 2));
        mRvGuide.addItemDecoration(new SpaceDecoration(getResources().getDimensionPixelSize(R.dimen.dms_14)), getResources().getDimensionPixelSize(R.dimen.dms_30));
        mRvGuide.setAdapter(mAdapterGuide);
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, OnlineExpoActivity.class));
    }
}
