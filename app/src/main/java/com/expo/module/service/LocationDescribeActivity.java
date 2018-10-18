package com.expo.module.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.utils.Constants;

/*
 * 位置更改描述页
 */
public class LocationDescribeActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_loaction_describe;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    /**
     * 启动位置描述页面
     *
     * @param activity
     * @param lat      当前定位位置的纬度
     * @param lng      当前定位位置的经度
     * @return RequestCode 请求码
     */
    public static int startActivityForResult(@NonNull Activity activity, double lat, double lng) {
        Intent in = new Intent( activity, LocationDescribeActivity.class );
        in.putExtra( Constants.EXTRAS.EXTRA_LONGITUDE, lng );
        in.putExtra( Constants.EXTRAS.EXTRA_LATITUDE, lat );
        int requestCode = 120;
        activity.startActivityForResult( in, requestCode );
        return requestCode;
    }

    /*
     * 设置返回数据结果
     */
    private void setResult(Double lat, Double lng, String content) {
        if (lat == null || lng == null) {
            setResult( RESULT_CANCELED );
        } else {
            Intent result = new Intent();
            result.putExtra( Constants.EXTRAS.EXTRA_LATITUDE, lat );
            result.putExtra( Constants.EXTRAS.EXTRA_LONGITUDE, lng );
            if (!TextUtils.isEmpty( content )) {
                result.putExtra( Constants.EXTRAS.EXTRAS, content );
            }
            setResult( RESULT_OK, result );
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult( null, null, null );
    }
}
