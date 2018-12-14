package com.expo.module.main.find;

import android.os.Parcel;
import android.os.Parcelable;

public class FindTab implements Parcelable {

    public String id;
    public int tab;

    public FindTab(String id, int tab){
        this.id = id;
        this.tab = tab;
    }

    protected FindTab(Parcel in) {
        id = in.readString();
        tab = in.readInt();
    }

    public static final Creator<FindTab> CREATOR = new Creator<FindTab>() {
        @Override
        public FindTab createFromParcel(Parcel in) {
            return new FindTab(in);
        }

        @Override
        public FindTab[] newArray(int size) {
            return new FindTab[size];
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
