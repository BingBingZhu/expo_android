package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.RouteInfo;
import com.expo.entity.VenuesInfo;

import java.util.List;

public interface RouteDetailContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void getRouteDetail(Long id);

        public abstract void getVenuesList(String ids);

    }

    interface View extends IView {

        void showRouteDetail(RouteInfo info);

        void showVenuesList(List<VenuesInfo> list);
    }
}
