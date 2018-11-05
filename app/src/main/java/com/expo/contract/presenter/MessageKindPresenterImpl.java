package com.expo.contract.presenter;

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
                .add("eq", "msg_kind", "3")
                .add("groupBy", "type", true)
                .add("orderBy", "create_time", true);
        mView.freshMessageList(mDao.query(Message.class, params));
    }

    @Override
    public void delMessage(long id, int position) {
        mDao.delete(Message.class, id);
        mView.delMessage(position);
    }
}
