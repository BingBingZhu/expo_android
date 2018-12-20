package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Find;

public interface FindDetailContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void addEnjoy(String id);

        public abstract void addViews(String id);

    }

    interface View extends IView {

        void addEnjoyRes();

    }
}
