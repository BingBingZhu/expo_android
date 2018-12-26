package com.expo.contract.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.expo.contract.HomeContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Message;
import com.expo.entity.TopLineInfo;
import com.expo.entity.Venue;
import com.expo.module.heart.HeartBeatService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .add("orderBy", "idx", true);
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
                .add("orderBy", "idx", true);
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
                .add("orderBy", "idx", true);
        mView.showExhibitGarden(mDao.query(Encyclopedias.class, params));
    }

    public void startHeartService(Context context) {
        HeartBeatService.startService(context);
    }

    @Override
    public void stopHeartService(Context context) {
        HeartBeatService.stopService(context);
    }

    @Override
    public String loadCommonInfo(String type) {
        CommonInfo info = mDao.unique(CommonInfo.class, new QueryParams().add("eq", "type", type));
        if (info != null) {
            return info.getLinkUrl();
        }
        return null;
    }

    @Override
    public Float getDistance(long id, LatLng latLng) {
        QueryParams queryParams = new QueryParams()
                .add("eq", "wiki_id", id);
        List<Venue> venues = mDao.query(Venue.class, queryParams);
        if (venues == null || venues.size() == 0) return 999999F;
        Venue venue = venues.get(0);
        Float distance = AMapUtils.calculateLineDistance(latLng, new LatLng(venue.getLat(), venue.getLng()));
        return distance;
    }

    @Override
    public void sortVenue(List<Encyclopedias> list) {
        Collections.sort(list, (o1, o2) -> o1.getDistance().compareTo(o2.getDistance()));
    }
}
