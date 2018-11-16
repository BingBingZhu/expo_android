package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;

public interface HeartBeatContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void sendHeartBeat();
    }

    interface View extends IView {
        void setHeartInvTime(int heartInvTime);
    }
}
