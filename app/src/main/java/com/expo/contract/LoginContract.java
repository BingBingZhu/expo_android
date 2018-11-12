package com.expo.contract;

import android.support.annotation.StringRes;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.CommonInfo;
import com.expo.network.response.VerifyCodeLoginResp;

public interface LoginContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract void getCode(String mobile, String countryCeod, String guestId);

        public abstract void verifyCodeLogin(String mobile, String countryCode, String verifyCode);

        public abstract void threeLogin(String name);

        public abstract void loadUserProtocol();
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
         */
        void verifyCodeLogin();

        /**
         * 使用验证第三方openid是否已注册
         *
         * @param platform 第三方登录的信息
         */
        void toBindPhone(String platform);

        /**
         * 跳转用户协议页面
         *
         * @param info
         */
        void toUserProtocol(CommonInfo info);

        /**
         * 返回第三方登录的取消或错误信息
         *
         * @param resId
         */
        void showShort(@StringRes int resId);
    }
}
