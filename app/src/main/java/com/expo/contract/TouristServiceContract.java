package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;

public interface TouristServiceContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract String loadCommonUrlByType(String type);
    }

    interface View extends IView {

    }
}
