package com.expo.contract.presenter;

import com.expo.contract.FreeWiFiContract;
import com.expo.entity.Park;

public class FreeWifiPresenterImpl extends FreeWiFiContract.Presenter {
    public FreeWifiPresenterImpl(FreeWiFiContract.View view) {
        super( view );
    }

    @Override
    public String[] queryWifi() {
        Park park = mDao.unique( Park.class, null );
        return new String[]{ park.getParkWifiId(), park.getParkWifiPsd() };
    }
}
