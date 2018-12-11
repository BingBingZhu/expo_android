package com.expo.contract;

import com.amap.api.maps.model.LatLng;
import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.CustomRoute;
import com.expo.entity.Park;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;

import java.util.List;

public interface CustomRouteContract {

    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void clearCustomRoute();

        public abstract void saveCustomRoute(List<Venue> venues, List<CustomRoute> customRoutes);

        public abstract void loadParkMapData();
    }

    interface View extends IView {
        void showParkScope(Park park);

        void loadFacilityRes(List<Venue> facilities);

        void updateMarkerPic(VenuesType vt);

        void loadCustomRoute(List<CustomRoute> customRoutes);
    }
}
