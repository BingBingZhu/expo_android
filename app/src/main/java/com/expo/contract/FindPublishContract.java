package com.expo.contract;

import android.location.Location;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.CommonInfo;
import com.expo.entity.Find;
import com.expo.entity.VisitorService;

public interface FindPublishContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void addSociety(Find find);

        public abstract void clickPolicy(String userProtocol);
    }

    interface View extends IView {
        void complete();

        void returnCommonInfo(CommonInfo commonInfo);
    }
}
