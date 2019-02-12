//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.vr.sdk.widgets.common;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class VrWidgetRenderer implements Renderer {
    private static final String TAG = VrWidgetRenderer.class.getSimpleName();
    protected static final boolean DEBUG = false;
    private static final String STATE_KEY_CURRENT_YAW = "currentYaw";
    private long nativeRenderer;
    private final Context context;
    private float xMetersPerPixel;
    private float yMetersPerPixel;
    public static boolean disableRenderingForTesting;
    private final int screenRotation;
    private final GLThreadScheduler glThreadScheduler;
    private volatile SetStereoModeRequest lastSetStereoModeRequest;
    private float[] tmpHeadAngles = new float[2];
    private float currentYaw;
    public boolean isLanscape = false;

    protected VrWidgetRenderer(Context context, GLThreadScheduler glThreadScheduler, float xMetersPerPixel, float yMetersPerPixel, int screenRotation) {
        this.context = context;
        this.glThreadScheduler = glThreadScheduler;
        this.xMetersPerPixel = xMetersPerPixel;
        this.yMetersPerPixel = yMetersPerPixel;
        this.screenRotation = screenRotation;
    }

    public void setStereoMode(boolean stereoMode) {
        this.lastSetStereoModeRequest = new SetStereoModeRequest(stereoMode);
        this.postApiRequestToGlThread(this.lastSetStereoModeRequest);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (this.nativeRenderer != 0L) {
            this.nativeDestroy(this.nativeRenderer);
        }

        this.nativeRenderer = this.nativeCreate(this.getClass().getClassLoader(), this.context.getApplicationContext(), this.currentYaw);
        if (this.lastSetStereoModeRequest != null) {
            this.executeApiRequestOnGlThread(this.lastSetStereoModeRequest);
        }

    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        int screenRotation = this.screenRotation;
        if (isLanscape) screenRotation = 90;
        this.nativeResize(this.nativeRenderer, w, h, this.xMetersPerPixel, this.yMetersPerPixel, screenRotation);
    }

    public void onDrawFrame(GL10 gl) {
        if (this.nativeRenderer != 0L) {
            this.nativeRenderFrame(this.nativeRenderer);
        }

    }

    public void updateCurrentYaw() {
        this.getHeadRotation(this.tmpHeadAngles);
        this.currentYaw = this.tmpHeadAngles[0];
    }

    protected long getNativeRenderer() {
        return this.nativeRenderer;
    }

    public void shutdown() {
        if (this.nativeRenderer != 0L) {
            this.nativeDestroy(this.nativeRenderer);
            this.nativeRenderer = 0L;
        }

    }

    public void onPanningEvent(float translationPixelX, float translationPixelY) {
        if (this.nativeRenderer != 0L) {
            this.nativeOnPanningEvent(this.nativeRenderer, translationPixelX, translationPixelY);
        }

    }

    public void getHeadRotation(float[] yawAndPitch) {
        if (this.nativeRenderer != 0L) {
            this.nativeGetHeadRotation(this.nativeRenderer, yawAndPitch);
        }

    }

    protected void onViewDetach() {
    }

    public void onPause() {
        if (this.nativeRenderer != 0L) {
            this.nativeOnPause(this.nativeRenderer);
        }

    }

    public void onResume() {
        if (this.nativeRenderer != 0L) {
            this.nativeOnResume(this.nativeRenderer);
        }

    }

    protected void postApiRequestToGlThread(final ApiRequest apiRequest) {
        this.glThreadScheduler.queueGlThreadEvent(new Runnable() {
            public void run() {
                VrWidgetRenderer.this.executeApiRequestOnGlThread(apiRequest);
            }
        });
    }

    protected void executeApiRequestOnGlThread(ApiRequest apiRequest) {
        if (disableRenderingForTesting) {
            Log.i(TAG, "disableRenderingForTesting");
        } else {
            if (this.nativeRenderer == 0L) {
                Log.i(TAG, "Native renderer has just been destroyed. Dropping request.");
            } else {
                apiRequest.execute();
            }

        }
    }

    public Bundle onSaveInstanceState() {
        this.updateCurrentYaw();
        Bundle bundle = new Bundle();
        bundle.putFloat("currentYaw", this.currentYaw);
        return bundle;
    }

    protected void onRestoreInstanceState(Bundle state) {
        this.currentYaw = state.getFloat("currentYaw");
    }

    protected abstract long nativeCreate(ClassLoader var1, Context var2, float var3);

    protected abstract void nativeResize(long var1, int var3, int var4, float var5, float var6, int var7);

    protected abstract void nativeDestroy(long var1);

    protected abstract void nativeRenderFrame(long var1);

    protected abstract void nativeSetStereoMode(long var1, boolean var3);

    protected abstract void nativeOnPause(long var1);

    protected abstract void nativeOnResume(long var1);

    protected abstract void nativeOnPanningEvent(long var1, float var3, float var4);

    protected abstract void nativeGetHeadRotation(long var1, float[] var3);

    private class SetStereoModeRequest implements ApiRequest {
        public final boolean stereoMode;

        public SetStereoModeRequest(boolean stereoMode) {
            this.stereoMode = stereoMode;
        }

        public void execute() {
            VrWidgetRenderer.this.nativeSetStereoMode(VrWidgetRenderer.this.nativeRenderer, this.stereoMode);
        }
    }

    protected interface ApiRequest {
        void execute();
    }

    public interface GLThreadScheduler {
        void queueGlThreadEvent(Runnable var1);
    }
}
