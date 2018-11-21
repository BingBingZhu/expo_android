package com.expo.contract.presenter;

import com.expo.contract.TouristServiceContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;

public class TouristServicePresenterImpl extends TouristServiceContract.Presenter {
    public TouristServicePresenterImpl(TouristServiceContract.View view) {
        super( view );
    }

    @Override
    public String loadCommonUrlByType(String type) {
        CommonInfo info = mDao.unique( CommonInfo.class, new QueryParams().add( "eq", "type", type ) );
        if (info != null) {
            return info.getLinkUrl();
        }
        return null;
    }

}
