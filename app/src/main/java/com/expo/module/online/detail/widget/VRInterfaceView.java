package com.expo.module.online.detail.widget;

import android.view.View;

import com.expo.entity.VrInfo;

public interface VRInterfaceView {

    public View getVrVideoView();

    public void setVrInfo(VrInfo vrInfo);

    public void showVrSceen();

    public void onDestroy();

}
