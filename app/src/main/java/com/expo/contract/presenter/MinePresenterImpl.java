package com.expo.contract.presenter;

import com.expo.contract.MineContract;
import com.expo.entity.User;

public class MinePresenterImpl extends MineContract.Presenter {
    public MinePresenterImpl(MineContract.View view) {
        super(view);
    }

    @Override
    public void loadUser() {
        mView.freshUser(mDao.unique(User.class, null));
    }
}
