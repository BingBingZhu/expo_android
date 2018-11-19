package com.expo.contract.presenter;

import com.expo.base.ExpoApp;
import com.expo.contract.MessageKindContract;
import com.expo.db.QueryParams;
import com.expo.entity.Message;

public class MessageKindPresenterImpl extends MessageKindContract.Presenter {
    public MessageKindPresenterImpl(MessageKindContract.View view) {
        super(view);
    }

    @Override
    public void getMessage() {
        QueryParams params = new QueryParams()
                .add("eq", "uid", ExpoApp.getApplication().getUser().getUid())
                .add("groupBy", "type", true)
                .add("orderBy", "create_time", true);
        mView.freshMessageList(mDao.query(Message.class, params));
    }

    @Override
    public void delMessage(Message message, int position) {
        message.delMessage(ExpoApp.getApplication().getUser().getUid());
        mView.delMessage(position);
    }
}
