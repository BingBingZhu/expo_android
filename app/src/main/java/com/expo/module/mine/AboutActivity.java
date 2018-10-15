package com.expo.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.R;
import com.expo.base.BaseActivity;

/*
 * 关于我们
 */
public class AboutActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, AboutActivity.class );
        context.startActivity( in );
    }
}
