package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.network.response.VersionInfoResp;

public interface SettingContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void clearCache();

        public abstract void checkUpdate();

        public abstract void update();

        public abstract void logout();

    }

    interface View extends IView {

        void logout();

        void appUpdate(VersionInfoResp info);
    }
}