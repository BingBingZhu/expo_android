package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.User;

public interface MineContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );

        }

        public abstract void loadUser();

        public abstract String loadCommonInfo(String s);

    }

    interface View extends IView {
        void freshUser(User user);
    }
}
