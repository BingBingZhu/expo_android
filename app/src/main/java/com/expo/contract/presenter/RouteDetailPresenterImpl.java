package com.expo.contract.presenter;

import com.expo.contract.RouteDetailContract;
import com.expo.db.QueryParams;
import com.expo.entity.RouteHotInfo;
import com.expo.entity.RouteInfo;
import com.expo.entity.VenuesInfo;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.RouteHotCountResp;
import com.expo.network.response.VerificationCodeResp;

import java.util.Arrays;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class RouteDetailPresenterImpl extends RouteDetailContract.Presenter {

    public RouteDetailPresenterImpl(RouteDetailContract.View view) {
        super(view);
    }

    @Override
    public void getRouteDetail(Long id) {
        QueryParams params = new QueryParams();
        params.add("eq", "_id", id);
        mView.showRouteDetail(mDao.unique(RouteInfo.class, params));
    }

    @Override
    public void getVenuesList(String ids) {
        ids = ids.replace("[", "").replace("]", "");
        String[] idArr = ids.split(",");
        QueryParams params = new QueryParams();
        params.add("in", "_id", Arrays.asList(idArr));
        params.add("and");
        params.add("eq", "is_enable", "1");
        mView.showVenuesList(mDao.query(VenuesInfo.class, params));
    }

}
