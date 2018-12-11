package com.expo.contract.presenter;

import com.expo.contract.WebContract;
import com.expo.entity.User;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.RichTextRsp;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class WebPresenterImpl extends WebContract.Presenter {
    public WebPresenterImpl(WebContract.View view) {
        super(view);
    }

    @Override
    public void getUrlById(int urlId) {
        mView.showLoadingView();
        Map<String, Object> params = Http.getBaseParams();
        params.put( "id", urlId );
        RequestBody requestBody = Http.buildRequestBody( params );
        Observable<RichTextRsp> observable = Http.getServer().getRichText( requestBody );
        Http.request( new ResponseCallback<RichTextRsp>() {
            @Override
            protected void onResponse(RichTextRsp rsp) {
                mView.returnRichText( rsp.richText );
            }

            @Override
            public void onComplete() {
                mView.hideLoadingView();
                super.onComplete();
            }
        }, observable );
    }

    @Override
    public void logout() {
        User user = mDao.unique(User.class, null);
        mView.showLoadingView();
        Map<String, Object> params = Http.getBaseParams();
        params.put("Uid", user.getUid());
        params.put("Ukey", user.getUkey());
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<BaseResponse> observable = Http.getServer().userlogout(requestBody);
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
                user.deleteOnDb(mDao);
                mView.logoutResp();
            }

            @Override
            public void onComplete() {
                mView.hideLoadingView();
                super.onComplete();
            }
        }, observable);
    }
}
