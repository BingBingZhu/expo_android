package com.expo.module.guide;

import android.os.Bundle;

import com.expo.R;
import com.expo.base.BaseFragment;
import com.expo.base.utils.PrefsHelper;
import com.expo.module.splash.SplashActivity;
import com.expo.utils.Constants;

/**
 * 语言选择页
 */
public class LanguageFragment extends BaseFragment {
    @Override
    public int getContentView() {
        return R.layout.fragment_language;
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        //设置语言选择页已显示过
        PrefsHelper.setBoolean( Constants.Prefs.KEY_SHOW_SELECT_LANGUAGE, false );
    }

    @Override
    protected boolean hasPresenter() {
        return false;
    }

    /*
     * 跳转下一功能页
     */
    private void next() {
        ((SplashActivity) getActivity()).next();
    }
}
