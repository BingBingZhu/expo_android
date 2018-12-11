package com.expo.module.map;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.model.NaviLatLng;
import com.google.gson.annotations.SerializedName;

public class MyLatLng {
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;

    public MyLatLng(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public MyLatLng(NaviLatLng latLng){
        setLatLng(latLng);
    }

    public MyLatLng(){

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public LatLng getLatLng(){
        return new LatLng(lat, lon);
    }

    public void setLatLng(NaviLatLng latLng){
        this.lat = latLng.getLatitude();
        this.lon = latLng.getLongitude();
    }
}
