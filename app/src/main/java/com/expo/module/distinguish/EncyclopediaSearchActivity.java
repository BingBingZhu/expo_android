package com.expo.module.distinguish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.EncyclopediaSearchContract;

/*
 * 百科搜索页
 */
public class EncyclopediaSearchActivity extends BaseActivity<EncyclopediaSearchContract.Presenter> implements EncyclopediaSearchContract.View {
    @Override
    protected int getContentView() {
        return R.layout.activity_encyclopedia_search;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, EncyclopediaSearchActivity.class );
        context.startActivity( in );
    }
}
