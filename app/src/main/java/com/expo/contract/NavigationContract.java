package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.ActualScene;

public interface NavigationContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract ActualScene loadSceneById(long spotId);
    }

    interface View extends IView {

    }
}
