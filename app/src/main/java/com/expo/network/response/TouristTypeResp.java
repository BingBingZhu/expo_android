package com.expo.network.response;

import com.expo.entity.TouristType;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TouristTypeResp extends BaseResponse {
    @SerializedName("ObjList")
    public List<TouristType> touristTypes;
    @SerializedName("UpdateTime")
    public String updateTime;
}
