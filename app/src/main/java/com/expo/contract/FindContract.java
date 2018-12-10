package com.expo.contract;

import com.expo.adapters.Tab;
import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.module.main.find.FindTab;

import java.util.List;

public interface FindContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract void loadTabs();
    }

    interface View extends IView {
        void setTabData(List<FindTab> tabs);
    }
}
