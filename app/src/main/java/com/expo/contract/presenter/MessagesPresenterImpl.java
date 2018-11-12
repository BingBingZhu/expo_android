package com.expo.contract.presenter;

import com.expo.base.ExpoApp;
import com.expo.contract.MessagesContract;
import com.expo.db.QueryParams;
import com.expo.entity.Message;

import java.util.ArrayList;
import java.util.List;

public class MessagesPresenterImpl extends MessagesContract.Presenter {
    public MessagesPresenterImpl(MessagesContract.View view) {
        super(view);
    }

    @Override
    public void getMessage(String type) {
        QueryParams params = new QueryParams()
                .add("eq", "type", type)
                .add("and")
                .add("eq", "uid", ExpoApp.getApplication().getUser().getUid())
                .add("orderBy", "create_time", true);
        mView.freshMessageList(mDao.query(Message.class, params));
    }

    @Override
    public void delMessage(long id, int position) {
        mDao.delete(Message.class, id);
        mView.delMessage(position);
    }

}
