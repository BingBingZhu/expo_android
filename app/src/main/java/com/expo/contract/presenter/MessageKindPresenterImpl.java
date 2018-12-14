package com.expo.contract.presenter;

import com.expo.R;
import com.expo.base.ExpoApp;
import com.expo.contract.MessageKindContract;
import com.expo.db.QueryParams;
import com.expo.entity.Message;

import java.util.Collections;
import java.util.List;

public class MessageKindPresenterImpl extends MessageKindContract.Presenter {
    public MessageKindPresenterImpl(MessageKindContract.View view) {
        super(view);
    }

    @Override
    public void getMessage() {
        QueryParams params = new QueryParams()
                .add("eq", "uid", ExpoApp.getApplication().getUser().getUid())
                .add("groupBy", "type")
                .add("orderBy", "create_time", true);
        List<Message> list = mDao.query(Message.class, params);
        int sum = 0;
        if (list != null)
            for (int i = 0; i < list.size(); i++) {
                sum += 1 << Integer.valueOf(list.get(i).getType().replace(" ", ""));
            }
        for (int i = 0; i < 6; i++) {
            if (i != 1 && i != 4 && i != 5) continue;
            if ((sum & (1 << i)) == 0) {
                list.add(getDefaultMessage(i));
            }
        }
        Collections.sort(list, (o1, o2) -> o1.getType().compareTo(o2.getType()));
        list.add(list.get(0));
        list.remove(0);
        mView.freshMessageList(list);
    }

    @Override
    public void delMessage(Message message, int position) {
        message.delMessage(ExpoApp.getApplication().getUser().getUid());
        mView.delMessage(position);
    }

    private Message getDefaultMessage(int type) {
        Message message = new Message();
        message.setType(type + "");
        switch (type) {
            case 1:
                initMessage(message, R.string.message_kind_system_title, R.string.message_kind_system_content);
                break;
            case 4:
                initMessage(message, R.string.message_kind_tourist_title, R.string.message_kind_tourist_content);
                break;
            case 5:
                initMessage(message, R.string.message_kind_appointment_title, R.string.message_kind_appointment_content);
                break;
        }
        return message;
    }

    private void initMessage(Message message, int caption, int content) {
        message.setCaption(ExpoApp.getApplication().getString(caption));
        message.setCaptionEn(ExpoApp.getApplication().getString(caption));
        message.setContent(ExpoApp.getApplication().getString(content));
        message.setContentEn(ExpoApp.getApplication().getString(content));
    }
}
