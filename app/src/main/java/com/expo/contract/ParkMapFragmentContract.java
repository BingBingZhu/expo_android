package com.expo.contract;

import com.amap.api.maps.model.LatLng;
import com.expo.adapters.DownloadData;
import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.CustomRoute;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Park;
import com.expo.entity.RouteInfo;
import com.expo.entity.TouristType;
import com.expo.entity.Venue;
import com.expo.entity.VenuesType;

import java.util.ArrayList;
import java.util.List;

public interface ParkMapFragmentContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void startDownloadTask(DownloadData tourist);

        public abstract void stopDownloadTask(DownloadData tourist);

        public abstract void registerDownloadListener();

        public abstract void unregisterDownloadListener();

        public abstract void setData(List<TouristType> mTouristTypes);

        public abstract void saveUsed(List<TouristType> mTouristTypes);

        public abstract void loadParkMapData(Long asId, List<VenuesType> venuesTypes);

        public abstract Encyclopedias getEncy(String wikiId);

        public abstract List<Venue> getActualScenes(ArrayList<Integer> ids);

        public abstract List<Venue> selectVenueByCaption(String caption);

        public abstract boolean checkInVenue(LatLng latLng, Venue venue);
    }

    interface View extends IView {
        void loadTabRes(List<VenuesType> venuesTypes, int tabPosition);

        void loadFacilityRes(List<Venue> facilities, Venue as);

        void loadTouristTypeRes(List<TouristType> touristTypes);

        void updateItemProgress(DownloadData info);

        void updateItemStatus(DownloadData info);

        void updatePic(VenuesType vt);

        void showParkScope(Park park);

        void loadRoute(List<RouteInfo> routeInfos);

        void loadCustomRoute(List<CustomRoute> customRoutes);
    }
}
