package com.expo.module.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.ParkMapContract;
import com.expo.utils.Constants;

/*
 * 世园会地图导览页
 */
public class ParkMapActivity extends BaseActivity<ParkMapContract.Presenter> implements ParkMapContract.View {
    @Override
    protected int getContentView() {
        return R.layout.activity_park_map;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动ParkMapActivity
     *
     * @param context
     */
    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, ParkMapActivity.class );
        context.startActivity( in );
    }
}
