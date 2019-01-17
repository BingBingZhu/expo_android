package com.expo.contract;

import android.location.Location;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Venue;
import com.expo.entity.VisitorService;

public interface OnlineHomeContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void get1(VisitorService visitorService);
        public abstract void get2(VisitorService visitorService);
        public abstract void get3(VisitorService visitorService);

    }

    interface View extends IView {
    }
}
