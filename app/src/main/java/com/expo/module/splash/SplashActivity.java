package com.expo.module.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.expo.BuildConfig;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.ExpoApp;
import com.expo.base.utils.PrefsHelper;
import com.expo.contract.SplashContract;
import com.expo.entity.User;
import com.expo.module.camera.CameraActivity;
import com.expo.module.language.LanguageActivity;
import com.expo.module.main.MainActivity;
import com.expo.module.guide.GuideFragment;
import com.expo.module.guide.LanguageFragment;
import com.expo.module.login.LoginActivity;
import com.expo.module.mine.UserInfoActivity;
import com.expo.module.routes.RoutesActivity;
import com.expo.utils.Constants;

public class SplashActivity extends BaseActivity<SplashContract.Presenter> implements SplashContract.View {

    private Handler mHandler = new Handler();
    private boolean mPrepared;

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setDoubleTapToExit(false);
        mHandler.postDelayed(this::next, 3000);
        mPresenter.loadInitData();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    public void next() {
        if (false) {//直接去往某个界面，调试开发用的
            UserInfoActivity.startActivity(this);
        }
        if (mPrepared) {
            String shownVer = PrefsHelper.getString(Constants.Prefs.KEY_GUIDE_SHOWN, null);
            boolean showSelectLanguage = PrefsHelper.getBoolean(Constants.Prefs.KEY_SHOW_SELECT_LANGUAGE, true);
            if (!BuildConfig.VERSION_NAME.equals(shownVer)) {//检查当前版本是否首次使用
                showFragment(new GuideFragment());
            } else if (PrefsHelper.getString(Constants.Prefs.KEY_LANGUAGE_CHOOSE, null) == null) {
                startActivity(new Intent(this, LanguageActivity.class));
//            } else if (showSelectLanguage) {//检查是否需要显示语言选择
//                showFragment(new LanguageFragment());
            } else {//检查是否需要登录
                User user = mPresenter.loadUser();
                if (user == null) {
                    LoginActivity.startActivity(this);
                } else {
                    ExpoApp.getApplication().setUser(user);
                    MainActivity.startActivity(this);
//                    CameraActivity.startActivity(this);
                    mPresenter.appRun(user.getUid(), user.getUkey());
                }
                finish();
            }
        } else {
            mPrepared = true;
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.splash_root, fragment, fragment.getClass().getName())
                .commit();
    }
}
