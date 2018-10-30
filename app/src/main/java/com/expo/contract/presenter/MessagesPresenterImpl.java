package com.expo.contract.presenter;

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
    public List getData(int type, QueryParams queryParams) {
//        return mDao.query(Message.class, queryParams);

        ///////////////////////////////////////////测试数据
        List list = new ArrayList();
        for (int i = 0; i < 5; i++) {
            Message message = new Message();
            message.setCreateTime("2018-11-13 12:10:10");
            message.setContent("通知：世园会2018-10-10正式开放场馆预约功能；开放的场馆有中国馆国际馆、生活馆、演艺中心。\n");
            if (type == 0) {
                message.setCaption("系统消息");
            } else if (type == 1) {
                message.setCaption("寻求帮助");
            } else if (type == 2) {
                message.setCaption("预约成功");
            }
            list.add(message);
        }
        return list;
    }

}
