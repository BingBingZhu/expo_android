package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.FootPrint;

public interface GPSLocationContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void recordLocation(FootPrint footPrint);
    }

    interface View extends IView {
    }
}
