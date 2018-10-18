package com.expo.module.routes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.RoutesContract;

/*
 * 推荐路线列表
 */
public class RoutesActivity extends BaseActivity<RoutesContract.Presenter> implements RoutesContract.View {
    @Override
    protected int getContentView() {
        return R.layout.activity_routes;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动推荐路线列表页
     *
     * @param context
     */
    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, RoutesActivity.class );
        context.startActivity( in );
    }
}
