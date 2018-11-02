package com.expo.adapters;

import android.os.Parcelable;

public interface Tab extends Parcelable {
    String getTab();

    Long getId();

    Object getData();
}
