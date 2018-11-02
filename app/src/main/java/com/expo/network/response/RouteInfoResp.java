package com.expo.network.response;

import com.expo.entity.RouteInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteInfoResp extends BaseResponse {
    @SerializedName("UpdateTime")
    public String updateTime;
    @SerializedName("ObjList")
    public List<RouteInfo> routeList;
}
