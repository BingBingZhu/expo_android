package com.expo.module.map;

import com.expo.entity.Venue;
import com.facebook.drawee.view.SimpleDraweeView;

public interface InfoWindowListener {

    void onToInfo(Venue venue);

    void onNavigation(Venue venue);

    void onPlayVoice(Venue venue);

    void onStopPlay(Venue venue);

    void onSetPic(Venue venue, SimpleDraweeView simpleDraweeView);

}
