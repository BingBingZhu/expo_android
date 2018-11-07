package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.entity.Message;

import java.util.List;

public interface MessageKindContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void getMessage();

        public abstract void delMessage(Message message, int position);

    }

    interface View extends IView {

        void freshMessageList(List<Message> list);

        void delMessage(int position);
    }
}
