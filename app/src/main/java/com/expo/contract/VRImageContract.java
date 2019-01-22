package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Message;
import com.expo.entity.VrInfo;

import java.util.List;

public interface VRImageContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void loadVrRecommend(VrInfo vrInfo);

    }

    interface View extends IView {
        void freshVrList(List<VrInfo> list);
    }
}
