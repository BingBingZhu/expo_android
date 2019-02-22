package com.expo.contract.presenter;

import com.expo.contract.PortalSiteContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;
import com.expo.entity.PortalSite;

import java.util.List;

public class PortalSitePresenterImpl extends PortalSiteContract.Presenter {
    public PortalSitePresenterImpl(PortalSiteContract.View view) {
        super(view);
    }

    @Override
    public void loadPortalSites() {
        List<PortalSite> list = mDao.query(PortalSite.class, null);
        mView.setPortalSites(list);
    }

    @Override
    public String loadCommonUrl() {
        return mDao.unique(CommonInfo.class, new QueryParams()
                .add("eq", "type", CommonInfo.EXPO_PLANT_WIKI)).getLinkUrl();
    }
}
