package com.expo.network.response;

import com.expo.entity.ActualScene;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpotsResp extends BaseResponse {
    @SerializedName(value = "Updatetime", alternate = {"UpTime", "UpdateTime"})
    public String updateTime;
    @SerializedName("ParksList")
    public List<ScenicSpot> scenicSpots;
    @SerializedName("VenuesList")
    public List<ActualScene> actualScenes;
}
