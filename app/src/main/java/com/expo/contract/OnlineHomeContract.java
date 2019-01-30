package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.TouristType;
import com.expo.entity.VrInfo;

import java.util.List;

public interface OnlineHomeContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void loadVrHot();

        public abstract void loadData();

        public abstract void loadMore(int page);

        public abstract void loadTouristType();

    }

    interface View extends IView {

        void loadLiveDataRes(List<VrInfo> liveVrs);

        void loadCultureDataRes(List<VrInfo> cultureVrs);

        void loadTourDataRes(List<VrInfo> tourVrs);

        void loadMoreTourDataRes(List<VrInfo> tourVrs);

        void loadTouristTypeRes(TouristType touristType);
    }
}
