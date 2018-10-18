package com.expo.module.routes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.RouteDetailContract;
import com.expo.utils.Constants;

/*
 * 路线详情页
 */
public class RouteDetailActivity extends BaseActivity<RouteDetailContract.Presenter> implements RouteDetailContract.View {
    @Override
    protected int getContentView() {
        return R.layout.activity_route_detail;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动查看路线详情
     *
     * @param context
     * @param routeId
     */
    public static void startActivity(@NonNull Context context, @NonNull Long routeId) {
        Intent in = new Intent( context, RouteDetailActivity.class );
        in.putExtra( Constants.EXTRAS.EXTRA_ID, routeId );
        context.startActivity( in );
    }
}
