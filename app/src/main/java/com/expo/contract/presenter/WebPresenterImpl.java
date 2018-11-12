package com.expo.contract.presenter;

import com.expo.contract.WebContract;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
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
                mView.hideLoadingView();
            }
        }, observable );
    }
}
