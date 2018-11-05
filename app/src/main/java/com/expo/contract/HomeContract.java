package com.expo.contract;

import android.content.Context;

import com.expo.base.IPresenter;
import com.expo.base.IView;

public interface HomeContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void startHeartService(Context context);

        public abstract void stopHeartService(Context context);

    }

    interface View extends IView {

        void freshHeartMessage();

    }
}
