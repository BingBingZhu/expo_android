package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "actual_scene")
public class ActualScene extends Scene {
    @DatabaseField(columnName = "is_lined_ar")
    @SerializedName("islinedar")
    private String isLinedAr;
    @DatabaseField(columnName = "ar_link_url")
    @SerializedName("arlinkurl")
    private String arLinkUrl;


    public ActualScene() {
    }

    protected ActualScene(Parcel in) {
        super(in);
        isLinedAr = in.readString();
        arLinkUrl = in.readString();
    }

    public static final Parcelable.Creator<ActualScene> CREATOR = new Parcelable.Creator<ActualScene>() {
        @Override
        public ActualScene createFromParcel(Parcel in) {
            return new ActualScene(in);
        }

        @Override
        public ActualScene[] newArray(int size) {
            return new ActualScene[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(isLinedAr);
        dest.writeString(arLinkUrl);
    }

    public String getIsLinedAr() {
        return isLinedAr;
    }

    public void setIsLinedAr(String isLinedAr) {
        this.isLinedAr = isLinedAr;
    }

    public String getArLinkUrl() {
        return arLinkUrl;
    }

    public void setArLinkUrl(String arLinkUrl) {
        this.arLinkUrl = arLinkUrl;
    }
}
