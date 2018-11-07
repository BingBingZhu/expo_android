package com.expo.contract.presenter;

import android.content.Context;

import com.expo.contract.HomeContract;
import com.expo.db.QueryParams;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Message;
import com.expo.entity.TopLineInfo;

import java.util.ArrayList;
import java.util.List;

import com.expo.module.heart.HeartBeatService;

public class HomePresenterImpl extends HomeContract.Presenter {
    public HomePresenterImpl(HomeContract.View view) {
        super(view);
    }

    @Override
    public void setMessageCount() {
        new Message().sendMessageCount(null);
    }

    @Override
    public void setTopLine() {
        mView.showTopLine(mDao.query(TopLineInfo.class, null));
    }

    @Override
    public void setVenue() {
        QueryParams params = new QueryParams()
                .add("eq", "enable", 1)
                .add("and")
                .add("eq", "recommend", 1)
                .add("and")
                .add("eq", "type_name", "场馆")
                .add("orderBy", "recommended_idx", true);
        mView.showVenue(mDao.query(Encyclopedias.class, params));
    }

    @Override
    public void setExhibit() {
        //从数据库获取
        QueryParams params = new QueryParams()
                .add("eq", "enable", 1)
                .add("and")
                .add("eq", "recommend", 1)
                .add("and")
                .add("eq", "type_name", "展览")
                .add("orderBy", "recommended_idx", true);
        mView.showExhibit(mDao.query(Encyclopedias.class, params));
    }

    @Override
    public void setExhibitGarden() {
        //从数据库获取
        QueryParams params = new QueryParams()
                .add("eq", "enable", 1)
                .add("and")
                .add("eq", "recommend", 1)
                .add("and")
                .add("eq", "type_name", "展园")
                .add("orderBy", "recommended_idx", true);
        mView.showExhibitGarden(mDao.query(Encyclopedias.class, params));
    }

    public void startHeartService(Context context) {
        HeartBeatService.startService(context);
    }

    @Override
    public void stopHeartService(Context context) {
        HeartBeatService.stopService(context);
    }
}
