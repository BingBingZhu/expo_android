package com.expo.contract;

import com.expo.base.IPresenter;
import com.expo.base.IView;
import com.expo.db.QueryParams;
import com.expo.entity.Message;

import java.util.List;

public interface MessagesContract {
    abstract class Presenter extends IPresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void getMessage(String type);

        public abstract void delMessage(long id, int position);

        public abstract void setMessageRead(Message message);

        public abstract String loadCommonInfo(String type);

        public abstract void getOrderInfo(String linkId);
    }

    interface View extends IView {
        void freshMessageList(List<Message> list);

        void delMessage(int position);

        void gotoInfoPage(String data);
    }
}
