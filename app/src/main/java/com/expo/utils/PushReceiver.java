package com.expo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.expo.network.Http;
import com.expo.network.ResponseCallback;
import com.expo.network.response.BaseResponse;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;

public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String jpushId = JPushInterface.getRegistrationID(context);
        if (!TextUtils.isEmpty(jpushId)) {
            Map<String, Object> params = Http.getBaseParams();
            params.put("jgid", jpushId);
            Observable<BaseResponse> observable = Http.getServer().userlogAppRun(Http.buildRequestBody(params));
            Http.request(new ResponseCallback<BaseResponse>() {
                @Override
                protected void onResponse(BaseResponse rsp) {
                }
            }, observable);
        }
    }
}
