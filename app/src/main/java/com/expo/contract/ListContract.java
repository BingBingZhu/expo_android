package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Encyclopedias;
import com.expo.entity.VrInfo;

import java.util.List;

public interface ListContract {

    abstract class Presenter extends IPresenter<ListContract.View> {

        public Presenter(ListContract.View view) {
            super(view);
        }

        public abstract void loadEncyByType(Long tabId, int page);

        public abstract void loadVrsByType(Long id, int page);
    }

    interface View extends IView {
        void addEncysToList(List<Encyclopedias> data);

        void addVrsToList(List<VrInfo> data);
    }
}
