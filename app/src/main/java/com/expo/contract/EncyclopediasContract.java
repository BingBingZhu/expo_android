package com.expo.contract;

import android.content.Context;

import com.expo.adapters.Tab;
import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.VenuesType;

import java.util.List;

public interface EncyclopediasContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void loadTabs(Context context, List<VenuesType> list);
    }

    interface View extends IView {
        void setTabData(List<Tab> tabs);
    }
}
