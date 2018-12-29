package com.expo.pay;

import com.expo.utils.NotificationUtil;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RegisterCallback implements Observer<HashMap<String, String>> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(HashMap<String, String> result) {
        if ("0".equals( result.get( "code" ) )) {
            NotificationUtil.getInstance().showNotification( "网站购票登录密码", "你的官网购票登录的默认密码是" + result.get( "data" ), null );
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
