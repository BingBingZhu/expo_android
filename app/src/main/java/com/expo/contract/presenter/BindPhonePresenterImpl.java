package com.expo.contract.presenter;

import com.expo.base.ExpoApp;
import com.expo.contract.BindPhoneContract;
import com.expo.entity.User;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.VerificationCodeResp;
import com.expo.network.response.VerifyCodeLoginResp;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public class BindPhonePresenterImpl extends BindPhoneContract.Presenter {
    public BindPhonePresenterImpl(BindPhoneContract.View view) {
        super(view);
    }

    @Override
    public void getCode(String mobile) {
        Map<String, Object> params = Http.getBaseParams();
        params.put("Mobile", mobile);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<VerificationCodeResp> observable = Http.getServer().getVerificationCode(requestBody);
        Http.request(new ResponseCallback<VerificationCodeResp>() {
            @Override
            protected void onResponse(VerificationCodeResp rsp) {
                mView.returnRequestVerifyCodeResult(rsp.verificationCode);
            }
        }, observable);
    }

    @Override
    public void requestThirdLogin(String mobile, String verifyCode) {
        Map<String, Object> params = Http.getBaseParams();
        params.put("Mobile", mobile);
        params.put("VerifyCode", verifyCode);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<VerifyCodeLoginResp> verifyCodeLoginObservable = Http.getServer().verifyCodeLogin(requestBody);
        Http.request(new ResponseCallback<VerifyCodeLoginResp>() {
            @Override
            protected void onResponse(VerifyCodeLoginResp rsp) {
                setAppUserInfo(rsp);
                mView.verifyCodeLogin(rsp);
            }
        }, verifyCodeLoginObservable);
    }

    private void setAppUserInfo(VerifyCodeLoginResp rsp) {
        User user = new User();
        user.setNick(rsp.getCaption());
        user.setCity(rsp.getCity());
        user.setUid(rsp.getId());
        user.setUkey(rsp.getKey());
        user.setMobile(rsp.getMobile());
        user.setPhotoUrl(rsp.getPhotoUrl());
        user.setSex(rsp.getSex());
        ExpoApp.getApplication().setUser(user);
        mDao.clear(User.class);
        mDao.saveOrUpdate(user);
    }

}
