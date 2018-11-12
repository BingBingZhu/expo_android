package com.expo.contract.presenter;

import android.os.Handler;
import android.os.Message;

import com.expo.R;
import com.expo.base.ExpoApp;
import com.expo.base.utils.PrefsHelper;
import com.expo.contract.LoginContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;
import com.expo.entity.User;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.CheckThirdIdRegisterStateResp;
import com.expo.network.response.VerificationCodeResp;
import com.expo.network.response.VerifyCodeLoginResp;
import com.expo.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import io.reactivex.Observable;
import okhttp3.RequestBody;

public class LoginPresenterImpl extends LoginContract.Presenter implements PlatformActionListener {
    public LoginPresenterImpl(LoginContract.View view) {
        super(view);
    }

    @Override
    public void getCode(String mobile, String countryCode, String guestId) {
        mView.showLoadingView();
        Map<String, Object> params = Http.getBaseParams();
        params.put("Mobile", mobile);
        params.put("countrycode", countryCode);
        params.put("guestid", guestId);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<VerificationCodeResp> observable = Http.getServer().getVerificationCode(requestBody);
        Http.request(new ResponseCallback<VerificationCodeResp>() {
            @Override
            protected void onResponse(VerificationCodeResp rsp) {
                mView.returnRequestVerifyCodeResult(rsp.verificationCode);
                PrefsHelper.setString(Constants.Prefs.KEY_COUNTRY_CODE, countryCode);
                mView.hideLoadingView();
            }
        }, observable);
    }

    @Override
    public void verifyCodeLogin(String mobile, String countryCode, String verifyCode) {
        Map<String, Object> params = Http.getBaseParams();
        params.put("Mobile", mobile);
        params.put("countrycode", countryCode);
        params.put("VerifyCode", verifyCode);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<VerifyCodeLoginResp> verifyCodeLoginObservable = Http.getServer().verifyCodeLogin(requestBody);
        Http.request(new ResponseCallback<VerifyCodeLoginResp>() {
            @Override
            protected void onResponse(VerifyCodeLoginResp rsp) {
                setAppUserInfo(rsp);
                mView.verifyCodeLogin();
            }
        }, verifyCodeLoginObservable);
    }

    /**
     * 第三方登录授权
     *
     * @param name
     */
    @Override
    public void threeLogin(String name) {
        Platform plat = ShareSDK.getPlatform(name);
        if (plat == null) {
            return;
        }
        plat.setPlatformActionListener(this);
        //关闭SSO授权
//        plat.SSOSetting(true);
        plat.showUser(null);
    }

    /**
     * 三方登录回调
     * onCancel  onComplete  onError
     */
    @Override
    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = new Object[]{platform.getName(), res};
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onError(Platform arg0, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_ERROR);
        }
        t.printStackTrace();
    }

    private static final int MSG_AUTH_CANCEL = 1;
    private static final int MSG_AUTH_ERROR = 2;
    private static final int MSG_AUTH_COMPLETE = 3;
    private Handler handler = new Handler(msg -> {
        switch (msg.what) {
            case MSG_AUTH_CANCEL:
                //取消授权
                mView.showShort(R.string.cancel_the_login);
                break;
            case MSG_AUTH_ERROR:
                //授权失败
                mView.showShort(R.string.authorization_failure);
                break;
            case MSG_AUTH_COMPLETE:
                //授权成功
                mView.showShort(R.string.authorization_success);
                Object[] objs = (Object[]) msg.obj;
                String platform = (String) objs[0];
                verifyThirdState(platform);
                break;
        }
        return true;
    });

    private void verifyThirdState(String platform) {
        Platform mPlatform = ShareSDK.getPlatform(platform);
        if (platform == null) {
            return;
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
        params.put("Type", "1");
        params.put("thirdid", mPlatform.getDb().getUserId());
        params.put("thirdtype", thirdtype);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<CheckThirdIdRegisterStateResp> verifyCodeLoginObservable = Http.getServer().checkThirdIdRegisterState(requestBody);
        Http.request(new ResponseCallback<CheckThirdIdRegisterStateResp>() {
            @Override
            protected void onResponse(CheckThirdIdRegisterStateResp rsp) {
                if (rsp.state == 0)//未注册过
                    mView.toBindPhone(platform);
                else if (rsp.state == 1)//已注册过
                    doThreeLogined(rsp.uid, rsp.ukey);
            }
        }, verifyCodeLoginObservable);
    }

    private void doThreeLogined(String uid, String ukey) {
        // 网络请求登录操作
        Map<String, Object> params = Http.getBaseParams();
        params.put("Type", "1");
        params.put("Uid", uid);
        params.put("Ukey", ukey);
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<User> verifyCodeLoginObservable = Http.getServer().loadUserInfo(requestBody);
        Http.request(new ResponseCallback<User>() {
            @Override
            protected void onResponse(User rsp) {
//                setAppUserInfo(rsp);
//                mView.verifyCodeLogin(rsp);
                rsp.setUid(uid);
                rsp.setUkey(ukey);
                rsp.saveOnDb(mDao);
                mView.verifyCodeLogin();
            }
        }, verifyCodeLoginObservable);
    }

    @Override
    public void loadUserProtocol() {
        CommonInfo info = mDao.unique(CommonInfo.class, new QueryParams().add("eq", "type", "1"));
        mView.toUserProtocol(info);
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
