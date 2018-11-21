package com.expo.module.language;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.PrefsHelper;
import com.expo.contract.LoginContract;
import com.expo.module.login.BindPhoneActivity;
import com.expo.module.login.LoginActivity;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class LanguageActivity extends BaseActivity<LoginContract.Presenter> {

    @BindView(R.id.language_create_btn)
    TextView mTvCreate;
    @BindView(R.id.languane_login)
    TextView mTvLogin;
    @BindView(R.id.language_cn)
    View mLanguageCn;
    @BindView(R.id.language_en)
    View mLanguageEn;

    Locale mLocale = null;
    String mLanguage = null;

    @Override
    protected int getContentView() {
        return R.layout.activity_language;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setDoubleTapToExit( true );
//        mLanguage = PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null);
//        if (mLanguage == null) {
//            mLanguageCn.performClick();
//        } else if (StringUtils.equals(LanguageUtil.LANGUAGE_CN, mLanguage)) {
//            mLanguageCn.performClick();
//        } else if (StringUtils.equals(LanguageUtil.LANGUAGE_EN, mLanguage)) {
//            mLanguageEn.performClick();
//        }
        if (LanguageUtil.isCN())
            mLanguageCn.performClick();
        else
            mLanguageEn.performClick();

    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    @OnClick({R.id.language_cn, R.id.language_en})
    public void selectLanague(View view) {
        mLanguageCn.setSelected(false);
        mLanguageEn.setSelected(false);
        view.setSelected(true);
        switch (view.getId()) {
            case R.id.language_cn:
                mLocale = Locale.CHINA;
                mLanguage = LanguageUtil.LANGUAGE_CN;
                mTvCreate.setText("创建账号");
                mTvLogin.setText("登录");
                break;
            case R.id.language_en:
                mLocale = Locale.ENGLISH;
                mLanguage = LanguageUtil.LANGUAGE_EN;
                mTvCreate.setText("Create Account");
                mTvLogin.setText("Login");
                break;

        }
    }

    @OnClick(R.id.languane_login)
    public void login(View view) {
        LoginActivity.startActivity(this);
        finish();
    }

    @OnClick(R.id.language_create_btn)
    public void createLanguage() {
        if (mLocale == null) {
            return;
        }
        LanguageUtil.changeAppLanguage(this, mLocale);

        PrefsHelper.setString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, mLanguage);
        LoginActivity.startActivity(this);
        finish();
    }

}
