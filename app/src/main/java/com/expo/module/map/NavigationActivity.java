package com.expo.module.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.NavigationContract;
import com.expo.utils.Constants;

/*
 * 世园会实景导航页
 */
public class NavigationActivity extends BaseActivity<NavigationContract.Presenter> implements NavigationContract.View {
    @Override
    protected int getContentView() {
        return R.layout.activity_navigation;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    /**
     * 启动NavigationActivity
     *
     * @param context
     */
    public static void startActivity(@NonNull Context context, @NonNull Long spotId) {
        Intent in = new Intent( context, ParkMapActivity.class );
        in.putExtra( Constants.EXTRAS.EXTRA_SPOT_ID, spotId );
        context.startActivity( in );
    }
}
