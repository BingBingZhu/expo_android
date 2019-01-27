package com.expo.contract.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.expo.base.utils.PrefsHelper;
import com.expo.contract.VRDetailContract;
import com.expo.db.QueryParams;
import com.expo.entity.RouteInfo;
import com.expo.entity.VrInfo;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.VerificationCodeResp;
import com.expo.utils.Constants;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class VRDetailPresenterImpl extends VRDetailContract.Presenter {

    public VRDetailPresenterImpl(VRDetailContract.View view) {
        super(view);
    }

    @Override
    public VrInfo getVrInfo(String id) {
        return mDao.queryById(VrInfo.class, id);
    }

    @Override
    public void setPanResViews(Long id) {
        Map<String, Object> params = Http.getBaseParams();
        params.put( "id", id );
        RequestBody requestBody = Http.buildRequestBody( params );
        Observable<BaseResponse> observable = Http.getServer().setPanResViews( requestBody );
        Http.request( new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
            }

        }, observable );
    }

}
