package com.expo.module.heart.message;

import android.content.Context;

import com.expo.R;
import com.expo.entity.Message;

public class MessageTypeTourist implements MessageTypeInterface{
    @Override
    public int getTitle() {
        return R.string.title_message_tourist_ac;
    }

    @Override
    public int getItemRes() {
        return R.layout.item_message_tourist;
    }

    @Override
    public void itemClick(Context context, Message message) {

    }
}
