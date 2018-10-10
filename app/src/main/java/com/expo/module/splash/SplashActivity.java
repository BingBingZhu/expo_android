package com.expo.module.splash;

import android.os.Bundle;
import android.os.Handler;

import com.expo.BuildConfig;
import com.expo.R;
import com.expo.base.BaseActivity;
import com.expo.base.ExpoApp;
import com.expo.base.utils.PrefsHelper;
import com.expo.contract.SplashContract;
import com.expo.entity.User;
import com.expo.main.MainActivity;
import com.expo.module.guide.GuideFragment;
import com.expo.module.login.LoginActivity;
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
        setDoubleTapToExit( false );
        mHandler.postDelayed( this::next, 3000 );
        mPresenter.loadInitData();
    }

    @Override
    protected boolean hasPresenter() {
        return true;
    }

    @Override
    public void next() {
        if (mPrepared) {
            String shownVer = PrefsHelper.getString( Constants.Prefs.KEY_GUIDE_SHOWN, null );
            if (!BuildConfig.VERSION_NAME.equals( shownVer )) {
                showGuideView();
            } else {
                User user = mPresenter.loadUser();
                if (user == null) {
                    LoginActivity.startActivity( this );
                } else {
                    ExpoApp.getApplication().setUser( user );
                    MainActivity.startActivity( this );
                }
                finish();
            }
        } else {
            mPrepared = true;
        }
    }

    private void showGuideView() {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations( R.anim.slide_in_right, R.anim.slide_in_right )
                .add( R.id.splash_root, new GuideFragment(), null )
                .commit();
    }
}
