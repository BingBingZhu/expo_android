package com.expo.contract;

import com.expo.adapters.DownloadData;
import com.expo.adapters.Tab;
import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.ActualScene;
import com.expo.entity.DataType;
import com.expo.entity.DownloadInfo;
import com.expo.entity.TouristType;

import java.util.List;

public interface ParkMapContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract void loadTab();

        public abstract void loadFacility();

        public abstract void loadTouristType();

        public abstract void startDownloadTask(DownloadData tourist);
        public abstract void stopDownloadTask(DownloadData tourist);

        public abstract void registerDownloadListener();

        public abstract void unregisterDownloadListener();

        public abstract void setData(List<TouristType> mTouristTypes);

        public abstract void saveUsed(List<TouristType> mTouristTypes);
    }

    interface View extends IView {
        void loadTabRes(List<Tab> Tab);

        void loadFacilityRes(List<ActualScene> facilities);

        void loadTouristTypeRes(List<TouristType> touristTypes);

        void updateItemProgress(DownloadData info);

        void updateItemStatus(DownloadData info);
    }
}
