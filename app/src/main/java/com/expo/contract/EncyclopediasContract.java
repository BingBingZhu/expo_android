package com.expo.contract;

import com.expo.adapters.Tab;
import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Subject;

import java.util.List;

public interface EncyclopediasContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract void loadTabs();
    }

    interface View extends IView {
        void setTabData(List<Tab> tabs);
    }
}
