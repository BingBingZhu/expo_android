//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.vr.sdk.widgets.common;

import android.util.Log;

public class VrEventListener {
    private static final String TAG = VrEventListener.class.getSimpleName();
    private static final boolean DEBUG = false;

    public VrEventListener() {
    }

    public void onLoadSuccess() {
    }

    public void onLoadError(String errorMessage) {
        String var10000 = TAG;
        int var2 = this.hashCode();
        Log.e(var10000, (new StringBuilder(25 + String.valueOf(errorMessage).length())).append(var2).append(".onLoadError: ").append(errorMessage).toString());
    }

    public void onClick() {
    }

    public void onDisplayModeChanged(int newDisplayMode) {
    }
}
