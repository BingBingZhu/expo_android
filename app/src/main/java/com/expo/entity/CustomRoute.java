package com.expo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.model.NaviLatLng;
import com.expo.module.map.MyLatLng;
import com.expo.network.Http;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "custom_route")
public class CustomRoute implements Parcelable {

    @DatabaseField(columnName = "_id", generatedId = true, allowGeneratedIdInsert = true)
    private Long id;
    @DatabaseField(columnName = "start_id")
    private Long startId;
    @DatabaseField(columnName = "end_id")
    private Long endId;
    @DatabaseField(columnName = "point_json")
    private String pointsJSON;
    @DatabaseField(columnName = "distance")
    private double distance;
    @DatabaseField(columnName = "duration")
    private double duration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStartId() {
        return startId;
    }

    public void setStartId(Long startId) {
        this.startId = startId;
    }

    public Long getEndId() {
        return endId;
    }

    public void setEndId(Long endId) {
        this.endId = endId;
    }

    public List<LatLng> getPoints() {
        List<LatLng> latLngs = new ArrayList<>();
        List<MyLatLng> myLatLngs = Http.getGsonInstance().fromJson(pointsJSON, new TypeToken<List<MyLatLng>>(){}.getType());
        for (MyLatLng mll : myLatLngs){
            latLngs.add(mll.getLatLng());
        }
        return latLngs;
    }

    public void setPoints(List<NaviLatLng> latLngs) {
        List<MyLatLng> myLatLngs = new ArrayList<>();
        for (NaviLatLng latLng : latLngs){
            myLatLngs.add(new MyLatLng(latLng));
        }
        this.pointsJSON = Http.getGsonInstance().toJson(myLatLngs);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public CustomRoute() {
    }

    protected CustomRoute(Parcel in) {
        if (in.readByte() == 0) {
            startId = null;
        } else {
            startId = in.readLong();
        }
        if (in.readByte() == 0) {
            endId = null;
        } else {
            endId = in.readLong();
        }
        pointsJSON = in.readString();
    }

    public static final Creator<CustomRoute> CREATOR = new Creator<CustomRoute>() {
        @Override
        public CustomRoute createFromParcel(Parcel in) {
            return new CustomRoute(in);
        }

        @Override
        public CustomRoute[] newArray(int size) {
            return new CustomRoute[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (startId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(startId);
        }
        if (endId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(endId);
        }
        dest.writeString(pointsJSON);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomRoute that = (CustomRoute) o;

        if (!startId.equals(that.startId)) return false;
        return endId.equals(that.endId);
    }

    @Override
    public int hashCode() {
        int result = startId.hashCode();
        result = 31 * result + endId.hashCode();
        return result;
    }
}
