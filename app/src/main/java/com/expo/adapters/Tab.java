package com.expo.adapters;

import android.os.Parcelable;

public interface Tab extends Parcelable {
    String getTab();

    String getEnTab();

    Long getId();

    Object getData();
}
