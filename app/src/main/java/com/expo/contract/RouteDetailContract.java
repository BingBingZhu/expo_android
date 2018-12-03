package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.RouteInfo;
import com.expo.entity.Venue;

import java.util.List;

public interface RouteDetailContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract void getRouteDetail(Long id);

        public abstract void getRouteDetailFromency(String id);

        public abstract void getVenuesList(String ids);

        public abstract void loadRemarkFormEncyclopedia(Venue venue);
    }

    interface View extends IView {

        void showRouteDetail(RouteInfo info);

        void showRemarkDetail(String remark);

        void showVenuesList(List<Venue> list);
    }
}
