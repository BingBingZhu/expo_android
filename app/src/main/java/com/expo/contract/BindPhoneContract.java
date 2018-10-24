package com.expo.contract;

import android.support.annotation.StringRes;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.network.response.VerifyCodeLoginResp;

public interface BindPhoneContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void getCode(String mobile);

        public abstract void requestThirdLogin(String mobile, String verifyCode);

    }

    interface View extends IView {

        /**
         * 返回验证码的请求结果
         *
         * @param code 验证码
         */
        void returnRequestVerifyCodeResult(String code);

        /**
         * 使用手机号、验证码的方式登录结果
         *
         * @param rsp 请求结果及用户信息
         */
        void verifyCodeLogin(VerifyCodeLoginResp rsp);

        /**
         * 返回第三方登录的取消或错误信息
         *
         * @param resId
         */
        void showShort(@StringRes int resId);
    }
}
