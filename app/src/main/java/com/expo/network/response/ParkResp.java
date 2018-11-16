package com.expo.network.response;

import com.expo.entity.Park;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParkResp extends BaseResponse {
    @SerializedName("Updatetime")
    public String updateTime;
    @SerializedName("ParksList")
    public List<Park> parkList;
}
