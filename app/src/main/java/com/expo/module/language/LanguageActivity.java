package com.expo.module.language;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.utils.PrefsHelper;
import com.expo.contract.LoginContract;
import com.expo.module.bind.BindActivity;
import com.expo.module.login.LoginActivity;
import com.expo.utils.Constants;
import com.expo.utils.LanguageUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class LanguageActivity extends BaseActivity<LoginContract.Presenter> {

    @BindView(R.id.language_cn)
    RelativeLayout language_cn;
    @BindView(R.id.language_en)
    RelativeLayout language_en;

    Locale locale = null;
    String language = null;

    @Override
    protected int getContentView() {
        return R.layout.activity_language;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        language = PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null);
        if (language == null) {

        } else if (LanguageUtil.LANGUAGE_CN.endsWith(language)) {
            language_cn.performClick();
        } else if (LanguageUtil.LANGUAGE_EN.endsWith(language)) {
            language_en.performClick();
        }
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    @OnClick({R.id.language_cn, R.id.language_en})
    public void selectLanague(View view) {
        language_cn.setSelected(false);
        language_en.setSelected(false);
        view.setSelected(true);
        switch (view.getId()) {
            case R.id.language_cn:
                locale = Locale.CHINA;
                language = LanguageUtil.LANGUAGE_CN;
                break;
            case R.id.language_en:
                locale = Locale.ENGLISH;
                language = LanguageUtil.LANGUAGE_EN;
                break;

        }
    }

    @OnClick(R.id.back)
    public void back(View view) {
        finish();
    }

    @OnClick(R.id.login)
    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.language_create_btn)
    public void createLanguage() {
        if (locale == null) {
            return;
        }
        LanguageUtil.changeAppLanguage(this, locale);

        PrefsHelper.setString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, language);

        Intent intent = new Intent(this, LoginActivity.class);
//        Intent intent = new Intent(this, BindActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
