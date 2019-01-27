package com.expo.module.online.culture;

import android.os.Parcel;
import android.os.Parcelable;

public class CultureTab implements Parcelable {

    public String id;
    public int tab;

    public CultureTab(String id, int tab){
        this.id = id;
        this.tab = tab;
    }

    protected CultureTab(Parcel in) {
        id = in.readString();
        tab = in.readInt();
    }

    public static final Creator<CultureTab> CREATOR = new Creator<CultureTab>() {
        @Override
        public CultureTab createFromParcel(Parcel in) {
            return new CultureTab(in);
        }

        @Override
        public CultureTab[] newArray(int size) {
            return new CultureTab[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(tab);
    }
}
