package com.expo.contract;

import android.location.Location;

import com.expo.base.IPresenter;
import com.expo.base.IView;

public interface HeartBeatContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void sendHeartBeat(Location location);
    }

    interface View extends IView {
        void setHeartInvTime(int heartInvTime);
    }
}
