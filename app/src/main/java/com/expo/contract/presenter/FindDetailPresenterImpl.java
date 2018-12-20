package com.expo.contract.presenter;

import com.expo.contract.FindDetailContract;
import com.expo.entity.Find;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.SocietyListResp;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class FindDetailPresenterImpl extends FindDetailContract.Presenter {
    public FindDetailPresenterImpl(FindDetailContract.View view) {
        super(view);
    }

    @Override
    public void addEnjoy(String id) {
        mView.showLoadingView();
        Map<String, Object> params = Http.getBaseParams();
        params.put("id", id);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<BaseResponse> observable = Http.getServer().setEnjoySociety(requestBody);
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
                mView.hideLoadingView();
                mView.addEnjoyRes();
            }

            @Override
            public void onComplete() {
                mView.hideLoadingView();
                super.onComplete();
            }
        }, observable);
    }

    @Override
    public void addViews(String id) {
        Map<String, Object> params = Http.getBaseParams();
        params.put("id", id);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<BaseResponse> observable = Http.getServer().setSocietyViews(requestBody);
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
            }
        }, observable);
    }

}
