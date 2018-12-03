package com.expo.module.heart.message;

import com.expo.R;

public class MessageTypeAppointment extends MessageType {
    @Override
    public int getTitle() {
        return R.string.title_message_appointment_ac;
    }

    @Override
    public int getItemRes() {
        return R.layout.item_message_appointment;
    }

}
