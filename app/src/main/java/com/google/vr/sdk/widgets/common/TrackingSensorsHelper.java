//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.vr.sdk.widgets.common;

import android.content.pm.PackageManager;

public class TrackingSensorsHelper {
    public static boolean pretendSensorsAreAvailableForTesting = false;
    public static boolean enableTouchTracking = false;
    public static boolean showStereoModeButtonForTesting = false;
    PackageManager packageManager;

    public TrackingSensorsHelper(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public boolean areTrackingSensorsAvailable() {
        if (pretendSensorsAreAvailableForTesting) {
            return true;
        } else if (enableTouchTracking) {
            return false;
        } else {
            return this.packageManager.hasSystemFeature("android.hardware.sensor.gyroscope") && this.packageManager.hasSystemFeature("android.hardware.sensor.accelerometer");
        }
    }

    public boolean showStereoModeButtonForTesting() {
        return showStereoModeButtonForTesting;
    }
}
