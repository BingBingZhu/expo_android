package com.expo.contract.presenter;

import com.expo.contract.ExamineContract;
import com.expo.contract.RoutesContract;
import com.expo.db.QueryParams;
import com.expo.entity.RouteHotInfo;
import com.expo.entity.RouteInfo;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.RouteHotCountResp;
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

}
