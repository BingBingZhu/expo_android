package com.expo.module.circum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.utils.Constants;

public class CircunListActivity extends BaseActivity {

    private int mCircunType;   // 周边类型 1美食 2酒店 3购物 4交通 5景区

    @Override
    protected int getContentView() {
        mCircunType = getIntent().getIntExtra(Constants.EXTRAS.EXTRA_CIRCUN_TYPE, 0);
        switch (mCircunType){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return R.layout.activity_circun_list;
            default:
                return 0;
        }
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {


    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(Context context, int circunType) {
        Intent intent =  new Intent( context, CircunListActivity.class );
        intent.putExtra(Constants.EXTRAS.EXTRA_CIRCUN_TYPE, circunType);
        context.startActivity(intent);
    }
}
