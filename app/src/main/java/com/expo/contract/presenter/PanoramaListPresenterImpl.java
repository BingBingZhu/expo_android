package com.expo.contract.presenter;

import com.expo.contract.PanoramaListContract;
import com.expo.db.QueryParams;
import com.expo.entity.VrLableInfo;

import java.util.List;

public class PanoramaListPresenterImpl extends PanoramaListContract.Presenter {

    public PanoramaListPresenterImpl(PanoramaListContract.View view) {
        super(view);
    }

    @Override
    public void loadTabData(int type) {
        List<VrLableInfo> tabs = mDao.query(VrLableInfo.class, new QueryParams().add("eq", "link_id", type));
        mView.loadTabRes(tabs);
    }


}
