package com.expo.contract.presenter;

import com.expo.contract.GPSLocationContract;
import com.expo.entity.FootPrint;

public class GPSLocationPresenterImpl extends GPSLocationContract.Presenter {
    public GPSLocationPresenterImpl(GPSLocationContract.View view) {
        super(view);
    }

    @Override
    public void recordLocation(FootPrint footPrint) {
        mDao.saveOrUpdate(footPrint);
    }
}
