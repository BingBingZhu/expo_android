package com.expo.module.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.R;
import com.expo.base.BaseActivity;

/*
 * 照片拍摄，视频拍摄页
 */
public class ShootActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_shoot;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    /**
     * 启动拍摄界面
     *
     * @param activity
     * @return 请求码RequestCode
     */
    public static int startActivityForResult(@NonNull Activity activity) {
        Intent in = new Intent( activity, ShootActivity.class );
        int requestCode = 110;
        activity.startActivityForResult( in, requestCode );
        return requestCode;
    }
}
