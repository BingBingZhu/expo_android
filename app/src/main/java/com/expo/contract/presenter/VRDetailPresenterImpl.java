package com.expo.contract.presenter;

import com.expo.contract.VRDetailContract;
import com.expo.db.QueryParams;
import com.expo.entity.RouteInfo;
import com.expo.entity.VrInfo;

public class VRDetailPresenterImpl extends VRDetailContract.Presenter {

    public VRDetailPresenterImpl(VRDetailContract.View view) {
        super(view);
    }

    @Override
    public VrInfo getVrInfo(String id) {
        return mDao.queryById(VrInfo.class, id);
    }

}
