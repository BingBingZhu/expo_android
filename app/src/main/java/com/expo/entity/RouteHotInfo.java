package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class RouteHotInfo implements Parcelable {

    public String element;
    public int score;

    protected RouteHotInfo() {

    }

    protected RouteHotInfo(Parcel in) {
        element = in.readString();
        score = in.readInt();
    }

    public static final Creator<RouteHotInfo> CREATOR = new Creator<RouteHotInfo>() {
        @Override
        public RouteHotInfo createFromParcel(Parcel in) {
            return new RouteHotInfo(in);
        }

        @Override
        public RouteHotInfo[] newArray(int size) {
            return new RouteHotInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(element);
        dest.writeInt(score);
    }
}
