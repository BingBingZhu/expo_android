package com.expo.module.heart.message;

import com.expo.R;

public class MessageTypeTourist extends MessageType {
    @Override
    public int getTitle() {
        return R.string.title_message_tourist_ac;
    }

    @Override
    public int getItemRes() {
        return R.layout.item_message_tourist;
    }

}
