package com.expo.module.online.scene;

import android.os.Parcel;
import android.os.Parcelable;

public class SceneTab implements Parcelable {

    public String id;
    public int tab;

    public SceneTab(String id, int tab){
        this.id = id;
        this.tab = tab;
    }

    protected SceneTab(Parcel in) {
        id = in.readString();
        tab = in.readInt();
    }

    public static final Creator<SceneTab> CREATOR = new Creator<SceneTab>() {
        @Override
        public SceneTab createFromParcel(Parcel in) {
            return new SceneTab(in);
        }

        @Override
        public SceneTab[] newArray(int size) {
            return new SceneTab[size];
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
