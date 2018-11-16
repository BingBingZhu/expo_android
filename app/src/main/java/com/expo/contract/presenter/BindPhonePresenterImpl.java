package com.expo.contract.presenter;

import com.expo.base.ExpoApp;
import com.expo.base.utils.PrefsHelper;
import com.expo.contract.BindPhoneContract;
import com.expo.entity.User;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.VerificationCodeResp;
import com.expo.network.response.VerifyCodeLoginResp;
import com.expo.utils.Constants;

import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import io.reactivex.Observable;
import okhttp3.RequestBody;

public class BindPhonePresenterImpl extends BindPhoneContract.Presenter {
    public BindPhonePresenterImpl(BindPhoneContract.View view) {
        super(view);
    }

    @Override
    public void getCode(String mobile, String code) {
        Map<String, Object> params = Http.getBaseParams();
        params.put("Mobile", mobile);
        params.put("countrycode", code);
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
    public void requestThirdLogin(String mobile, String countryCode, String verifyCode, String platform) {
        Platform mPlatform = ShareSDK.getPlatform(platform);
        String gender = "";
        if (platform == null) {
            return;
        }
        gender = mPlatform.getDb().getUserGender();
        if (gender.equals("m")) {
            gender = "0";
        } else {
            gender = "1";
        }
        String thirdtype = "0";
        if (platform.equals("Wechat")) {
            thirdtype = "1";
        } else if (platform.equals("QQ")) {
            thirdtype = "2";
        } else {
            thirdtype = "3";
        }
        // 网络请求登录操作
        Map<String, Object> params = Http.getBaseParams();
        params.put("caption", mPlatform.getDb().getUserName());
        params.put("pic", mPlatform.getDb().getUserIcon());
        params.put("sex", gender);
        params.put("thirdid", mPlatform.getDb().getUserId());
//        mShareParams.put("thirdtype", ShareSDK.getPlatform(mPlatform.getDevinfo(Wechat.NAME)).getSortId());
        params.put("thirdtype", thirdtype);
        params.put("city", "");
        params.put("VerificationCode", verifyCode);
        params.put("countrycode", countryCode);
        params.put("mobile", mobile);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<VerifyCodeLoginResp> verifyCodeLoginObservable = Http.getServer().requestThirdLogin(requestBody);
        Http.request(new ResponseCallback<VerifyCodeLoginResp>() {
            @Override
            protected void onResponse(VerifyCodeLoginResp rsp) {
                setAppUserInfo(rsp);
                PrefsHelper.setString(Constants.Prefs.KEY_COUNTRY_CODE, countryCode);
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
        user.saveOnDb(mDao);
    }

}
