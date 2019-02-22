package com.expo.contract;

import com.expo.adapters.DownloadData;
import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.CustomRoute;
import com.expo.entity.Park;
import com.expo.entity.RouteInfo;
import com.expo.entity.TouristType;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;

import java.util.List;

public interface CircumTrafficContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void loadTrafficData();
    }

    interface View extends IView {
        void loadTrafficDataRes(List<Venue> venuePark, List<Venue> venueBus);

//        void loadTabRes(List<VenuesType> venuesTypes, int tabPosition);

//        void updatePic(VenuesType vt);

        void showParkScope(Park park);
    }
}
