package com.expo.module.heart.message;

import android.content.Context;

import com.expo.R;
import com.expo.entity.Message;

public class MessageTypeAppointment implements MessageTypeInterface{
    @Override
    public int getTitle() {
        return R.string.title_message_appointment_ac;
    }

    @Override
    public int getItemRes() {
        return R.layout.item_message_appointment;
    }

    @Override
    public void itemClick(Context context, Message message) {

    }
}
