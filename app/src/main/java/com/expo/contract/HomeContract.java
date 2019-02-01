package com.expo.contract;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.AppInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.ExpoActivityInfo;
import com.expo.entity.RouteInfo;
import com.expo.entity.TopLineInfo;
import com.expo.entity.VrInfo;

import java.util.List;

public interface HomeContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void setMessageCount();

        public abstract void setTopLine();

        public abstract void setVenue();

//        public abstract void setExhibit();

//        public abstract void setExhibitGarden();

        public abstract void setHotActivity();

        public abstract void setRecommendRoute();

        public abstract void setRankingScenic();

        public abstract void setVr();

        public abstract void setFood();

        public abstract void setHotel();

        public abstract void startHeartService(Context context);

        public abstract void stopHeartService(Context context);

        public abstract String loadCommonInfo(String type);

        public abstract Float getDistance(long id, LatLng latLng);

        public abstract void sortVenue(List<Encyclopedias> list);

        public abstract void appRun(String jgId);

        public abstract void checkUpdate();

        public abstract void update(Context context, AppInfo appInfo);
    }

    interface View extends IView {
        void showTopLine(List<TopLineInfo> list);

        void showVenue(List<Encyclopedias> list);

//        void showExhibit(List<Encyclopedias> list);

//        void showExhibitGarden(List<Encyclopedias> list);

        void appUpdate(AppInfo appInfo);

        void showActivity(List<ExpoActivityInfo> list);

        void showRoute(List<RouteInfo> list);

        void showRankingScenic(List<Encyclopedias> list);

        void showVr(List<VrInfo> data);

        void showFood(List<Encyclopedias> list);

        void showHotel(List<Encyclopedias> list);
    }
}
