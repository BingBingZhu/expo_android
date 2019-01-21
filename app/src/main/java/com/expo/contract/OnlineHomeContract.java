package com.expo.contract;

import android.location.Location;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.RollData;
import com.expo.entity.Venue;
import com.expo.entity.VisitorService;

import java.util.List;

public interface OnlineHomeContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void loadRollData();

    }

    interface View extends IView {
        void loadRollDataRes(List<RollData> rollDataList);
    }
}
