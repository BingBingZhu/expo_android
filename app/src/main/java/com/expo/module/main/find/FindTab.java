package com.expo.module.main.find;

import android.os.Parcel;
import android.os.Parcelable;

public class FindTab implements Parcelable {

    public Long id;
    public int tab;

    public FindTab(Long id, int tab){
        this.id = id;
        this.tab = tab;
    }

    protected FindTab(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
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
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeInt(tab);
    }
}
