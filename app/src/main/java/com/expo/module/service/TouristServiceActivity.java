package com.expo.module.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.BaseAdapterItemClickListener;
import com.expo.module.service.adapter.TouristServiceAdapter;
import com.expo.utils.Constants;
import com.expo.widget.decorations.SpaceDecoration;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 游客服务页
 */
public class TouristServiceActivity extends BaseActivity {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    TouristServiceAdapter mAdapter;

    BaseAdapterItemClickListener mListener = new BaseAdapterItemClickListener() {
        @Override
        public void itemClick(View view, int position, Object o) {
            if (position == 0
                    || position == 1
                    || position == 2
                    || position == 4
                    || position == 5
                    ) {
                Intent intent = new Intent(TouristServiceActivity.this, SeekHelpActivity.class);
                intent.putExtra(Constants.EXTRAS.EXTRA_TITLE, getResources().getText(getResources().getIdentifier("item_tourist_service_text_" + position, "string", AppUtils.getAppPackageName())));
                intent.putExtra(Constants.EXTRAS.EXTRAS, position);
                startActivity(intent);
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_tourist_service;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

        setTitle(0, "游客服务");

        mAdapter = new TouristServiceAdapter(this);
        mAdapter.setListener(mListener);

        mRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        mRecycler.addItemDecoration(new SpaceDecoration((int) getResources().getDimension(R.dimen.dms_30)));
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent(context, TouristServiceActivity.class);
        context.startActivity(in);
    }

    @OnClick(R.id.tourist_services_phone)
    public void click(View view) {

    }

}
