package com.expo.contract.presenter;

import com.expo.base.ExpoApp;
import com.expo.contract.MessagesContract;
import com.expo.db.QueryParams;
import com.expo.entity.CommonInfo;
import com.expo.entity.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesPresenterImpl extends MessagesContract.Presenter {
    public MessagesPresenterImpl(MessagesContract.View view) {
        super( view );
    }

    @Override
    public void getMessage(String type) {
        QueryParams params = new QueryParams()
                .add( "eq", "type", type )
                .add( "and" )
                .add( "eq", "uid", ExpoApp.getApplication().getUser().getUid() )
                .add( "orderBy", "create_time", "desc" );
        List<Message> messages = mDao.query( Message.class, params );
        if (type.equals( "4" )) {      // 加入日期分组数据
            Map<String, List<Message>> map = new HashMap<>();
            List<Message> itemMessages = new ArrayList<>();
            String messageDate = "";
            for (int i = 0; i < messages.size(); i++) {
                if (messageDate.equals( "" ) || messageDate.equals( messages.get( i ).getCreateTime().split( " " )[0] )) {
                    itemMessages.add( messages.get( i ) );
                } else {
                    map.put( messageDate, itemMessages );
                    itemMessages = new ArrayList<>();
                    itemMessages.add( messages.get( i ) );
                }
                if (i == messages.size() - 1) {
                    map.put( messages.get( i ).getCreateTime().split( " " )[0], itemMessages );
                }
                messageDate = messages.get( i ).getCreateTime().split( " " )[0];
            }
            messages = new ArrayList<>();
            Message message = new Message();
            for (String key : map.keySet()) {
                message.setCreateTime( key );
                message.setRead( true );
                messages.add( message );
                for (Message msg : map.get( key )) {
                    messages.add( msg );
                }
            }
        }
        mView.freshMessageList( messages );
    }

    @Override
    public void delMessage(long id, int position) {
        mDao.delete( Message.class, id );
        mView.delMessage( position );
    }

    @Override
    public void setMessageRead(Message message) {
        mDao.saveOrUpdate( message );
    }

    @Override
    public String loadCommonInfo(String type) {
        CommonInfo info = mDao.unique( CommonInfo.class, new QueryParams()
                .add( "eq", "type", type ) );
        if (info != null) {
            return info.getLinkUrl();
        }
        return null;
    }
}
