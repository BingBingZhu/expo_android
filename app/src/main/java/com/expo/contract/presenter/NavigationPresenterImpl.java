package com.expo.contract.presenter;

import com.expo.contract.NavigationContract;
import com.expo.entity.ActualScene;

public class NavigationPresenterImpl extends NavigationContract.Presenter {
    public NavigationPresenterImpl(NavigationContract.View view) {
        super( view );
    }

    @Override
    public ActualScene loadSceneById(long spotId) {
        return mDao.queryById( ActualScene.class, spotId );
    }
}
