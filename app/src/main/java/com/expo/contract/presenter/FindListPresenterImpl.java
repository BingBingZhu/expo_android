package com.expo.contract.presenter;

import com.expo.contract.FindListContract;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.SocietyListResp;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class FindListPresenterImpl extends FindListContract.Presenter {

    private static final int PER_PAGE_COUNT = 10;

    public FindListPresenterImpl(FindListContract.View view) {
        super(view);
    }

    @Override
    public void getSocietyListFilter(int page, String type, boolean fresh) {
        Map<String, Object> params = Http.getBaseParams();
        params.put("Count", 10);
        params.put("Pageidx", page);
        params.put("typename", type);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<SocietyListResp> observable = Http.getServer().getSocietyListFilter(requestBody);
        Http.request(new ResponseCallback<SocietyListResp>() {
            @Override
            protected void onResponse(SocietyListResp rsp) {
                mView.addFindList(rsp.finds, fresh);
            }

            @Override
            public void onComplete() {
                super.onComplete();
            }
        }, observable);

    }

}
