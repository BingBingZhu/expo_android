package com.expo.contract.presenter;

import com.expo.contract.ExamineContract;
import com.expo.contract.RoutesContract;
import com.expo.db.QueryParams;
import com.expo.entity.RouteHotInfo;
import com.expo.entity.RouteInfo;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.RouteHotCountResp;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class ExaminePresenterImpl extends ExamineContract.Presenter {
    public ExaminePresenterImpl(ExamineContract.View view) {
        super(view);
    }

    @Override
    public void getRoutesData() {
        QueryParams params = new QueryParams()
                .add("eq", "enable", "1")
                .add("and")
                .add("eq", "type_id", "1");
        mView.freshRoutes(mDao.query(RouteInfo.class, params));
    }

    @Override
    public void clickRoute(String id) {
        Map<String, Object> params = Http.getBaseParams();
        params.put("routeid", id);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<BaseResponse> observable = Http.getServer().addRouterHotClick(requestBody);
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
            }
        }, observable);
    }

    @Override
    public void getRouterHotCount() {
        Map<String, Object> params = Http.getBaseParams();
        params.put("parkid", "1");
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<RouteHotCountResp> observable = Http.getServer().getRouterHotCountList(requestBody);
        Http.request(new ResponseCallback<RouteHotCountResp>() {
            @Override
            protected void onResponse(RouteHotCountResp rsp) {
                if (rsp == null || rsp.routeHots == null || rsp.routeHots.size() == 0) return;
                for (int i = 0; i < rsp.routeHots.size(); i++) {
                    RouteHotInfo hotInfo = rsp.routeHots.get(i);
                    RouteInfo routeInfo = mDao.queryById(RouteInfo.class, hotInfo.element);
                    if (routeInfo != null) {
                        routeInfo.hotCount = hotInfo.score;
                        mDao.saveOrUpdate(routeInfo);
                    }
                }
                getRoutesData();
            }
        }, observable);
    }
}
