package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.expo.network.Http;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = "park")
public class Park implements Parcelable {

    @DatabaseField(columnName = "_id", id = true)
    @SerializedName("id")
    private Long id;

    @DatabaseField(columnName = "electronic_fence_list")
    @SerializedName("electronicfencelist")
    private String electronicFenceList;

    @DatabaseField(columnName = "park_wifi_id")
    @SerializedName("parkwifiid")
    private String parkWifiId;

    @DatabaseField(columnName = "park_wifi_psd")
    @SerializedName("parkwifipsd")
    private String parkWifiPsd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<double[]> getElectronicFenceList() {
        return Http.getGsonInstance().fromJson( electronicFenceList, new TypeToken<ArrayList<double[]>>() {
        }.getType() );
    }

    public void setElectronicFenceList(String electronicFenceList) {
        this.electronicFenceList = electronicFenceList;
    }

    public String getParkWifiId() {
        return parkWifiId;
    }

    public void setParkWifiId(String parkWifiId) {
        this.parkWifiId = parkWifiId;
    }

    public String getParkWifiPsd() {
        return parkWifiPsd;
    }

    public void setParkWifiPsd(String parkWifiPsd) {
        this.parkWifiPsd = parkWifiPsd;
    }

    public Park() {
    }

    protected Park(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        electronicFenceList = in.readString();
        parkWifiId = in.readString();
        parkWifiPsd = in.readString();
    }

    public static final Creator<Park> CREATOR = new Creator<Park>() {
        @Override
        public Park createFromParcel(Parcel in) {
            return new Park(in);
        }

        @Override
        public Park[] newArray(int size) {
            return new Park[size];
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
        dest.writeString(electronicFenceList);
        dest.writeString(parkWifiId);
        dest.writeString(parkWifiPsd);
    }
}
