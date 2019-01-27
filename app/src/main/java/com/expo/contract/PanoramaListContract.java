package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.VrLableInfo;

import java.util.List;

public interface PanoramaListContract {
    abstract class Presenter extends IPresenter<PanoramaListContract.View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void loadTabData(int type);
    }

    interface View extends IView {

        void loadTabRes(List<VrLableInfo> tabs);
    }
}
