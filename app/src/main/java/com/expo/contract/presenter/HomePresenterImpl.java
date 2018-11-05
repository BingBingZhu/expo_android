package com.expo.contract.presenter;

import android.content.Context;

import com.expo.contract.HomeContract;
import com.expo.module.heart.HeartBeatService;

public class HomePresenterImpl extends HomeContract.Presenter {
    public HomePresenterImpl(HomeContract.View view) {
        super(view);
    }

    @Override
    public void startHeartService(Context context) {
        HeartBeatService.startService(context);
    }

    @Override
    public void stopHeartService(Context context) {
        HeartBeatService.stopService(context);
    }
}
