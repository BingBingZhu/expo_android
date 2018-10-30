package com.expo.contract;

import android.support.v7.widget.RecyclerView;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.db.QueryParams;
import com.expo.module.heart.message.MessageInterface;

import java.util.List;

public interface MessagesContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void setAdaptor(int type);

        public abstract List getData(int type, QueryParams queryParams);

    }

    interface View extends IView {

        void setAdaptor(MessageInterface mi);

    }
}
