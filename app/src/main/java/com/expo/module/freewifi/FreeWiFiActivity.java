package com.expo.module.freewifi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.FreeWiFiContract;

public class FreeWiFiActivity extends BaseActivity<FreeWiFiContract.Presenter> implements FreeWiFiContract.View {
    @Override
    protected int getContentView() {
        return R.layout.activity_free_wifi;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, FreeWiFiActivity.class );
        context.startActivity( in );
    }
}
