package com.expo.module.map;

import com.amap.api.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

class MyLatLng {
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;

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
}
