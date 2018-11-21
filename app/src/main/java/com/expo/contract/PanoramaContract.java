package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;

public interface PanoramaContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract String loadPanoramaUrl();
    }

    interface View extends IView {

    }
}
