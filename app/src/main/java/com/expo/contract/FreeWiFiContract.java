package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;

public interface FreeWiFiContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract String[] queryWifi();
    }

    interface View extends IView {

    }
}
