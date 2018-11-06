package com.expo.module.routes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.base.BaseActivity;
import com.expo.contract.RoutesContract;

public class RoutesActivity extends BaseActivity<RoutesContract.Presenter> {
    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return false;
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
