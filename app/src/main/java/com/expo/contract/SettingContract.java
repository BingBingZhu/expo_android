package com.expo.contract;

import android.content.Context;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.AppInfo;
import com.expo.entity.CommonInfo;

public interface SettingContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract void clearCache();

        public abstract void checkUpdate();

        public abstract void update(Context context, AppInfo appInfo);

        public abstract void logout();

        public abstract void clickPolicy(String type);
    }

    interface View extends IView {

        void logout();

        //void appUpdate(VersionInfoResp info);

        void returnCommonInfo(CommonInfo commonInfo);

        void appUpdate(AppInfo bean);
    }
}
