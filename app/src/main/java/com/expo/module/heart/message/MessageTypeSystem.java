package com.expo.module.heart.message;

import android.content.Context;

import com.expo.R;
import com.expo.entity.Message;

public class MessageTypeSystem extends MessageType {
    @Override
    public int getTitle() {
        return R.string.title_message_system_ac;
    }

    @Override
    public int getItemRes() {
        return R.layout.item_message_system;
    }

    @Override
    public void itemClick(Context context, Message message) {
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
