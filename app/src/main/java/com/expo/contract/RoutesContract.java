package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.RouteInfo;

import java.util.List;

public interface RoutesContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void getRoutesData();

        public abstract void clickRoute(String id);

        public abstract void getRouterHotCount();

    }

    interface View extends IView {
        void freshRoutes(List<RouteInfo> list);
    }
}
