package com.expo.contract.presenter;

import com.expo.contract.PanoramaContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;

public class PanoramaPresenterImpl extends PanoramaContract.Presenter {
    public PanoramaPresenterImpl(PanoramaContract.View view) {
        super( view );
    }

    @Override
    public String loadPanoramaUrl() {
        CommonInfo info = mDao.unique( CommonInfo.class, new QueryParams().add( "eq", "type", "7" ) );
        if (info != null) {
            return info.getLinkUrl();
        }
        return null;
    }


}
