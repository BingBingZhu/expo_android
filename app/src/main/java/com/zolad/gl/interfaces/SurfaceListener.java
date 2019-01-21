package com.zolad.gl.interfaces;

import android.graphics.SurfaceTexture;

public interface SurfaceListener {
    void onSurfaceCreated(SurfaceTexture surface);

    void onSurfaceChanged(SurfaceTexture surface, int width, int height);

    void onSurfaceUpdate(SurfaceTexture surface);
}
