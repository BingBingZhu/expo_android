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
        VrInfo vrInfo = mDao.queryById(VrInfo.class, id);
        if (StringUtils.equals(vrInfo.getType(), "0")) {
            vrInfo.setUrl("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        } else {
            vrInfo.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547707751782&di=ffa3185875f6d3be3c94a6811d8f7317&imgtype=0&src=http%3A%2F%2Fpic1.16pic.com%2F00%2F50%2F47%2F16pic_5047893_b.jpg");
        }
        vrInfo.setLinkPanResId("11");
        return vrInfo;
//        return mDao.queryById(VrInfo.class, id);
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
