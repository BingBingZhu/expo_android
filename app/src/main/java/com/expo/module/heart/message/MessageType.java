package com.expo.module.heart.message;

import android.content.Context;

import com.expo.entity.Message;
import com.expo.module.webview.WebTemplateActivity;

public abstract class MessageType {

    public abstract int getTitle();

    public abstract int getItemRes();

    public void itemClick(Context context, Message message) {
        WebTemplateActivity.startActivity(context, message.getId());
//        WebTemplateActivity.startActivity(context, LanguageUtil.chooseTest(message.getLinkId(), message.getLinkIdEn()));
//        WebTemplateActivity.startActivity(context, LanguageUtil.chooseTest(message.getLinkId(), message.getLinkIdEn()), LanguageUtil.chooseTest(message.getCaption(), message.getCaptionEn()));
    }

    public abstract int getMenuIcon();

    public abstract int getMenuBackground();
}
