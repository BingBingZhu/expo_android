package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.User;

public interface SplashContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract void loadInitData();

        public abstract User loadUser();

        public abstract void appRun(String uId, String uKey);
    }

    interface View extends IView {

        void next();

    }
}
