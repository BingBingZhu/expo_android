package com.expo.contract.presenter;

import com.expo.contract.MineContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;
import com.expo.entity.User;

public class MinePresenterImpl extends MineContract.Presenter {
    public MinePresenterImpl(MineContract.View view) {
        super(view);
    }

    @Override
    public void loadUser() {
        mView.freshUser(mDao.unique(User.class, null));
    }

    @Override
    public void clickPolicy(String type) {
        CommonInfo commonInfo = mDao.unique(CommonInfo.class, new QueryParams().add("eq", "type", type));
        mView.returnCommonInfo(commonInfo);

    }
}
