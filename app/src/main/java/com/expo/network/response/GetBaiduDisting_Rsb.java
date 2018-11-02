package com.expo.network.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.expo.entity.BaikeInfo;

public class GetBaiduDisting_Rsb implements Parcelable {
    public Double score;
    public String name;
    public BaikeInfo baike_info;

    protected GetBaiduDisting_Rsb(Parcel in) {
        if (in.readByte() == 0) {
            score = null;
        } else {
            score = in.readDouble();
        }
        name = in.readString();
    }

    public static final Creator<GetBaiduDisting_Rsb> CREATOR = new Creator<GetBaiduDisting_Rsb>() {
        @Override
        public GetBaiduDisting_Rsb createFromParcel(Parcel in) {
            return new GetBaiduDisting_Rsb(in);
        }

        @Override
        public GetBaiduDisting_Rsb[] newArray(int size) {
            return new GetBaiduDisting_Rsb[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (score == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(score);
        }
        dest.writeString(name);
    }
}
