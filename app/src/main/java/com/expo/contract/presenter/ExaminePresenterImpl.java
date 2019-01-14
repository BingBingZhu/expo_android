package com.expo.contract.presenter;

import com.expo.contract.ExamineContract;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.SocietyListResp;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class ExaminePresenterImpl extends ExamineContract.Presenter {
    public ExaminePresenterImpl(ExamineContract.View view) {
        super(view);
    }

    @Override
    public void getMySocietyList(boolean isPass) {
        mView.showLoadingView();
        Map<String, Object> params = Http.getBaseParams();
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<SocietyListResp> observable = Http.getServer().getMySocietyList(requestBody);
        Http.request(new ResponseCallback<SocietyListResp>() {
            @Override
            protected void onResponse(SocietyListResp rsp) {
                for (int i = rsp.finds.size() - 1; i >= 0; i--) {
                    if (isPass && rsp.finds.get(i).state != 1) {
                        rsp.finds.remove(i);
                    } else if (!isPass && rsp.finds.get(i).state == 1) {
                        rsp.finds.remove(i);
                    }
                }
                mView.freshFind(rsp.finds);
            }

            @Override
            public void onComplete() {
                mView.hideLoadingView();
                super.onComplete();
            }
        }, observable);

    }

    @Override
    public void deleteSociety(int id, int type, int position) {
        mView.showLoadingView();
        Map<String, Object> params = Http.getBaseParams();
        params.put("Societyid", id);
        params.put("kind", type);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<BaseResponse> observable = Http.getServer().deleteSociety(requestBody);
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
                mView.deleteSociety(position);
            }

            @Override
            public void onComplete() {
                mView.hideLoadingView();
                super.onComplete();
            }
        }, observable);
    }

}
