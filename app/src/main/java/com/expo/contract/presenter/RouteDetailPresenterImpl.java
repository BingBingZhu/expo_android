package com.expo.contract.presenter;

import com.expo.contract.RouteDetailContract;
import com.expo.db.QueryParams;
import com.expo.entity.RouteInfo;
import com.expo.entity.VenuesInfo;

import java.util.Arrays;

public class RouteDetailPresenterImpl extends RouteDetailContract.Presenter {

    public RouteDetailPresenterImpl(RouteDetailContract.View view) {
        super(view);
    }

    @Override
    public void getRouteDetail(Long id) {
        QueryParams params = new QueryParams();
        params.add("eq", "id", id);
        mView.showRouteDetail(mDao.unique(RouteInfo.class, params));
    }

    @Override
    public void getVenuesList(String ids) {
        String[] idArr = ids.split(",");
        QueryParams params = new QueryParams();
        params.add("in", "id", Arrays.asList(idArr));
        mView.showVenuesList(mDao.query(VenuesInfo.class, params));
    }
}
