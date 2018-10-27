package com.expo.contract.presenter;

import com.expo.contract.SettingContract;
import com.expo.entity.DownloadInfo;
import com.expo.entity.User;
import com.expo.module.download.DownloadManager;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.VersionInfoResp;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class SettingPresenterImpl extends SettingContract.Presenter {
    public SettingPresenterImpl(SettingContract.View view) {
        super(view);
    }

    @Override
    public void clearCache() {

    }

    @Override
    public void checkUpdate() {
        mView.showLoadingView();
        Map<String, Object> params = Http.getBaseParams();
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<VersionInfoResp> observable = Http.getServer().getAppUpdateInfo(requestBody);
        Http.request(new ResponseCallback<VersionInfoResp>() {
            @Override
            protected void onResponse(VersionInfoResp rsp) {
                mView.appUpdate(rsp);
            }
        }, observable);
    }

    @Override
    public void update() {
        //调用下载功能
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
                mView.hideLoadingView();
                user.deleteOnDb(mDao);
                mView.logout();
            }
        }, observable);
    }
}
