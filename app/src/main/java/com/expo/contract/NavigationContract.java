package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Venue;
import com.expo.entity.Encyclopedias;
import com.expo.entity.TouristType;
import com.expo.entity.VenuesDistance;

import java.util.List;

public interface NavigationContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract Venue loadSceneById(long spotId);

        public abstract List<VenuesDistance> getVenues();

        public abstract Encyclopedias getEncyclopedias(String id);

        public abstract TouristType getTourist();

        public abstract void submitTouristRecord(double sx, double xy, double ex, double ey, float speed);
    }

    interface View extends IView {

    }
}
