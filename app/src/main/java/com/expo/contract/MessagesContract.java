package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.db.QueryParams;

import java.util.List;

public interface MessagesContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract List getData(int type, QueryParams queryParams);

    }

    interface View extends IView {

    }
}