package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Find implements Parcelable {
    protected Find() {

    }

    protected Find(Parcel in) {
    }

    public static final Creator<Find> CREATOR = new Creator<Find>() {
        @Override
        public Find createFromParcel(Parcel in) {
            return new Find(in);
        }

        @Override
        public Find[] newArray(int size) {
            return new Find[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
