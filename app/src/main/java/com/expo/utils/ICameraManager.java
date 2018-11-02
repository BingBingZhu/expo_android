package com.expo.utils;

import android.hardware.Camera;
import android.view.SurfaceView;
import android.view.TextureView;

public interface ICameraManager {
    void startPreview();

    void stopPreview();

    void toggleBackOrFront();

    void setFacing(boolean facing);

    boolean haveFrontCamera();

    void release();

    void turnOnFlashLamp(boolean on);

    void setDisplayView(TextureView displayView);

    void setDisplayView(SurfaceView displayView);

    void setPreviewCallback(Camera.PreviewCallback callback);

    void tackPicture(Camera.PictureCallback callback);
}
