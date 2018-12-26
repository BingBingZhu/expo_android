package com.expo.contract;

import android.content.Context;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.AppInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.TopLineInfo;

import java.util.List;

public interface HomeContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract void setMessageCount();

        public abstract void setTopLine();

        public abstract void setVenue();

        public abstract void setExhibit();

        public abstract void setExhibitGarden();

        public abstract void startHeartService(Context context);

        public abstract void stopHeartService(Context context);

        public abstract String loadCommonInfo(String type);

        public abstract void appRun(String jgId);

        public abstract void checkUpdate();

        public abstract void update(Context context, AppInfo appInfo);
    }

    interface View extends IView {
        void showTopLine(List<TopLineInfo> list);

        void showVenue(List<Encyclopedias> list);

        void showExhibit(List<Encyclopedias> list);

        void showExhibitGarden(List<Encyclopedias> list);

        void appUpdate(AppInfo appInfo);
    }
}
