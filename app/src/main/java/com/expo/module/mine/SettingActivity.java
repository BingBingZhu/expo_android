package com.expo.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.SettingContract;

/*
 * 设置页
 */
public class SettingActivity extends BaseActivity<SettingContract.Presenter> implements SettingContract.View {
    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, SettingActivity.class );
        context.startActivity( in );
    }
}
