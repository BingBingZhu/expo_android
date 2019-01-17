//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.vr.sdk.widgets.common;

import android.app.Activity;
import android.os.Bundle;

class OrientationHelper {
    private static final String STATE_KEY_IS_ORIENTATION_LOCKED = "isOrientationLocked";
    private static final String STATE_KEY_ORIGINAL_REQUESTED_ORIENTATION = "originalRequestedOrientation";
    private Activity activity;
    private boolean isOrientationLocked;
    private int originalRequestedOrientation;

    public OrientationHelper(Activity activity) {
        this.activity = activity;
    }

    public boolean isInPortraitOrientation() {
        return this.activity.getResources().getConfiguration().orientation == 1;
    }

    public void lockOrientation() {
        if (!this.isOrientationLocked) {
            this.originalRequestedOrientation = this.activity.getRequestedOrientation();
            this.activity.setRequestedOrientation(this.isInPortraitOrientation() ? 1 : 0);
            this.isOrientationLocked = true;
        }
    }

    public void restoreOriginalOrientation() {
        this.isOrientationLocked = false;
        this.activity.setRequestedOrientation(this.originalRequestedOrientation);
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isOrientationLocked", this.isOrientationLocked);
        bundle.putInt("originalRequestedOrientation", this.originalRequestedOrientation);
        return bundle;
    }

    public void onRestoreInstanceState(Bundle state) {
        this.originalRequestedOrientation = state.getInt("originalRequestedOrientation");
        this.isOrientationLocked = state.getBoolean("isOrientationLocked");
    }
}
