package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Find implements Parcelable {

    public String id;
    public String name;
    public String head;
    public List<String> picUrl;
    public String like;
    public String scans;
    public String content;
    public String title;
    public String time;
    public int state;

    public Find() {

    }

    protected Find(Parcel in) {
        id = in.readString();
        name = in.readString();
        head = in.readString();
        picUrl = in.createStringArrayList();
        like = in.readString();
        scans = in.readString();
        content = in.readString();
        title = in.readString();
        time = in.readString();
        state = in.readInt();
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
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(head);
        dest.writeStringList(picUrl);
        dest.writeString(like);
        dest.writeString(scans);
        dest.writeString(content);
        dest.writeString(title);
        dest.writeString(time);
        dest.writeInt(state);
    }
}
