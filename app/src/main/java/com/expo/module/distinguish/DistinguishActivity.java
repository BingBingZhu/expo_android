package com.expo.module.distinguish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.DistinguishContract;

/*
 * 识花界面
 */
public class DistinguishActivity extends BaseActivity<DistinguishContract.Presenter> implements DistinguishContract.View {
    @Override
    protected int getContentView() {
        return R.layout.activity_distinguish;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, DistinguishActivity.class );
        context.startActivity( in );
    }
}
