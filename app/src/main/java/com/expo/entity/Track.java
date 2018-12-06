package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Track")
public class Track implements Parcelable {

    @DatabaseField(columnName = "_id", generatedId = true, allowGeneratedIdInsert = true)
    private Long id;
    @DatabaseField(columnName = "lat")
    private double lat;
    @DatabaseField(columnName = "lng")
    private double lng;
    @DatabaseField(columnName = "flag")
    private Long flag;
    @DatabaseField(columnName = "uid")
    private String uid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Long getFlag() {
        return flag;
    }

    public void setFlag(Long flag) {
        this.flag = flag;
    }

    public Track() {
    }

    public Track(double lat, double lng, Long flag, String uid) {
        this.lat = lat;
        this.lng = lng;
        this.flag = flag;
        this.uid = uid;
    }

    protected Track(Parcel in) {
        id = in.readLong();
        lat = in.readInt();
        lng = in.readInt();
        flag = in.readLong();
        uid = in.readString();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeLong(flag);
        dest.writeString(uid);
    }
}
