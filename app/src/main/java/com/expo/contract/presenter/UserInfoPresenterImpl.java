package com.expo.contract.presenter;

import android.content.Context;
import android.support.v4.math.MathUtils;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.expo.base.ExpoApp;
import com.expo.contract.UserInfoContract;
import com.expo.entity.User;
import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;
import com.expo.network.response.UploadRsp;
import com.expo.utils.Constants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserInfoPresenterImpl extends UserInfoContract.Presenter {
    public UserInfoPresenterImpl(UserInfoContract.View view) {
        super(view);
    }

    @Override
    public void loadUser() {
        User user = mDao.unique(User.class, null);
        mView.refreshUserInfo(user);
        Map<String, Object> params = Http.getBaseParams();
        params.put("Type", "1");
        params.put("Uid", user.getUid());
        params.put("Ukey", user.getUkey());
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<User> verifyCodeLoginObservable = Http.getServer().loadUserInfo(requestBody);
        Http.request(new ResponseCallback<User>() {
            @Override
            protected void onResponse(User rsp) {
                freshUserInfo(user, rsp);
                user.saveOnDb(mDao);
                mView.refreshUserInfo(user);
            }
        }, verifyCodeLoginObservable);
    }

    @Override
    public void saveUserInfo(boolean changeImg, User user) {
        mView.showLoadingView();
        uploadImg(changeImg, user);
    }

    @Override
    public void setAge(String birthDay) {
        if (!StringUtils.isEmpty(birthDay)) {
            SimpleDateFormat format = new SimpleDateFormat(Constants.TimeFormat.TYPE_SIMPLE);
            long mills = TimeUtils.string2Millis(birthDay, format);
            String birYear = TimeUtils.millis2String(mills, new SimpleDateFormat(Constants.TimeFormat.TYPE_YEAR));
            String year = TimeUtils.getNowString(new SimpleDateFormat(Constants.TimeFormat.TYPE_YEAR));
            String age = Integer.valueOf(year) - Integer.valueOf(birYear) + "";
            mView.changeAge(birthDay, age);
        } else {
            mView.changeAge(birthDay, "");
        }
    }

    /**
     * @param user  被刷新user
     * @param user2 user数据
     */
    public void freshUserInfo(User user, User user2) {
        user.setPhotoUrl(user2.getPhotoUrl());
        user.setNick(user2.getNick());
        user.setSex(user2.getSex());
        user.setBirthDay(user2.getBirthDay());
    }

    /**
     * 上传头像
     */
    public void uploadImg(boolean changeImg, User user) {
        if (!changeImg) saveUserInfoOnLine(user);
        else if (StringUtils.isEmpty(user.getPhotoUrl()))
            saveUserInfoOnLine(user);
        else if (user.getPhotoUrl().toLowerCase().startsWith("http"))
            saveUserInfoOnLine(user);
        else {
            File file = new File(user.getPhotoUrl());
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data;"), file);

            Observable<UploadRsp> verifyCodeLoginObservable = Http.getServer().uploadFile(MultipartBody.Part.createFormData("file", "image.png", requestBody));
            Http.request(new ResponseCallback<UploadRsp>() {
                @Override
                protected void onResponse(UploadRsp rsp) {
                    user.setPhotoUrl(rsp.fullUrl);
                    saveUserInfoOnLine(user);
                }
            }, verifyCodeLoginObservable);
        }

    }

    public void saveUserInfoOnLine(User user) {
        Map<String, Object> params = Http.getBaseParams();
        params.put("Uid", user.getUid());
        params.put("Ukey", user.getUkey());
        params.put("birthday", user.getBirthDay());
        params.put("caption", user.getNick());
        params.put("picUrl", user.getPhotoUrl());
        params.put("sex", user.getSex());
        RequestBody requestBody = Http.buildRequestBody(params);
        Observable<BaseResponse> verifyCodeLoginObservable = Http.getServer().setUserInfo(requestBody);
        Http.request(new ResponseCallback<BaseResponse>() {
            @Override
            protected void onResponse(BaseResponse rsp) {
                user.saveOnDb(mDao);
                mView.saveUserInfo();
                mView.hideLoadingView();
            }
        }, verifyCodeLoginObservable);
    }

}
