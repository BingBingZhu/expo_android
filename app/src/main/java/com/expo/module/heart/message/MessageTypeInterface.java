package com.expo.module.heart.message;

import android.content.Context;

import com.expo.entity.Message;

public interface MessageTypeInterface {

    public int getTitle();

    public int getItemRes();

    public void itemClick(Context context, Message message);
}
