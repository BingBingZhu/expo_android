package com.expo.module.circum;

import android.os.Bundle;
import android.view.View;

import com.expo.R;
import com.expo.base.BaseActivity;

import butterknife.OnClick;

public class CircunHomeActivity extends BaseActivity {



    @Override
    protected int getContentView() {
        return R.layout.activity_circun_home;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @OnClick({R.id.circun_cate, R.id.circun_hotel, R.id.circun_shop, R.id.circun_traffic, R.id.circun_scenic})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.circun_cate:
                CircunListActivity.startActivity(getContext(), 1);
                break;
            case R.id.circun_hotel:
                CircunListActivity.startActivity(getContext(), 2);
                break;
            case R.id.circun_shop:
                CircunListActivity.startActivity(getContext(), 3);
                break;
            case R.id.circun_traffic:
                CircunListActivity.startActivity(getContext(), 4);
                break;
            case R.id.circun_scenic:
                CircunListActivity.startActivity(getContext(), 5);
                break;
        }
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }
}
