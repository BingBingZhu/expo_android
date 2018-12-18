package com.expo.contract.presenter;

import com.expo.R;
import com.expo.base.utils.CheckUtils;
import com.expo.base.utils.ToastHelper;
import com.expo.contract.FeedbackContract;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class FeedbackPresenterImpl extends FeedbackContract.Presenter {
    public FeedbackPresenterImpl(FeedbackContract.View view) {
        super(view);
    }

    @Override
    public void submit(String opter, String email, String content) {
//        if (CheckUtils.isEmtpy(email, R.string.check_string_empty_email, true)) return;
        if (!CheckUtils.isEmail(email, true)) return;
        if (CheckUtils.isEmtpy(content, R.string.check_string_empty_content, true)) return;
        if (content.length() < 10) {
            ToastHelper.showShort(R.string.short_content);
            return;
        }
        mView.showLoadingView();
        Map<String, Object> params = Http.getBaseParams();
        params.put("useropter", opter);
        params.put("email", email);
        params.put("Content", content);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<BaseResponse> observable = Http.getServer().submitFeedback(requestBody);
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
                mView.submitComplete(rsp.msg);
            }

            @Override
            public void onComplete() {
                mView.hideLoadingView();
                super.onComplete();
            }
        }, observable);
    }
}
