package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.RouteInfo;

import java.util.List;

public interface SceneContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void getExpo(String type, boolean fresh, int page, int count);

    }

    interface View extends IView {
        void freshRoutes(boolean fresh, List list);
    }
}
