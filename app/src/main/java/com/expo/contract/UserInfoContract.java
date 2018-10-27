package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.User;

public interface UserInfoContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void loadUser();

        public abstract void saveUserInfo(boolean changeImg, User user);

        public abstract void setAge(String birthDay);

    }

    interface View extends IView {

        public void refreshUserInfo(User user);

        public void changeAge(String birthDay, String age);

        public void saveUserInfo();

    }
}
