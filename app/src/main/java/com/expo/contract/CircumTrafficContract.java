package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Venue;

import java.util.List;

public interface CircumTrafficContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void loadTrafficData();
    }

    interface View extends IView {
        void loadTrafficDataRes(List<Venue> venuePark, List<Venue> venueBus);
    }
}
