package com.expo.module.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.contract.NationalSmsCodeContract;
import com.expo.entity.NationalSmsCode;
import com.expo.utils.Constants;

/*
 * 国家短信代号选择
 */
public class NationalSmsCodeActivity extends BaseActivity<NationalSmsCodeContract.Presenter> implements NationalSmsCodeContract.View {

    private NationalSmsCode mNationalSmsCode;

    @Override
    protected int getContentView() {
        return R.layout.activity_national_sms_code;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mNationalSmsCode = getIntent().getParcelableExtra( Constants.EXTRAS.EXTRAS );
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    /**
     * 启动选择短信前缀国家代号
     *
     * @param activity
     * @param code     当前选择的国家代号
     * @return
     */
    public static int startActvityForResult(@NonNull Activity activity, @NonNull NationalSmsCode code) {
        Intent in = new Intent( activity, NationalSmsCodeActivity.class );
        in.putExtra( Constants.EXTRAS.EXTRAS, code );
        int requestCode = 100;
        activity.startActivityForResult( in, requestCode );
        return requestCode;
    }

    /*
     * 设置返回的结果
     */
    private void setResult(@Nullable NationalSmsCode code) {
        if (code != null || !code.equals( mNationalSmsCode )) {
            Intent extra = new Intent();
            extra.putExtra( Constants.EXTRAS.EXTRAS, code );
            setResult( RESULT_OK, extra );
        } else {
            setResult( RESULT_CANCELED );
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult( null );
    }
}
