package com.expo.contract;

import android.graphics.Bitmap;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Venue;

import java.util.List;

public interface PlayMapContract {

    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void queryVenueAndNearByType(String typeName);

        public abstract void queryVenuesByVenue(Venue venue);
    }

    interface View extends IView {

        void queryVenueRes(List<Venue> venues);

        void updateMarkerPic(Bitmap bmp);
    }

}
