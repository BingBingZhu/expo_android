package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.ActualScene;
import com.expo.entity.Encyclopedias;
import com.expo.entity.VenuesDistance;
import com.expo.entity.VenuesInfo;

import java.util.List;

public interface NavigationContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract ActualScene loadSceneById(long spotId);

        public abstract List<VenuesDistance> getVenues();

        public abstract Encyclopedias getEncyclopedias(String id);
    }

    interface View extends IView {

    }
}
