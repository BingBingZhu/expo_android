package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;

public interface MessagesContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

    }

    interface View extends IView {

    }
}
