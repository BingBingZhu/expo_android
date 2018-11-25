package com.expo.network.response;

import com.expo.entity.Venue;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VenueResp extends BaseResponse {
    @SerializedName(value = "Updatetime", alternate = {"UpTime", "UpdateTime"})
    public String updateTime;
    @SerializedName("VenuesList")
    public List<Venue> venues;
}
