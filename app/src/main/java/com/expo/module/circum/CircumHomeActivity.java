package com.expo.module.circum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.expo.R;
import com.expo.base.BaseActivity;

import butterknife.OnClick;

public class CircumHomeActivity extends BaseActivity {



    @Override
    protected int getContentView() {
        return R.layout.activity_circum_home;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setTitle(1, "世园周边");
    }

    @OnClick({R.id.circun_cate, R.id.circun_hotel, R.id.circun_shop, R.id.circun_traffic, R.id.circun_scenic})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.circun_cate:
                CircumListActivity.startActivity(getContext(), 1);
                break;
            case R.id.circun_hotel:
                CircumListActivity.startActivity(getContext(), 2);
                break;
            case R.id.circun_shop:
                CircumListActivity.startActivity(getContext(), 3);
                break;
            case R.id.circun_traffic:
                CircumListActivity.startActivity(getContext(), 4);
                break;
            case R.id.circun_scenic:
                CircumListActivity.startActivity(getContext(), 5);
                break;
        }
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(Context context) {
        Intent intent =  new Intent( context, CircumHomeActivity.class );
        context.startActivity(intent);
    }
}
