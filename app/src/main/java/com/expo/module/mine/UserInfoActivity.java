package com.expo.module.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.UserInfoContract;

/*
 * 用户信息页
 */
public class UserInfoActivity extends BaseActivity<UserInfoContract.Presenter> implements UserInfoContract.View {
    @Override
    protected int getContentView() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动用户信息页
     *
     * @param context
     */
    public static void startActivity(@NonNull Context context) {
        Intent in = new Intent( context, UserInfoActivity.class );
        context.startActivity( in );
    }
}
