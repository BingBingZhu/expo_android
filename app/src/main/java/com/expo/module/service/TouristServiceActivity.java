package com.expo.module.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.R;
import com.expo.base.BaseActivity;

/*
 * 游客服务页
 */
public class TouristServiceActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_tourist_service;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, TouristServiceActivity.class );
        context.startActivity( in );
    }
}
