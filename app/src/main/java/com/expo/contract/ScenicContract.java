package com.expo.contract;

import com.expo.adapters.Tab;
import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.VenuesType;

import java.util.List;

public interface ScenicContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract List<VenuesType> getTabs();
    }

    interface View extends IView {
    }
}
