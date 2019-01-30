package com.expo.contract.presenter;

import com.expo.contract.ArContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;

public class ArPresenterImpl extends ArContract.Presenter {
    public ArPresenterImpl(ArContract.View view) {
        super(view);
    }

    @Override
    public String loadCommonInfo(String type) {
        CommonInfo info = mDao.unique(CommonInfo.class, new QueryParams().add("eq", "type", type));
        if (info != null) {
            return info.getLinkUrl();
        }
        return null;
    }

}
