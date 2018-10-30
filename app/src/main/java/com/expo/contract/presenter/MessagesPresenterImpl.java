package com.expo.contract.presenter;

import com.expo.contract.MessagesContract;
import com.expo.db.QueryParams;
import com.expo.entity.Message;
import com.expo.module.heart.message.MessageAppointment;
import com.expo.module.heart.message.MessageService;
import com.expo.module.heart.message.MessageTourist;

import java.util.List;

public class MessagesPresenterImpl extends MessagesContract.Presenter {
    public MessagesPresenterImpl(MessagesContract.View view) {
        super(view);
    }

    @Override
    public void setAdaptor(int type) {
        switch (type) {
            case 0:
                mView.setAdaptor(new MessageService());
                break;
            case 1:
                mView.setAdaptor(new MessageTourist());
                break;
            case 2:
                mView.setAdaptor(new MessageAppointment());
                break;
        }
    }

    @Override
    public List getData(int type, QueryParams queryParams) {
        return mDao.query(Message.class, queryParams);
    }

}
