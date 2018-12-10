package com.expo.contract.presenter;

import com.expo.base.utils.PrefsHelper;
import com.expo.contract.FindListContract;
import com.expo.contract.ListContract;
import com.expo.db.QueryParams;
import com.expo.entity.Encyclopedias;
import com.expo.entity.Find;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.SocietyListResp;
import com.expo.network.response.VerificationCodeResp;
import com.expo.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class FindListPresenterImpl extends FindListContract.Presenter {

    private static final int PER_PAGE_COUNT = 10;

    public FindListPresenterImpl(FindListContract.View view) {
        super(view);
    }

    @Override
    public void getSocietyListFilter(int page, boolean fresh) {
        mView.showLoadingView();
        Map<String, Object> params = Http.getBaseParams();
        params.put("Count", 10);
        params.put("Pageidx", page);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<SocietyListResp> observable = Http.getServer().getSocietyListFilter(requestBody);
        Http.request(new ResponseCallback<SocietyListResp>() {
            @Override
            protected void onResponse(SocietyListResp rsp) {
                mView.hideLoadingView();
                mView.addFindList(rsp.finds, fresh);
            }
        }, observable);

    }

}
