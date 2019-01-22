package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.VrInfo;

public interface VRDetailContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract VrInfo getVrInfo(String id);

    }

    interface View extends IView {
    }
}
