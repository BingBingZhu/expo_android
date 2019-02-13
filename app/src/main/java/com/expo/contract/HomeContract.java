package com.expo.contract;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.AppInfo;
import com.expo.entity.Circum;
import com.expo.entity.CommonInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.ExpoActivityInfo;
import com.expo.entity.RouteInfo;
import com.expo.entity.TopLineInfo;
import com.expo.entity.Venue;
import com.expo.entity.VrInfo;

import java.util.List;
import java.util.Map;

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

        //加载主菜单
        public abstract List<int[]> loadMainMenuDate();

        //世园活动
        public abstract List loadExpoActivities();

        //推荐路线
        public abstract List loadRouteInfo();

        //经典
        public abstract List<Object> loadSciences();

        //推荐网上世园
        public abstract List<Object> loadVrInfo();

        //预约
        public abstract List<Object> loadBespeak();

        //世园美食
        public abstract List<Object> loadExpoFoods();

        //周边美食
        public abstract void loadOutsideFoods();

        //世园酒店
        public abstract List<Object> loadHotels();

        //延庆必体验
        public abstract List<Object> loadNearbyExperience();

        //发现数据
        public abstract List<Object> loadDiscover();

        //根据类型名找对应id
        public abstract long loadTypeId(String typeName);

        public abstract String loadBespeakUrlInfo();

    }

    interface View extends IView {
        void showTopLine(List<ExpoActivityInfo> list);

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

        void setOutsideFoods(List<Circum> circums);
    }
}
