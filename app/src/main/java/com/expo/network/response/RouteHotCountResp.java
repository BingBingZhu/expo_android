package com.expo.network.response;

import com.expo.entity.RouteHotInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteHotCountResp extends BaseResponse {
    @SerializedName("UpdateTime")
    public String updateTime;
    @SerializedName("hotinfos")
    public List<RouteHotInfo> routeHots;
}
