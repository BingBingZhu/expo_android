package com.expo.contract.presenter;

import com.expo.contract.FreeWiFiContract;
import com.expo.entity.Park;

public class FreeWifiPresenterImpl extends FreeWiFiContract.Presenter {
    public FreeWifiPresenterImpl(FreeWiFiContract.View view) {
        super( view );
    }

    @Override
    public String[] queryWifi() {
        Park park = mDao.queryById(Park.class, 1);
        return new String[]{ park.getParkWifiId(), park.getParkWifiPsd() };
    }
}
