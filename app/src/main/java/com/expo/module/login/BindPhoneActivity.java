package com.expo.module.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.BindPhoneContract;
import com.expo.entity.User;
import com.expo.utils.Constants;

/*
 * 第三方登录后绑定手机号
 */
public class BindPhoneActivity extends BaseActivity<BindPhoneContract.Presenter> implements BindPhoneContract.View {
    @Override
    protected int getContentView() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {

    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动第三方登录绑定手机号操作页
     *
     * @param context
     * @param user    第三方登录获得的可用用户信息
     */
    public static void startActivity(Context context, User user) {
        Intent in = new Intent( context, BindPhoneActivity.class );
        in.putExtra( Constants.EXTRAS.EXTRAS, user );
        context.startActivity( in );
    }
}
