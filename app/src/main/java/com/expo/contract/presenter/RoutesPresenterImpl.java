package com.expo.contract.presenter;

import com.expo.contract.RoutesContract;
import com.expo.db.QueryParams;
import com.expo.entity.RouteInfo;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.VerificationCodeResp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class RoutesPresenterImpl extends RoutesContract.Presenter {
    public RoutesPresenterImpl(RoutesContract.View view) {
        super(view);
    }

    @Override
    public void getRoutesData() {
        QueryParams params = new QueryParams();
        mView.freshRoutes(mDao.query(RouteInfo.class, null));
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
}
