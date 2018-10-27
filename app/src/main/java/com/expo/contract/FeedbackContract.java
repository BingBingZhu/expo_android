package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;

public interface FeedbackContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super( view );
        }

        public abstract void submit(String opter, String email, String content);

    }

    interface View extends IView {

        void submitComplete(String string);

    }
}
