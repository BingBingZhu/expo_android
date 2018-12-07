package com.expo.network.response;

import com.expo.entity.Find;
import com.expo.entity.Venue;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SocietyListResp extends BaseResponse {
    @SerializedName(value = "Updatetime", alternate = {"UpTime", "UpdateTime"})
    public String updateTime;
    @SerializedName("Objlist")
    public List<Find> finds;
}
