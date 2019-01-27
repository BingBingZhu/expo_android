package com.expo.contract;

import android.location.Location;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Venue;

public interface TouristServiceContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract String loadCommonUrlByType(String type);

        public abstract boolean checkInPark(Location location);

        public abstract Venue getNearbyServiceCenter(Location location);
    }

    interface View extends IView {

    }
}
