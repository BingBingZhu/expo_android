package com.expo.module.heart.message;

import com.expo.R;

public class MessageTypeActivity extends MessageType {
    @Override
    public int getTitle() {
        return R.string.title_message_activity_ac;
    }

    @Override
    public int getItemRes() {
        return R.layout.item_message_appointment;
    }

    @Override
    public int getMenuIcon() {
        return R.mipmap.delete;
    }

    @Override
    public int getMenuBackground() {
        return R.color.red_f7463f;
    }

}
