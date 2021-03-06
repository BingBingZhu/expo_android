package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.PortalSite;

import java.util.List;

public interface PortalSiteContract {

    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void loadPortalSites();

        public abstract String loadCommonUrl();

    }

    interface View extends IView {

        void setPortalSites(List<PortalSite> list);

    }
}
