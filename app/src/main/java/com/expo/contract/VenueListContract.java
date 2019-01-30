package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Venue;
import com.expo.entity.VrInfo;

import java.util.List;

public interface VenueListContract {

    abstract class Presenter extends IPresenter<VenueListContract.View> {

        public Presenter(VenueListContract.View view) {
            super(view);
        }

        public abstract void loadEncyByType(Long tabId, int page);

        public abstract void loadVrsByType(int vrType, Long id, int page);
    }

    interface View extends IView {
        void addEncysToList(List<Venue> data);

        void addVrsToList(List<VrInfo> data);
    }
}
