package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.contract.presenter.HeartBeatPresenterImpl;
import com.expo.contract.presenter.LoginPresenterImpl;
import com.expo.contract.presenter.SplashPresenterImpl;

public class PresenterFactory {
    public static IPresenter getPresenter(IView view) {
        if (view instanceof SplashContract.View) {
            return new SplashPresenterImpl( (SplashContract.View) view );
        } else if (view instanceof LoginContract.View) {
            return new LoginPresenterImpl( (LoginContract.View) view );
        } else if (view instanceof HeartBeatContract.View) {
            return new HeartBeatPresenterImpl( (HeartBeatContract.View) view );
        }
        throw new UnsupportedOperationException();
    }
}
